package com.mizore.mob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.mapper.DishMapper;
import com.mizore.mob.service.IDishService;
import com.mizore.mob.util.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    private DishMapper dishMapper;

    @Override
    public Result addDish(Dish dish) {
        int priority = getMaxPriority() + 1;
        dish.setPriority(priority);
        boolean res = save(dish);
        return res ? Result.ok() : Result.error("新增菜品失败！！");
    }

    public int getMaxPriority() {
        Integer max = dishMapper.getMaxPriority();
        return max != null ? max : 0;
    }

    @Override
    public Result changeState(Integer dishId, Byte state) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", dishId).set("state", state);
        boolean res = update(updateWrapper);
        return res ? Result.ok() : Result.error("状态更改失败！！");
    }

    @Override
    public Result searchDishesByName(String keyword) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword);
        List<Dish> list = list(queryWrapper);
        return list == null ? Result.error("菜品搜索失败！！") : Result.ok(list);
    }

    @Override
    public Result updateDish(Dish dish) {
        Integer id = dish.getId();
        Dish existingDish;
        if (id == null || (existingDish = getById(id)) == null) {
            return  Result.error("菜品不存在！！");
        }
        BeanUtil.copyNotNullProperties(dish, existingDish);
        updateById(existingDish);
        return Result.ok();
    }
}
