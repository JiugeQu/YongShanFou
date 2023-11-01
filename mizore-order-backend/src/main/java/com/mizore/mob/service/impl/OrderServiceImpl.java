package com.mizore.mob.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.entity.Order;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.entity.User;
import com.mizore.mob.exception.OrderException;
import com.mizore.mob.mapper.DishMapper;
import com.mizore.mob.mapper.OrderDishMapper;
import com.mizore.mob.mapper.OrderMapper;
import com.mizore.mob.message.SseServer;
import com.mizore.mob.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.util.Constant;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Override
    public Result createOrder(List<OrderDish> orderDishes, String address, String note, LocalDateTime expectArriveTime) throws OrderException {
        if (!doCreateOrder(orderDishes, address, note, expectArriveTime)) {
            return Result.error("下单失败！！");
        }
        // 响应给已建立sse连接的餐厅员工
        sseServer.batchSendMessage(SSE_REFRESH_MESSAGE, STAFF_PREFIX);
        return Result.ok();
    }


    // 事务方法中抛出RuntimeException和Error时才会生效
    @Transactional(rollbackFor = OrderException.class)
    public boolean doCreateOrder(List<OrderDish> orderDishes,
                                String address,
                                String note,
                                LocalDateTime expectArriveTime) throws OrderException {
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


    @Override
    public Result confirm(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_PREPARING)
                .eq("state", ORDER_WAIT_CONFIRM)
                .eq("order_code", orderCode);
        boolean res = update(wrapper);
        // 响应给顾客: confirmed
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_CONFIRMED_MESSAGE);
        return res ? Result.ok() : Result.error("接单失败！！");
    }

    @Override
    public Result finish(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_WAIT_DELIVERED)
                .eq("state", ORDER_PREPARING)
                .eq("order_code", orderCode);
        boolean res = update(wrapper);
        // 响应给已建立sse连接的骑手
        sseServer.batchSendMessage(SSE_REFRESH_MESSAGE, DELIVERY_PREFIX);
        // 响应给顾客: preparation completed
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_COMPLETED_MESSAGE);
        return res ? Result.ok() : Result.error("状态变更失败！！");
    }

    @Override
    public Result deliver(Long orderCode) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.setSql("state = " + ORDER_DELIVERING)
                .eq("state", ORDER_WAIT_DELIVERED)
                .eq("order_code", orderCode);
        boolean res = update(wrapper);
        // 响应给顾客: delivering
        Integer userId = orderMapper.selectUserIdByCode(orderCode);
        sseServer.sendMessage(CUSTOMER_PREFIX + userId, SSE_DELIVERING_MESSAGE);
        // 响应给餐厅员工: delivering
        sseServer.batchSendMessage(SSE_DELIVERING_MESSAGE, CUSTOMER_PREFIX);
        return res ? Result.ok() : Result.error("状态变更失败！！");
    }
}


