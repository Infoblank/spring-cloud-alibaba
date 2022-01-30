package com.sa.cloudsatoken.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    // 注入数据源查询当前用户的权限信息

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("开始授权,当前用户ID:{},登录的类型:{}",loginId,loginType);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user");
        permissions.add("admin");
        return permissions;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("开始授予角色,当前用户ID:{},登录的类型:{}",loginId,loginType);
        return null;
    }
}
