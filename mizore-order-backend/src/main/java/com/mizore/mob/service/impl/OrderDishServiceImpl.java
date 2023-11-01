package com.mizore.mob.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.entity.Order;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.entity.User;
import com.mizore.mob.mapper.DishMapper;
import com.mizore.mob.mapper.OrderDishMapper;
import com.mizore.mob.service.IOrderDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@Service
@AllArgsConstructor
public class OrderDishServiceImpl extends ServiceImpl<OrderDishMapper, OrderDish> implements IOrderDishService {


}
