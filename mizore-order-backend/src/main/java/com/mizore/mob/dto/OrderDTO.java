package com.mizore.mob.dto;

import com.mizore.mob.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderDTO extends Order{

    // 信息详细的菜品项列表
    private List<OrderDishDTO> orderDishes;

    // 顾客身份信息
    private String customerName;

    private String customerPhone;

    // 配送员身份信息
    private String deliverName;

    private String deliverPhone;

    // 评价内容
    private String commentContent;


}
