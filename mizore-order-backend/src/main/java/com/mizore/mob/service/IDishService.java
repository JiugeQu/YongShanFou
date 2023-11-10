package com.mizore.mob.service;

import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
public interface IDishService extends IService<Dish> {

    Result addDish(Dish dish);

    Result changeState(Integer id, Byte state);

    Result searchDishesByName(String keyword);

    Result updateDish(Dish dish);
}
