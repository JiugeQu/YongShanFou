package com.mizore.mob.controller.staff;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.service.IDishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mizore.mob.util.Constant.DISH_ON_SALE;
import static com.mizore.mob.util.Constant.MAX_COUNT_DISH_PER_PAGE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@RestController("staffDishController")
@RequestMapping("/staff/dish")
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
        return dishService.addDish(dish);
    }

    /**
     * 更改指定菜品的状态
     */
    @PutMapping("/{dishId}/state/{state}")
    public Result changeState(@PathVariable("dishId")Integer dishId,
                              @PathVariable("state")Byte state) {
        return dishService.changeState(dishId, state);
    }

    /**
     * 更改指定菜品信息
     */
    @PutMapping
    public Result update(@RequestBody Dish dish) {
        return dishService.updateDish(dish);
    }
    /**
     * 查看所有菜品
     */
    @GetMapping("all")
    public Result queryAllDish() {
        List<Dish> res;
        return (res = dishService.query().list()) == null
                ? Result.error("查看菜品失败！！")
                : Result.ok(res);
    }
/*    @GetMapping("all")
    public Result queryAllDish(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        List<Dish> res;
        return (res = dishService.query().page(new Page<>(current, MAX_COUNT_DISH_PER_PAGE)).getRecords())
                == null
                ? Result.error("查看菜品失败！！")
                : Result.ok(res);
    }*/

    /**
     * 获取所有未下架的菜名及其id、priority、类型
     */
    @GetMapping("names")
    public Result queryDishNames() {
        List<Dish> res;
        return (res = dishService.query().select("id", "name", "priority", "type").list()) == null
                ? Result.error("查看菜品失败！！")
                : Result.ok(res);
    }


    /**
     * 根据菜名模糊搜索菜品
     */
    @GetMapping("{keyword}")
    public Result searchDishesByName(@PathVariable("keyword") String keyword) {
        return dishService.searchDishesByName(keyword);
    }

    /**
     * 根据id删除指定菜品
     */
    @DeleteMapping("{id}")
    public Result deleteDish(@PathVariable("id") Integer id) {
        boolean res = dishService.removeById(id);
        return res ? Result.ok() : Result.error("删除菜品失败！！");
    }
}
