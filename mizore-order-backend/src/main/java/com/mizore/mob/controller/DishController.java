package com.mizore.mob.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.entity.User;
import com.mizore.mob.service.IDishService;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.mizore.mob.util.Constant.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@RestController
@RequestMapping("/dish")
@AllArgsConstructor
public class DishController {

    private IDishService dishService;

    /**
     * 添加菜品
     * @param dish
     * @return
     */
    @PostMapping
    public Result addDish(@RequestBody Dish dish) {
        // TODO 将身份权限校验放在拦截器
/*        User user = UserHolder.get();
        if (user != null && ROLE_STAFF.equals(user.getRole())) {
            return dishService.addDish(dish);
        }
        return Result.error("权限不足！！");*/
        return dishService.addDish(dish);
    }

    /**
     * 更改指定菜品的上下架状态
     * @param id
     * @return
     */
    @PutMapping("/state/{id}")
    public Result changeState(@PathVariable("id")Integer id) {
        return dishService.changeState(id);
    }

    /**
     * 更改指定菜品信息
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Dish dish) {
//        boolean res = dishService.updateById(dish);
//        return res ? Result.ok() : Result.error("菜品修改失败！！");
        return dishService.updateDish(dish);
    }
    /**
     * 查看所有菜品
     * @param current
     * @return
     */
    @GetMapping("all")
    public Result queryAllDish(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        List<Dish> res;
        return (res = dishService.query().page(new Page<>(current, MAX_COUNT_DISH_PER_PAGE)).getRecords())
                == null
                ? Result.error("查看菜品失败！！")
                : Result.ok(res);
    }

    /**
     * 查看所有上架菜品
     * @param current
     * @return
     */
    @GetMapping("on-sale")
    public Result queryDishOnSale(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        List<Dish> res;
        return (res = dishService.query().eq("state", DISH_ON_SALE).page(new Page<>(current, MAX_COUNT_DISH_PER_PAGE)).getRecords())
                == null
                ? Result.error("查看菜品失败！！")
                : Result.ok(res);
    }

    /**
     * 根据菜名模糊搜索菜品
     * @param keyword
     * @return
     */
    @GetMapping("{keyword}")
    public Result searchDishesByName(@PathVariable("keyword") String keyword) {
        return dishService.searchDishesByName(keyword);
    }

    /**
     * 删除指定菜品
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Result deleteDish(@PathVariable("id") Integer id) {
        boolean res = dishService.removeById(id);
        return res ? Result.ok() : Result.error("删除菜品失败！！");
    }
}
