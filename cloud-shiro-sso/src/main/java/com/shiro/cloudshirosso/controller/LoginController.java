package com.shiro.cloudshirosso.controller;

import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class LoginController {

    private MainService mainService;

    @Autowired
    public void setMainService(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("login")
    public String login(UserInfo userInfo, HttpServletRequest servletRequest) throws Exception {
        return mainService.login(userInfo, servletRequest);
    }
}
