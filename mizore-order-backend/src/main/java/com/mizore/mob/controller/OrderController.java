package com.mizore.mob.controller;

import com.mizore.mob.annotation.Idempotent;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Order;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.exception.OrderException;
import com.mizore.mob.service.IOrderService;
import com.mizore.mob.util.Constant;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

import static com.mizore.mob.util.Constant.ORDER_WAIT_CONFIRM;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private IOrderService orderService;

    /**
     * 顾客下单
     * @param orderDishes 订单菜品内容
     * @param address 顾客地址
     * @param note 备注
     * @param expectArriveTime 顾客设置的期望送达时间
     * @return
     * @throws OrderException
     */
    @PostMapping
    @Idempotent(expireTime = 10)
    public Result createOrder(
            @RequestBody List<OrderDish> orderDishes,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "note") String note,
            @RequestParam(value = "expectArriveTime")LocalDateTime expectArriveTime
            ) throws OrderException {
        return orderService.createOrder(orderDishes, address, note, expectArriveTime);
    }

    /**
     * 获取指定状态的订单
     * @return
     */
    @GetMapping("/state/{state}")
    public Result getOrdersByState(@PathVariable("state") Byte state) {
        List<Order> orders = orderService.query().eq("state", state).list();
        return Result.ok(orders);
    }

    /**
     * 商家对 待确认的顾客订单 进行接单操作
     * @param orderCode
     * @return
     */
    @PutMapping("confirm/{orderCode}")
    public Result confirm(@PathVariable("orderCode") Long orderCode) {
        return orderService.confirm(orderCode);
    }

    /**
     * 商家对 备餐完成的订单 进行接受配送操作
     * @param orderCode
     * @return
     */
    @PutMapping("/finish/{orderCode}")
    public Result finish(@PathVariable("orderCode") Long orderCode) {
        return orderService.finish(orderCode);
    }

    /**
     * 骑手 抢单配送
     * @param orderCode
     * @return
     */
    @PutMapping("/deliver/{orderCode}")
    public Result deliver(@PathVariable("orderCOde") Long orderCode) {
        return orderService.deliver(orderCode);
    }
}
