package com.shiro.cloudshirosso.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.RoleRepositories;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.service.MainService;
import com.shiro.cloudshirosso.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "sso", name = "sso路径")
@Slf4j
public class MainController {
    @Resource
    private UserInfoRepositories userInfoRepositories;

    @PersistenceContext
    transient EntityManager entityManager;
    @Resource
    private RoleRepositories roleRepositories;

    @Resource
    private ObjectMapper objectMapper;

    @Resource(name = "mainService", type = MainService.class)
    private MainService mainService;


    @GetMapping("hello")
    public String hello() throws JsonProcessingException {
        //List<UserInfo> info = this.userInfoRepositories.findAll();
        ArrayList<UserInfo> objects = new ArrayList<>();
        int i = 1000000;
        UserInfo userInfo;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i1 = 0; i1 < i; i1++) {
            entityManager.createNativeQuery("").executeUpdate();
        }
        userInfoRepositories.saveAll(objects);
        stopWatch.stop();
        //log.info("info={}", info);
        log.info("hello sso");
        return objectMapper.writeValueAsString(stopWatch.getTotalTimeMillis());
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
        boolean present = byId.isPresent();
        if (present) {
            UserInfo info = byId.get();
            info.setUserName(NanoIdUtils.randomNanoId());
            this.userInfoRepositories.save(info);
            return byId.get().toString();
        }
        return null;
    }


    @GetMapping("delete")
    @RequiresPermissions("user:delete")
    public String delete() {
        this.userInfoRepositories.deleteById(1L);
        return "success";
    }

    @GetMapping("testJPA")
    public String testJPA() {
        for (int i = 1000; i < 10000000; i++) {
            String sql = "INSERT INTO SHIRO_SSO.SYS_ROLE (ID, CREATE_TIME, FOUNDER, LAST_FOUNDER, MODIFY_TIME, VERSION, ROLE_NAME)VALUES (" + i + 3 + ",NOW(),1,1,NOW(),1," + "'ADMIN'" + ")";
            mainService.test(sql);
        }
        return "success";
    }
}
