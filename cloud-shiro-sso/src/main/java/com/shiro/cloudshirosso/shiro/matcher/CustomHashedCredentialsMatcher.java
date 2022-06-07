package com.shiro.cloudshirosso.shiro.matcher;

import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

@Slf4j
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 当前用户的token
        String credentials = (String) token.getCredentials();
        String userId = JWTUtil.getClaimFiledUserId(credentials);
        log.info("{},的userId:{}" + this.getClass().getSimpleName(), userId);
        return true;
    }
}
