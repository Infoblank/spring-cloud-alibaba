package com.sa.cloudsatoken.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ValidEntity {
    private int id;
    @NotBlank
    private String appId;
    @NotBlank
    private String name;
    @Email
    private String email;
}
