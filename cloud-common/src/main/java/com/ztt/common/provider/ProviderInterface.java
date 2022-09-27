package com.ztt.common.provider;

import com.ztt.common.entity.CommonUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface ProviderInterface {
    /**
     * @return str
     */
    String list();

    /**
     * @return str
     */
    String hello();

    /**
     * 通过openfeign调用cloud-provider提供的服务
     *
     * @param name1 name
     * @return String
     */
    String hello2(@PathVariable("name1") String name1);

    /**
     * 传递对象到服务端
     *
     * @param user user
     * @param name name
     * @return String
     */
    Map<String, Object> hello3(@RequestBody CommonUser user, @RequestParam("name") String name);

    /**
     * cc
     *
     * @param name     姓名
     * @param password 密码
     * @return string
     */
    Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password);


    List<CommonUser> sysUserList(@RequestParam("loginName") String loginName, @RequestParam("password") String password);
}
