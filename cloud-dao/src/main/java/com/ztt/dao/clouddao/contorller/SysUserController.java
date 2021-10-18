package com.ztt.dao.clouddao.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztt.dao.clouddao.mp.domain.SysUser;
import com.ztt.dao.clouddao.mp.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sys")
public class SysUserController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @GetMapping("user/list")
    public List<SysUser> queryUserList(){
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_name","sys_ztt");
        return this.sysUserMapper.selectList(userWrapper);
    }
}
