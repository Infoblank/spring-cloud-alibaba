package com.shiro.cloudshirosso.controller;

import com.shiro.cloudshirosso.enmu.PermissionsEnum;
import com.shiro.cloudshirosso.entity.Permissions;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.jpa.repositories.RoleRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleRepositories roleRepositories;

    @GetMapping("addRole")
    public String add(){
        Role role = new Role();
        role.setRoleName("admin");
        Permissions permissions = new Permissions();
        permissions.setPerUrl("/v5/menu/index");
        permissions.setPerType(PermissionsEnum.MENU);
        permissions.setPerName("新增索引按钮");
        ArrayList<Permissions> arrayList = new ArrayList<>();
        arrayList.add(permissions);
        role.setPermissions(arrayList);
        this.roleRepositories.save(role);
        return role.toString();
    }
}
