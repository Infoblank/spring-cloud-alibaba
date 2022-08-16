package com.ztt.consumer.cloudconsumer.interfaceprovider;

import com.ztt.common.entity.CommonUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ZTT, 当前版本不在支持RequestMapping, 直接使用FeignClient的path属性
 * value == name 服务的名称,url -- 直接填写地址
 * decode404 -- 404是被解码还是抛出异常
 * <p>
 * configuration()指明FeignClient的配置类，
 * 默认的配置类为FeignClientsConfiguration类，在缺省的情况下，这个类注入了默认的Decoder、Encoder和Contract等配置的Bean
 * <p>
 * fallback()为配置熔断器的处理类
 */
@FeignClient(value = "cloud-provider", path = "/provider")
public interface ProviderClient {

    /**
     * @return str
     */
    @RequestMapping(path = "/v1/list")
    String list();

    /**
     * @return  str
     */
    @RequestMapping(path = "/v1/hello")
    String hello();

    /**
     * 通过openfeign调用cloud-provider提供的服务
     *
     * @param name1 name
     * @return String
     */
    @RequestMapping(path = "/v1/hello2/{name1}")
    String hello2(@PathVariable("name1") String name1);

    /**
     * 传递对象到服务端
     *
     * @param user user
     * @param name name
     * @return String
     */
    @PostMapping(path = "/v1/hello3")
    Map<String,Object> hello3(@RequestBody CommonUser user, @RequestParam("name") String name);

    /**
     * cc
     *
     * @param name     姓名
     * @param password 密码
     * @return string
     */
    @RequestMapping(path = "/v1/hello5/{name}/{password}")
    Map<Object, Object> hello5(@PathVariable("name") String name, @PathVariable("password") String password);


    @RequestMapping(path = "/v1/sys")
    List<CommonUser> sysUserList(@RequestParam("loginName") String loginName, @RequestParam("password") String password);
}
