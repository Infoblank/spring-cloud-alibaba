package com.ztt.consumer.cloudconsumer.interfaceprovider;

import com.ztt.common.entity.CommonUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author ZTT
 */
@FeignClient(value = "cloud-provider")
//@RequestMapping(path = "provider/v1")
public interface ProviderClient {

    /**
     * @return
     */
    @RequestMapping(path = "provider/v1/list")
    String list();

    /**
     * @return
     */
    @RequestMapping(path = "provider/v1/hello")
    String hello();

    /**
     * 通过openfeign调用cloud-provider提供的服务
     *
     * @param name1
     * @return String
     */
    @RequestMapping(path = "provider/v1/hello2/{name1}")
    String hello2(@PathVariable("name1") String name1);

    /**
     * 传递对象到服务端
     *
     * @param user user
     * @param name name
     * @return String
     */
    @RequestMapping(path = "provider/v1/hello3")
    String hello3(@RequestBody CommonUser user, @RequestParam("name") String name);

    /**
     * cc
     *
     * @param name     姓名
     * @param password 密码
     * @return string
     */
    @RequestMapping(path = "provider/v1/hello5/{name}/{password}")
    Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password);


    @RequestMapping(path = "provider/v1/sys")
    List<CommonUser> sysUserList(@RequestParam("loginName") String loginName, @RequestParam("password") String password);
}
