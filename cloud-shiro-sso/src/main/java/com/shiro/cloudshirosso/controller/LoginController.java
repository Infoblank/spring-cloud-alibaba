package com.shiro.cloudshirosso.controller;

import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    UserInfoRepositories userInfoRepositories;

    @GetMapping("login")
    public String login() {
        HashMap<String, String> claim = new HashMap<>();
        UserInfo userInfo = userInfoRepositories.findUserInfoByUserNameAndPassword("zhangtt", "2FLy1yE9x_iRz8EOwa-we");
        if (!ObjectUtils.isEmpty(userInfo)) {
            claim.put("userId", userInfo.getId().toString());
            return JWTUtil.sign(claim);
        }
        return null;
    }
}
