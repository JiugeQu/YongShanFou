package com.mizore.mob.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mizore.mob.dto.OrderDTO;
import com.mizore.mob.dto.OrderDishDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.*;
import com.mizore.mob.exception.OrderException;
import com.mizore.mob.mapper.*;
import com.mizore.mob.message.SseServer;
import com.mizore.mob.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.util.BeanUtil;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mizore.mob.util.Constant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private OrderDishMapper orderDishMapper;
    private SnowflakeGenerator snowflakeGenerator;
    private DishMapper dishMapper;

    private OrderMapper orderMapper;
    private SseServer sseServer;

    private UserMapper userMapper;

    private CommentMapper commentMapper;

    @Override
    /** 事务方法中抛出RuntimeException和Error时才会生效
     * 在类内部调用调用类内部@Transactional标注的方法，这种情况下会导致事务不开启。
     * 所以这里在外层方法（createOrder）加注解，而不是 doCreateOrder
     **/
    @Transactional
    public Result createOrder(List<OrderDish> orderDishes, String address, String note, LocalDateTime expectArriveTime) {
        if (!doCreateOrder(orderDishes, address, note, expectArriveTime)) {
            return Result.error("下单失败！！");
        }
        // 响应给已建立sse连接的餐厅员工
        sseServer.batchSendMessage(SSE_REFRESH_MESSAGE, STAFF_PREFIX);
        return Result.ok();
    }



    public boolean doCreateOrder(List<OrderDish> orderDishes,
                                String address,
                                String note,
                                LocalDateTime expectArriveTime) {
        // 创建订单
        Order order = new Order();
        // 获取当前登录顾客
        User user = UserHolder.get();
        // 设置下单用户
        order.setUserId(user.getId());
        // 创建订单
        // 设置订单编号
        Long code = snowflakeGenerator.next();
        order.setCode(code);
        // 设置状态
        order.setState(ORDER_WAIT_CONFIRM);
        // 设置地址
        order.setAddress(address);
        // 设置备注
        order.setNote(note);
        // 设置期望送达时间
        order.setExpectArriveTime(expectArriveTime);
        // 在订单菜品关系表插入一个个菜品项
        BigDecimal sum = BigDecimal.valueOf(0);
        for (OrderDish orderDish : orderDishes) {
            // 根据菜id拿到菜价格，累加计算订单总价，并对菜品销量自增
            Integer dishId = orderDish.getDishId();
            Dish dish = dishMapper.selectById(dishId);
            // 判断菜品项的有效性
            if (dish == null ||  DISH_OUT_SALE.equals(dish.getState())) {
                throw new OrderException("菜品不存在或已经下架！！");
            }
            sum = sum.add(dish.getPrice().multiply(BigDecimal.valueOf(orderDish.getDishCount())));
            orderDish.setOrderCode(code);
            // 把菜品项插入数据库
            orderDishMapper.insert(orderDish);
            dish.setSale(dish.getSale() + 1);
            dishMapper.updateById(dish);
        };
        // 计算总价
        order.setTotalPrice(sum);
        // 把订单存入数据库
        return save(order);
    }


    /**
     * 商家接单
     */
    @Override
    public Result confirm(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_PREPARING)
                .eq("state", ORDER_WAIT_CONFIRM)
                .eq("code", orderCode);
        boolean res = update(wrapper);
        // 响应给顾客: confirmed
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_CONFIRMED_MESSAGE);
        // 响应给其他餐厅员工
        sseServer.sendMessage(SSE_REFRESH_MESSAGE, CUSTOMER_PREFIX);
        return res ? Result.ok() : Result.error("接单失败！！");
    }

    /**
     * 商家备餐完成
     */
    @Override
    public Result finish(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_WAIT_DELIVERED)
                .eq("state", ORDER_PREPARING)
                .eq("code", orderCode);
        boolean res = update(wrapper);
        // 响应给已建立sse连接的骑手
        sseServer.batchSendMessage(SSE_REFRESH_MESSAGE, DELIVERY_PREFIX);
        // 响应给顾客: preparation completed
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_COMPLETED_MESSAGE);
        return res ? Result.ok() : Result.error("状态变更失败！！");
    }


    /**
     * 骑手派送
     */
    @Override
    public Result deliver(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_DELIVERING)
                .eq("state", ORDER_WAIT_DELIVERED)
                .eq("code", orderCode);
        boolean res = update(wrapper);
        // 响应给顾客: delivering
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_DELIVERING_MESSAGE);
        // 响应给餐厅员工: delivering
        sseServer.batchSendMessage(SSE_DELIVERING_MESSAGE, CUSTOMER_PREFIX);
        return res ? Result.ok() : Result.error("状态变更失败！！");
    }

    @Override
    public Result getOrdersByStates(Byte[] states) {
        User user = UserHolder.get();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if (ROLE_CUSTOMER.equals(user.getRole())) {
            for (Byte state : states) {
                orderDTOs.addAll(getOrdersByStateForCustomer(state, user.getId()));
            }
        } else if (ROLE_DELIVERY.equals(user.getRole())) {
            for (Byte state : states) {
                orderDTOs.addAll(getOrdersByStateForDeliver(state, user.getId()));
            }
        } else {
            for (Byte state : states) {
                orderDTOs.addAll(getOrdersByStateForStaff(state));
            }
        }
        return Result.ok(orderDTOs);
    }

    @Override
    public Result getOrdersByState(Byte state) {
        User user = UserHolder.get();
        if (ROLE_CUSTOMER.equals(user.getRole())) {
            return Result.ok(getOrdersByStateForCustomer(state, user.getId()));
        } else if (ROLE_DELIVERY.equals(user.getRole())) {
            return Result.ok(getOrdersByStateForDeliver(state, user.getId()));
        } else {
            return Result.ok(getOrdersByStateForStaff(state));
        }
    }

    @Override
    public Result getOrderByCode(Long orderCode) {
        User user = UserHolder.get();
        if (ROLE_CUSTOMER.equals(user.getRole())) {
            return Result.ok(getOrderByCodeForCustomer(orderCode, user.getId()));
        } else if (ROLE_DELIVERY.equals(user.getRole())) {
            return Result.ok(getOrderByCodeForDeliver(orderCode));
        } else {
            return Result.ok(getOrderByCodeForStaff(orderCode));
        }
    }

    /**
     * 骑手查看所有已送达的历史订单
     * 订单号，状态，点餐内容（菜名+图片），期望送达时间，实际送达时间，地址
     * @return
     */
    @Override
    public Result getHistoryOrdersOfDeliver() {
        User user = UserHolder.get();
        List<Order> orders = query().select("code", "state", "expect_arrive_time", "real_arrive_time", "address")
                .eq("deliver_id", user.getId())
                .list();
        List<OrderDTO> orderDTOs = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtil.copyNotNullProperties(order, orderDTO);

            // 填充点餐内容  每个菜品项的菜名+图片
            long orderCode = order.getCode();
            QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("dish_id")
                    .eq("order_code",orderCode);
            List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
            List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
            for (OrderDish orderDish : orderDishes) {
                int dishId = orderDish.getDishId();
                QueryWrapper<Dish> wrapper = new QueryWrapper<>();
                wrapper.select("image", "name")
                        .eq("id", dishId);
                Dish dish = dishMapper.selectOne(wrapper);
                OrderDishDTO orderDishDTO = new OrderDishDTO();
                orderDishDTO.setDishImg(dish.getImage())
                        .setDishName(dish.getName());
                orderDishDTOs.add(orderDishDTO);
            }
            orderDTO.setOrderDishes(orderDishDTOs);
            orderDTOs.add(orderDTO);
        }
        return Result.ok(orderDTOs);
    }

    /**
     * 骑手送达
     */
    @Override
    public Result arrived(Long orderCode) {

        // 更改订单状态
        boolean res = update()
                .set("state", ORDER_DELIVERED)
                .eq("state", ORDER_DELIVERING)
                .eq("code", orderCode)
                .update();

        // 响应给顾客: arrived
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_DELIVERING_MESSAGE);
        return res ? Result.ok() : Result.error("订单状态变更失败！！");
    }

    /**
     * 对于未配送（状态为 1、2、3 ）的订单，返回订单的订单号，状态，菜品名字图片价格数量，订单总价，用户地址姓名电话，备注内容，期望送达时间
     * 对于配送中和送达未评价（状态为4、5）的订单，在上述内容中多返回配送员姓名+电话
     * 对于已评价（状态为6）的订单，在上述内容中多返回配送员姓名+电话+评价内容
     * @param orderCode 订单号
     */
    private Object getOrderByCodeForStaff(Long orderCode) {
        Order order = query()
                .select("code", "state", "total_price", "address", "note", "expect_arrive_time", "deliver_id", "user_id")
                .eq("code", orderCode)
                .one();

        OrderDTO res = new OrderDTO();
        BeanUtil.copyNotNullProperties(order, res);

        // 1. 装填： 菜品名字+图片+价格+数量
        // 1.1 先查到该订单对应的所有菜品项
        QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("dish_id", "dish_count")
                .eq("order_code",orderCode);
        List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
        List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
        // 1.2 去dish表补全每个菜品项信息  List<OrderDish> -> List<OrderDishDTO>
        for (OrderDish orderDish : orderDishes) {
            int dishId = orderDish.getDishId();
            QueryWrapper<Dish> wrapper = new QueryWrapper<>();
            wrapper.select("image", "name", "price")
                    .eq("id", dishId);
            Dish dish = dishMapper.selectOne(wrapper);
            OrderDishDTO orderDishDTO = new OrderDishDTO();
            orderDishDTO.setDishImg(dish.getImage())
                    .setDishName(dish.getName())
                    .setDishPrice(dish.getPrice())
                    .setDishCount(orderDish.getDishCount());
            orderDishDTOs.add(orderDishDTO);
        }
        // 1.3 执行装填完整点餐内容列表
        res.setOrderDishes(orderDishDTOs);


        // 2. 填充顾客姓名电话
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("name", "phone")
                .eq("id", order.getUserId());
        User customer = userMapper.selectOne(wrapper);
        res.setCustomerName(customer.getName());
        res.setCustomerPhone(customer.getPhone());

        // 3. 对于配送中和送达未评价（状态为4、5）的订单，填充配送员姓名+电话
        if (order.getState() > 3) {
            Integer deliverId = order.getDeliverId();
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.select("name", "phone")
                    .eq("id", deliverId);
            User deliver = userMapper.selectOne(qw);
            res.setDeliverName(deliver.getName());
            res.setDeliverPhone(deliver.getPhone());
        }

        // 4. 对于状态为 6 的订单，填充评价内容
        if (ORDER_COMMENTED.equals(order.getState())) {
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.select("content")
                    .eq("order_code", orderCode);
            Comment comment = commentMapper.selectOne(commentQueryWrapper);
            res.setCommentContent(comment.getContent());
        }

        // 5. 清空不必返回的属性
        res.setUserId(null);
        res.setDeliverId(null);

        return res;
    }

    private Object getOrderByCodeForDeliver(Long orderCode) {
        return Result.ok();
    }

    /**
     * 先去校验订单号对于的下单顾客是不是当前用户，防止订单泄漏。
     * 对于未送达（状态小于 5 ）的订单，返回订单的订单号，状态，菜品名字图片价格数量，订单总价，用户地址姓名电话，备注内容，期望送达时间
     * 对于状态为 5 的订单，在上述内容中多返回实际送达时间
     * 对于状态为 6 的订单，多返回实际送达时间和评价内容
     * @param orderCode 订单号
     * @param id 顾客id
     */
    private Object getOrderByCodeForCustomer(Long orderCode, Integer id) {
        // 1. 先去校验订单号对于的下单顾客是不是当前用户，防止订单泄漏。
        Order order = query()
                .select("code", "state", "total_price", "address", "note", "expect_arrive_time", "real_arrive_time")
                .eq("code", orderCode)
                .eq("user_id", id)
                .one();
        if (order == null) {
            return Result.error("顾客与订单号不匹配！！");
        }

        OrderDTO res = new OrderDTO();
        BeanUtil.copyNotNullProperties(order, res);

        // 2. 装填： 菜品名字+图片+价格+数量
        // 2.1 先查到该订单对应的所有菜品项
        QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("dish_id", "dish_count")
                .eq("order_code",orderCode);
        List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
        List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
        // 2.2 去dish表补全每个菜品项信息  List<OrderDish> -> List<OrderDishDTO>
        for (OrderDish orderDish : orderDishes) {
            int dishId = orderDish.getDishId();
            QueryWrapper<Dish> wrapper = new QueryWrapper<>();
            wrapper.select("image", "name", "price")
                    .eq("id", dishId);
            Dish dish = dishMapper.selectOne(wrapper);
            OrderDishDTO orderDishDTO = new OrderDishDTO();
            orderDishDTO.setDishImg(dish.getImage())
                    .setDishName(dish.getName())
                    .setDishPrice(dish.getPrice())
                    .setDishCount(orderDish.getDishCount());
            orderDishDTOs.add(orderDishDTO);
        }
        // 2.3 执行装填完整点餐内容列表
        res.setOrderDishes(orderDishDTOs);


        // 3. 填充顾客姓名电话
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("name", "phone")
                .eq("id", id);
        User customer = userMapper.selectOne(wrapper);
        res.setCustomerName(customer.getName());
        res.setCustomerPhone(customer.getPhone());

        // 4. 对于状态为 6 的订单，填充评价内容
        if (ORDER_COMMENTED.equals(order.getState())) {
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.select("content")
                    .eq("order_code", orderCode);
            Comment comment = commentMapper.selectOne(commentQueryWrapper);
            res.setCommentContent(comment.getContent());
        }

        return res;
    }

    private List<OrderDTO> getOrdersByStateForStaff(Byte state) {
        // 订单号，点餐内容（菜名+图片+数量+菜价），总价，状态，创建时间，顾客名
        List<Order> orders = query()
                .select("code", "total_price", "state", "create_time", "user_id")
                .eq("state", state)
                .list();

        // 包装OrderDTO
        List<OrderDTO> res = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtil.copyNotNullProperties(order, orderDTO);

            // 填充点餐内容  每个菜品项的菜名+图片+数量+菜品单价
            long orderCode = order.getCode();
            // 先查到该订单对应的所有菜品项
            QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("dish_id", "dish_count").eq("order_code",orderCode);
            List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
            List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
            // orderDish -> orderDishDTO
            for (OrderDish orderDish : orderDishes) {
                int dishId = orderDish.getDishId();
                QueryWrapper<Dish> wrapper = new QueryWrapper<>();
                wrapper.select("image", "name", "price").eq("id", dishId);
                Dish dish = dishMapper.selectOne(wrapper);
                OrderDishDTO orderDishDTO = new OrderDishDTO();
                orderDishDTO.setDishImg(dish.getImage())
                        .setDishName(dish.getName())
                        .setDishPrice(dish.getPrice())
                        .setDishCount(orderDish.getDishCount());
                orderDishDTOs.add(orderDishDTO);
            }
            orderDTO.setOrderDishes(orderDishDTOs);

            // 填充顾客姓名
            Integer customerId = order.getUserId();
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.select("name").eq("id", customerId);
            User customer = userMapper.selectOne(wrapper);
            orderDTO.setCustomerName(customer.getName());
            res.add(orderDTO);
        }
        return res;
    }

    private List<OrderDTO> getOrdersByStateForDeliver(Byte state, Integer id) {
        // 订单号，状态，点餐内容（菜名+图片），期望送达时间，地址
        List<Order> orders = query()
                .select("code", "state", "expect_arrive_time", "address")
                .eq("state", state)
                .eq(state > 3, "deliver_id", id)
                .list();

        // 包装OrderDTO
        List<OrderDTO> res = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtil.copyNotNullProperties(order, orderDTO);

            // 填充点餐内容  每个菜品项的菜名+图片
            long orderCode = order.getCode();
            // 先查到该订单对应的所有菜品项
            QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("dish_id")
                    .eq("order_code",orderCode);
            List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
            List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
            // orderDish -> orderDishDTO
            for (OrderDish orderDish : orderDishes) {
                int dishId = orderDish.getDishId();
                QueryWrapper<Dish> wrapper = new QueryWrapper<>();
                wrapper.select("image", "name")
                        .eq("id", dishId);
                Dish dish = dishMapper.selectOne(wrapper);
                OrderDishDTO orderDishDTO = new OrderDishDTO();
                orderDishDTO.setDishImg(dish.getImage())
                        .setDishName(dish.getName());
                orderDishDTOs.add(orderDishDTO);
            }
            orderDTO.setOrderDishes(orderDishDTOs);

            res.add(orderDTO);
        }
        return res;

    }

    private List<OrderDTO> getOrdersByStateForCustomer(Byte state, Integer id) {
        // 订单号，点餐内容（菜名+图片），总价，状态, 配送员姓名（if state == 4）
        List<Order> orders = query()
                .select("code", "total_price", "state")
                .select(state == 4, "code", "total_price", "state", "deliver_id")   // 条件不满足会使这句失效，select连用有效select语句会覆盖前面的有效select
                .eq("state", state)
                .eq("user_id", id)
                .list();

        // 包装OrderDTO
        List<OrderDTO> res = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtil.copyNotNullProperties(order, orderDTO);

            // 填充点餐内容  每个菜品项的菜名+图片
            long orderCode = order.getCode();
            QueryWrapper<OrderDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("dish_id").eq("order_code",orderCode);
            List<OrderDish> orderDishes = orderDishMapper.selectList(queryWrapper);
            List<OrderDishDTO> orderDishDTOs = new ArrayList<>(orderDishes.size());
            for (OrderDish orderDish : orderDishes) {
                int dishId = orderDish.getDishId();
                QueryWrapper<Dish> wrapper = new QueryWrapper<>();
                wrapper.select("image", "name").eq("id", dishId);
                Dish dish = dishMapper.selectOne(wrapper);
                OrderDishDTO orderDishDTO = new OrderDishDTO();
                orderDishDTO.setDishImg(dish.getImage())
                        .setDishName(dish.getName());
                orderDishDTOs.add(orderDishDTO);
            }
            orderDTO.setOrderDishes(orderDishDTOs);

            // 填充配送员姓名
            if (state == 4) {
                Integer deliverId = order.getDeliverId();
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.select("name").eq("id", deliverId);
                User user = userMapper.selectOne(wrapper);
                orderDTO.setDeliverName(user.getName());
            }
            res.add(orderDTO);
        }
        return res;
    }
}


