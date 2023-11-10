package com.mizore.mob.controller.staff;

import com.mizore.mob.dto.Result;
import com.mizore.mob.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@RestController("staffOrderController")
@RequestMapping("/staff/order")
@AllArgsConstructor
public class OrderController {
    private IOrderService orderService;



    /**
     * 获取某状态的全部订单
     * @return
     */
    @GetMapping("/state/{state}")
    public Result getOrdersByStates(@PathVariable("state") Byte state) {
        return orderService.getOrdersByState(state);
    }

    /**
     * 获取进行中的订单，即已接单并未送达的订单，对应状态为2,3,4
     * @return
     */
    @GetMapping("/in/progress")
    public Result getOrdersInProgress() {
        return orderService.getOrdersByStates(new Byte[]{2,3,4});
    }

    /**
     * 获取已送达的订单，对应状态为5,6
     * @return
     */
    @GetMapping("/arrived")
    public Result getOrdersArrived() {
        return orderService.getOrdersByStates(new Byte[]{5,6});
    }
    /**
     * 商家对 待确认的顾客订单 进行接单操作
     * @param orderCode
     * @return
     */
    @PutMapping("confirm/{code}")
    public Result confirm(@PathVariable("code") Long orderCode) {
        return orderService.confirm(orderCode);
    }

    /**
     * 商家对 备餐完成的订单 进行接受配送操作
     * @param orderCode
     * @return
     */
    @PutMapping("/finish/{code}")
    public Result finish(@PathVariable("code") Long orderCode) {
        return orderService.finish(orderCode);
    }


    /**
     * 获取指定订单号的订单详情
     * @param orderCode 订单号
     */
    @GetMapping("/{code}")
    public Result getOrderByCode(@PathVariable("code") Long orderCode) {
        return orderService.getOrderByCode(orderCode);
    }

}
