package com.mizore.mob.mapper;

import com.mizore.mob.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    @Select("SELECT user_id FROM `order` WHERE code = #{code}")
    Integer selectUserIdByCode(@Param("code") Long code);

    List<Map<Long, Object>> testIn(List<Byte> states);

    @Select("SELECT total_price FROM `order` WHERE code = #{code}")
    BigDecimal selectTotalPriceByCode(@Param("code") Long code);
}
