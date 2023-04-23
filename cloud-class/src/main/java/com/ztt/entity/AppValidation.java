package com.ztt.entity;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppValidation implements Serializable {

    @NotNull(message = "id不能为空")
    @Max(Integer.MAX_VALUE)
    @Min(1)
    private int id;
    @NotEmpty(message = "appId不能为空字符串")
    @NotNull(message = "appId不能为空")
    @Length(max = 225)
    private String appId;
    @NotNull(message = "name不能为空")
    private String name;
    @Email(message = "邮箱不正确")
    private String email;
}
