package com.sa.cloudsatoken.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class StpInterfaceImpl implements StpInterface {

    // 注入数据源查询当前用户的权限信息

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user");
        return permissions;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
