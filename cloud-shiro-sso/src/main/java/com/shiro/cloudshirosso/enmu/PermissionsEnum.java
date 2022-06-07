package com.shiro.cloudshirosso.enmu;

import lombok.Getter;

@Getter
public enum PermissionsEnum {

    BUTTON("20002", "按钮"),

    URL("20003", "路径"),
    MENU("20001", "菜单");

    private final String code;

    private final String desc;

    PermissionsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
