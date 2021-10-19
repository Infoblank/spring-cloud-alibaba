package com.ztt.dao.clouddao.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztt.dao.clouddao.mp.domain.SysUser;
import com.ztt.dao.clouddao.mp.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys")
public class SysUserController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @RequestMapping("user/list")
    public String queryUserList(){
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_name","sys_ztt");
        return this.sysUserMapper.selectList(userWrapper).toString();
    }
}
