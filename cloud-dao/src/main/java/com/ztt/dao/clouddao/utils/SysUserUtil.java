package com.ztt.dao.clouddao.utils;

import com.ztt.dao.clouddao.mp.domain.SysUser;
import com.ztt.entity.CommonUser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SysUserUtil {
    public static CommonUser sysUerToCommonUser(SysUser sysUser) {
        CommonUser commonUser = new CommonUser();
        commonUser.setId(sysUser.getId().toString());
        commonUser.setName(sysUser.getName());
        commonUser.setPassword(sysUser.getPassword());
        commonUser.setLoginState(sysUser.getLoginState().toString());
        commonUser.setLoginTime(changLocalDateTimeToString(sysUser.getLoginTime()));
        commonUser.setCreateTime(changLocalDateTimeToString(sysUser.getCreateTime()));
        commonUser.setModifyTime(changLocalDateTimeToString(sysUser.getModifyTime()));
        commonUser.setLoginName(sysUser.getLoginName());
        commonUser.setDeletedFlag(sysUser.getDeletedFlag().toString());
        return commonUser;
    }

    public static String changLocalDateTimeToString(java.time.LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static List<CommonUser> sysUerToCommonUserList(List<SysUser> sysUserList) {
        List<CommonUser> commonUserList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            commonUserList.add(sysUerToCommonUser(sysUser));
        }
        return commonUserList;
    }
}
