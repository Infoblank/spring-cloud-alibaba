package com.shiro.cloudshirosso.shiro;

import com.auth0.jwt.interfaces.Claim;
import com.shiro.cloudshirosso.entity.Permissions;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import com.shiro.cloudshirosso.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JwtRealm extends AuthorizingRealm {


    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String principal = principals.getPrimaryPrincipal().toString();
        // 获取token当中信息,主要是userId.
        Map<String, Claim> claimMap = JWTUtil.getClaimFiled(principal);
        UserInfoRepositories userInfoRepositories = (UserInfoRepositories) ApplicationContextUtils.getBean("userInfoRepositories");
        Claim userId = claimMap.get("userId");
        Optional<UserInfo> byId = userInfoRepositories.findById(Long.valueOf(userId.asString()));
        UserInfo info = byId.get();
        // 获取当前对象的全部角色
        List<Role> infoRoles = info.getRoles();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        HashSet<String> hashSet = new HashSet<>();
        infoRoles.forEach(v -> {
            authorizationInfo.addRole(v.getRoleName());
            List<Permissions> list = v.getPermissions();
            list.forEach(s -> hashSet.add(s.getPerStrName()));
        });
        authorizationInfo.addStringPermissions(hashSet);
        log.info("授权.......,{}", principal);
        return authorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("jwt不能为空");
        }
        if (!JWTUtil.verify(jwt)) {
            throw new UnknownAccountException();
        }
        Map<String, Claim> claimMap = JWTUtil.getClaimFiled(jwt);
        // 直接注入对象会导致类的对象过大,此方式同时还能保证事物,注入会导致userInfoRepositories提前初始化无法保证事物
        UserInfoRepositories userInfoRepositories = (UserInfoRepositories) ApplicationContextUtils.getBean("userInfoRepositories");
        Optional<UserInfo> byId = userInfoRepositories.findById(Long.parseLong(claimMap.get("userId").asString()));
        if (byId.isPresent()) {
            UserInfo info = byId.get();
            log.info("username={}", claimMap);
            log.info("info={}", info);
            return new SimpleAuthenticationInfo(jwt, jwt, this.getName());
        }
        return null;
    }
}
