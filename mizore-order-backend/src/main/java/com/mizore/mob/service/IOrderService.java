package com.mizore.mob.service;

import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.exception.OrderException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
public interface IOrderService extends IService<Order> {

    Result createOrder(List<OrderDish> orderDishes, String address, String note, LocalDateTime expectArriveTime) throws OrderException;

    Result confirm(Long orderCode);

    Result finish(Long orderCode);

    Result deliver(Long orderCode);
}
