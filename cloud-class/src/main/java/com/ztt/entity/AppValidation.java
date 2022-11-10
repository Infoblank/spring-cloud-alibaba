package com.ztt.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppValidation {

    @Max(Integer.MAX_VALUE)
    @Min(1)
    @NotNull(message = "id不能为空")
    private int id;
    @NotNull(message = "appId不能为空")
    @Length(max = 225)
    private String appId;
    @NotNull(message = "name不能为空")
    private String name;
    @Email(message = "邮箱不正确")
    private String email;
}
