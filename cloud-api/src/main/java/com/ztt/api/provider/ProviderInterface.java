package com.ztt.api.provider;

import com.ztt.entity.CommonUser;

import java.util.List;
import java.util.Map;

public interface ProviderInterface {
    /**
     * @return str
     */
    String list() throws Exception;

    /**
     * @return str
     */
    String hello() throws Exception;

    /**
     * 通过openfeign调用cloud-provider提供的服务
     *
     * @param name1 name
     * @return String
     */
    String hello2(String name1) throws Exception;

    /**
     * 传递对象到服务端
     *
     * @param user user
     * @param name name
     * @return String
     */
    Map<String, Object> hello3(CommonUser user, String name) throws Exception;

    /**
     * cc
     *
     * @param name     姓名
     * @param password 密码
     * @return string
     */
    Map<Object, Object> hello5(String name, String password) throws Exception;


    List<CommonUser> sysUserList(String loginName, String password) throws Exception;
}
