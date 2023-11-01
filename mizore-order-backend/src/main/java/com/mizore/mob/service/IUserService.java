package com.mizore.mob.service;

import com.mizore.mob.dto.LoginFromDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.dto.SignFromDTO;
import com.mizore.mob.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone);

    Result sign(SignFromDTO signFromDTO);

    Result login(LoginFromDTO loginFromDTO);
}
