package com.shiro.cloudshirosso.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.shiro.cloudshirosso.constant.Constant;
import com.shiro.cloudshirosso.entity.UserInfo;
import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordUtil {


    public static void saltPassword(UserInfo userInfo) {
        String salt = randomSalt();
        userInfo.setSalt(salt);
        userInfo.setPassword(encryptionPassword(salt, userInfo.getPassword()));
    }

    public static String randomSalt() {
        return NanoIdUtils.randomNanoId();
    }

    public static String encryptionPassword(String salt, String password) {
        return new Md5Hash(password, salt, Constant.HASH_ITERATIONS).toHex();
    }
}
