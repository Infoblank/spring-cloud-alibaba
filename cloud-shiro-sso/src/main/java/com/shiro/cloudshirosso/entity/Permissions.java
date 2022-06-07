package com.shiro.cloudshirosso.entity;

import com.shiro.cloudshirosso.enmu.PermissionsEnum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "sys_permissions")
@Entity
@Data
public class Permissions extends BaseEntity {
    /**
     * 权限名称
     */
    private String perName;

    /**
     * 权限类型
     */
    private PermissionsEnum perType;

    /**
     *
     */
    private String perUrl;


    /**
     * 权限字符串,例如user:*:*
     */
    private String perStrName;
}
