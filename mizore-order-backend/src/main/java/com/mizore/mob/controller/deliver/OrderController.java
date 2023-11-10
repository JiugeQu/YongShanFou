package com.mizore.mob.controller.deliver;

import com.mizore.mob.annotation.Idempotent;
import com.mizore.mob.dto.OrderDishDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.exception.OrderException;
import com.mizore.mob.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@RestController("deliverOrderController")
@RequestMapping("/deliver/order")
@AllArgsConstructor
public class OrderController {
    private IOrderService orderService;


    /**
     * 获取指定状态的订单
     */
    @GetMapping("/state/{state}")
    public Result getOrdersByStates(@PathVariable("state") Byte state) {
        return orderService.getOrdersByState(state);
    }

    /**
     * 查询该骑手送过的历史订单
     */
    @GetMapping("/history")
    public Result getHistoryOrders() {
        return orderService.getHistoryOrdersOfDeliver();
    }


    /**
     * 骑手 抢单
     */
    @PutMapping("/deliver/{code}")
    public Result deliver(@PathVariable("code") Long orderCode) {
        return orderService.deliver(orderCode);
    }

    /**
     * 骑手确认送达
     */
    @PutMapping("/finish/{code}")
    public Result finish(@PathVariable("code") Long orderCode) {
        return orderService.arrived(orderCode);
    }

}
