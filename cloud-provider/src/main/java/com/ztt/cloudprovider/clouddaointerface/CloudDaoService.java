package com.ztt.cloudprovider.clouddaointerface;

import com.ztt.common.entity.CommonUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cloud-dao", path = "/sys/user")
public interface CloudDaoService {

    @RequestMapping("/list")
    List<CommonUser> sysUserList(@RequestParam("loginName") String loginName, @RequestParam("password") String password);
}
