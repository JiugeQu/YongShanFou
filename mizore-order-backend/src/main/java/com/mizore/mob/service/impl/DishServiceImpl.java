package com.mizore.mob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.mapper.DishMapper;
import com.mizore.mob.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Override
    public Result addDish(Dish dish) {
        boolean res = save(dish);
        return res ? Result.ok() : Result.error("新增菜品失败！！");
    }

    @Override
    public Result changeState(Integer id) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("state = (state % 2) + 1").eq("id", id);
        boolean res = update(updateWrapper);
        return res ? Result.ok() : Result.error("上架/下架失败！！");
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
        existingDish.setDescription(dish.getDescription());
        existingDish.setImage(dish.getImage());
        existingDish.setName(dish.getName());
        existingDish.setPrice(dish.getPrice());
        existingDish.setType(dish.getType());
        updateById(existingDish);
        return Result.ok();
    }
}
