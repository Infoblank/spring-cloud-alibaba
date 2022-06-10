package com.shiro.cloudshirosso.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.RoleRepositories;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "sso", name = "sso路径")
@Slf4j
public class MainController {
    @Autowired
    private UserInfoRepositories userInfoRepositories;

    @Autowired
    private RoleRepositories roleRepositories;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("hello")
    public String hello() throws JsonProcessingException {
        List<UserInfo> info = this.userInfoRepositories.findAll();
        log.info("info={}", info);
        log.info("hello sso");
        return objectMapper.writeValueAsString(info.toString());
    }

    @GetMapping("addUserInfo")
    public String addUserInfo() {
        UserInfo info = new UserInfo();
        info.setPassword(NanoIdUtils.randomNanoId());
        info.setName(NanoIdUtils.randomNanoId());
        info.setUserName(NanoIdUtils.randomNanoId());
        info.setPhone(NanoIdUtils.randomNanoId());
        info.setEmail(NanoIdUtils.randomNanoId());
        PasswordUtil.saltPassword(info);
        ArrayList<Role> arrayList = new ArrayList<>();
        Optional<Role> byId = roleRepositories.findById(1L);
        boolean present = byId.isPresent();
        if (present) {
            arrayList.add(byId.get());
        }
        info.setRoles(arrayList);
        userInfoRepositories.save(info);
        log.info("info={}", info.getId());
        return info.toString();
    }

    @RequiresPermissions("user:modify")
    @GetMapping("modifyUser")
    public String modify() {
        Optional<UserInfo> byId = this.userInfoRepositories.findById(3L);
        UserInfo info = byId.get();
        info.setUserName(NanoIdUtils.randomNanoId());
        this.userInfoRepositories.save(info);
        return byId.get().toString();
    }


    @GetMapping("delete")
    public String delete() {
        this.userInfoRepositories.deleteById(1L);
        return "success";
    }
}
