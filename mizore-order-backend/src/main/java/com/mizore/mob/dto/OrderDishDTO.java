package com.mizore.mob.dto;

import com.mizore.mob.entity.OrderDish;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderDishDTO extends OrderDish {

    private String dishName;

    private String dishImg;

    private BigDecimal dishPrice;




}
