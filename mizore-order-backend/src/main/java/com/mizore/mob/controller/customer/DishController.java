package com.mizore.mob.controller.customer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.service.IDishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RestController("customerDishController")
@RequestMapping("/customer/dish")
@AllArgsConstructor
public class DishController {

    private IDishService dishService;


    /**
     * 顾客查看所有菜品
     * 实际是查看所有上架和售罄的菜品，不包括下架的
     */
    @GetMapping("all")
    public Result queryAllDish() {
        List<Dish> res;
        res = dishService.query()
                .select("id", "image", "description", "name" ,"state", "sale", "price", "type", "priority")
                .in("state", Arrays.asList(1, 3))
                .list();
        return res == null ? Result.error("查看菜品失败！！") : Result.ok(res);
    }


}
