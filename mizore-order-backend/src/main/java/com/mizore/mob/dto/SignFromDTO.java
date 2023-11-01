package com.mizore.mob.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignFromDTO {

    @NotBlank(message = "姓名不能为空！！")
    private String name;

    @NotBlank(message = "验证码不能为空！！")
    private String code;


    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
    message = "请正确填写手机号！！")
    private String phone;

    /**
     * 1：顾客，2：配送员。3：餐厅员工
     */
    @NotNull(message = "角色不能为空！！")
    private Byte role;
}
