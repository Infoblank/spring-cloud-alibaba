package com.ztt.dao.clouddao.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztt.dao.clouddao.mp.domain.SysUser;
import com.ztt.dao.clouddao.mp.service.SysUserService;
import com.ztt.dao.clouddao.utils.SysUserUtil;
import com.ztt.entity.CommonUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sys")
@Slf4j
public class SysUserController {

    private SysUserService sysUserService;


    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @RequestMapping(path = "user/list", name = "getSysUserList")
    public List<CommonUser> queryUserList(String loginName, String password) {
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_name", loginName);
        userWrapper.eq("password", password);
        log.info("login_name={},password={}", loginName, password);
        List<SysUser> list = this.sysUserService.list(userWrapper);
        return SysUserUtil.sysUerToCommonUserList(list);
    }

    @RequestMapping("list")
    @Cacheable(value = "systemEnvironment")
    public List<CommonUser> getUserList() {
        return SysUserUtil.sysUerToCommonUserList(this.sysUserService.list());
    }
}
