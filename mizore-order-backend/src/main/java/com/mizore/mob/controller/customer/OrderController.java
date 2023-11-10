package com.mizore.mob.controller.customer;

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
@RestController("customerOrderController")
@RequestMapping("/customer/order")
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
            ) {
        return orderService.createOrder(orderDishes, address, note, expectArriveTime);
    }

    /**
     * 获取未送达的订单，包括状态1、2、3、4
     * @return
     */
    @GetMapping("/before/arrive")
    public Result getOrdersBeforeArrive() {
        return orderService.getOrdersByStates(new Byte[]{1,2,3,4});
    }

    /**
     * 获取某一状态的订单
     * @return
     */
    @GetMapping("/state/{state}")
    public Result getOrdersByStates(@PathVariable("state") Byte state) {
        return orderService.getOrdersByState(state);
    }

    @GetMapping("/{code}")
    public Result getOrderByCode(@PathVariable("code") Long orderCode) {
        return orderService.getOrderByCode(orderCode);
    }

}
