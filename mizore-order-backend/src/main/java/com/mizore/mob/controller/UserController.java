package com.mizore.mob.controller;

import com.mizore.mob.dto.LoginFromDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.dto.SignFromDTO;
import com.mizore.mob.service.IUserService;
import com.mizore.mob.util.UserHolder;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@Slf4j
@RequestMapping("/user")
@RestController
@AllArgsConstructor
@Validated
public class UserController {

    private IUserService userService;
    @PostMapping("/code")
    public Result sendCode(@Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
                               @RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }

    /**
     * 用户注册
     * @param signFromDTO 注册时的信息包装，包括姓名，验证码，手机号，身份类别
     */
    @PostMapping("/sign")
    public Result sign(@Valid @RequestBody SignFromDTO signFromDTO) {
//        log.info("do sign");
        return userService.sign(signFromDTO);
    }


    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginFromDTO loginFromDTO) {
        return userService.login(loginFromDTO);
    }

    @GetMapping("/me")
    public Result me() {
        return Result.ok(UserHolder.get());
    }

}
