package com.shiro.cloudshirosso.controller;

import com.shiro.cloudshirosso.constant.RedisConstant;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import com.shiro.cloudshirosso.utils.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping
public class LoginController {

    private UserInfoRepositories userInfoRepositories;

    @Autowired
    public void setUserInfoRepositories(UserInfoRepositories userInfoRepositories) {
        this.userInfoRepositories = userInfoRepositories;
    }

    @GetMapping("login")
    public String login() {
        HashMap<String, String> claim = new HashMap<>();
        UserInfo userInfo = userInfoRepositories.findUserInfoByUserNameAndPassword("gJVpJW-C_x8PnBs2FqA22", "2FLy1yE9x_iRz8EOwa-we");
        if (!ObjectUtils.isEmpty(userInfo)) {
            claim.put("userId", userInfo.getId().toString());
            claim.put("userName", userInfo.getUserName());
            String sign = JWTUtil.sign(claim);
            String jti = JWTUtil.getJTI(sign);
            RedisTool.setKey(RedisConstant.USER_JWT_PREFIX + jti, sign);
            return sign;
        }
        return "登录失败!用户名或密码错误.";
    }
}
