package com.ztt.dao.clouddao.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class jasyptUtils {

    public static void jasypt() {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        //BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        // 自定义加密密钥
        encryptor.setPassword("");
        String uerName = encryptor.encrypt("");
        String password = encryptor.encrypt("");
        String host = encryptor.encrypt("");
        String url = encryptor.encrypt("");
        System.out.println("uerName = " + uerName);
        System.out.println("password = " + password);
        System.out.println("url = " + url);
        System.out.println("host = " + host);
    }
}
