package com.sa.cloudsatoken.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ValidEntity {
    private int id;

    @NotBlank(message = "ValidEntity的appId不能为空")
    private String appId;

    @NotBlank(message = "ValidEntity的name不能为空")
    private String name;

    @Email(message = "ValidEntity的email不是一个邮箱地址")
    private String email;
}
