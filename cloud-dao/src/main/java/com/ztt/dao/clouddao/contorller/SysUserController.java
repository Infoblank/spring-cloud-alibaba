package com.ztt.dao.clouddao.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztt.common.entity.CommonUser;
import com.ztt.dao.clouddao.mp.domain.SysUser;
import com.ztt.dao.clouddao.mp.mapper.SysUserMapper;
import com.ztt.dao.clouddao.utils.SysUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sys")
public class SysUserController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @RequestMapping("user/list")
    public List<CommonUser> queryUserList(String loginName, String password) {
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_name", loginName);
        userWrapper.eq("password", password);
        return SysUserUtil.sysUerToCommonUserList(this.sysUserMapper.selectList(userWrapper));
    }
}
