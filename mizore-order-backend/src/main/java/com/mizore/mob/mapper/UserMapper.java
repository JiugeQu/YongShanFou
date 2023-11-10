package com.mizore.mob.mapper;

import com.mizore.mob.entity.User;
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
 * @since 2023-10-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select name from user where id = #{id}")
    String selectNameById(@Param("id") Integer id);

}
