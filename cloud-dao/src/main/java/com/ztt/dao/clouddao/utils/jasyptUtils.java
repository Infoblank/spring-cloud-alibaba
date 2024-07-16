package com.ztt.dao.clouddao.utils;

import com.ztt.dao.clouddao.mp.domain.SysUser;
import org.jasypt.util.text.BasicTextEncryptor;

import java.lang.reflect.Field;
import java.util.Objects;

public class jasyptUtils {

    public static void jasypt() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        //BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        // 自定义加密密钥
        encryptor.setPassword("TZmHTVEp3GNH7e1wkFeWaw==");
        String uerName = encryptor.encrypt("106.55.94.123:4001");
        System.out.println("uerName = " + uerName);
        String password = encryptor.encrypt("");
        String host = encryptor.encrypt("");
        String url = encryptor.decrypt("GCM4MEbXby9utOBwkUal/OeH04ImsUPmAxnVIeRmpgbbqCSXWtOaJcOCHJqxHCQWpKr1hZaayNZzgw6X0bUw7g==");

        System.out.println("url = " + url);
    }

    public static void main(String[] args) {
        jasypt();
       // reflect();
    }

    public static void reflect() {
        SysUser sysUser = new SysUser();
        Field[] fields = sysUser.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                System.out.println(sysUser.getName());
                try {
                    field.setAccessible(true);
                    Object o = field.get(sysUser);
                    if (Objects.equals(o,"") || Objects.isNull(o)){
                        field.set(sysUser,"ccccc");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        System.out.println("sysUser = " + sysUser);
    }
}
