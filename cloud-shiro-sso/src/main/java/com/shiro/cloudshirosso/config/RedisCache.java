package com.shiro.cloudshirosso.config;

import com.shiro.cloudshirosso.constant.RedisConstant;
import com.shiro.cloudshirosso.entity.Permissions;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.utils.RedisTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 在项目完全跑起来后执行
 */
@Slf4j
@Component
public class RedisCache implements ApplicationRunner {

    private UserInfoRepositories userInfoRepositories;

    @Autowired
    public void setUserInfoRepositories(UserInfoRepositories userInfoRepositories) {
        this.userInfoRepositories = userInfoRepositories;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("用户角色权限缓存开始...");
        List<UserInfo> userInfoList = userInfoRepositories.findAll();
        userInfoList.forEach(userInfo -> {
            Set<String> rolesSet = new HashSet<>();
            Set<String> perSet = new HashSet<>();
            List<Role> roles = userInfo.getRoles();
            roles.forEach(role -> {
                rolesSet.add(role.getRoleName());
                List<Permissions> permissionsList = role.getPermissions();
                permissionsList.forEach(permissions -> {
                    perSet.add(permissions.getPerStrName());
                });
            });
            if (rolesSet.size() > 0) {
                RedisTool.setKeyBySet(RedisConstant.USER_ROLE_PREFIX + userInfo.getId(), rolesSet);
            }
            if (perSet.size() > 0) {
                RedisTool.setKeyBySet(RedisConstant.USER_PER_PREFIX + userInfo.getId(), perSet);
            }
        });
        log.info("用户角色权限缓存结束...");
    }
}
