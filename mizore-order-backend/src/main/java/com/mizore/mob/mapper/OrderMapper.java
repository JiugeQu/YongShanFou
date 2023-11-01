package com.mizore.mob.mapper;

import com.mizore.mob.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
