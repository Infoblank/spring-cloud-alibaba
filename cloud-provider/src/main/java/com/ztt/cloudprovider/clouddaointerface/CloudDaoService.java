package com.ztt.cloudprovider.clouddaointerface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cloud-dao")
//@RequestMapping("/sys")
public interface CloudDaoService {

     @RequestMapping("/sys/user/list")
     String sysUserList(@RequestParam("loginName") String loginName, @RequestParam("password")String password);
}
