package com.shiro.cloudshirosso.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.shiro.cloudshirosso.constant.Constant;
import com.shiro.cloudshirosso.constant.RedisConstant;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import com.shiro.cloudshirosso.utils.RedisTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Service
public class MainService {

    @PersistenceContext
    private EntityManager entityManager;
    private UserInfoRepositories userInfoRepositories;

    @Autowired
    public void setUserInfoRepositories(UserInfoRepositories userInfoRepositories) {
        this.userInfoRepositories = userInfoRepositories;
    }

    public String login(UserInfo user, HttpServletRequest servletRequest) throws Exception {
        // 当请求头当中的token存在时,
        String jwt = servletRequest.getHeader(Constant.AUTHORIZATION);
        if (jwt != null) {
            boolean verify;
            try {
                verify = JWTUtil.verify(jwt);
                if (verify) {
                    return jwt;
                }
            } catch (TokenExpiredException e) {
                // token过期了,直接刷新token
                String tokenExpired = JWTUtil.refreshTokenExpired(jwt);
                String jti = JWTUtil.getJTI(tokenExpired);
                RedisTool.setKey(RedisConstant.USER_JWT_PREFIX + jti, tokenExpired);
                RedisTool.deleteKey(RedisConstant.USER_JWT_PREFIX + JWTUtil.getJTI(jti));
                return tokenExpired;
            } catch (Exception e) {
                throw new Exception("虚假token,意图蒙混过关...");
            }
        }
        HashMap<String, String> claim = new HashMap<>();
        UserInfo userInfo = userInfoRepositories.findUserInfoByUserNameAndPassword(user.getUserName(), user.getPassword());
        if (!ObjectUtils.isEmpty(userInfo)) {
            claim.put("userId", userInfo.getId().toString());
            claim.put("userName", userInfo.getUserName());
            String sign = JWTUtil.sign(claim);
            String jti = JWTUtil.getJTI(sign);
            RedisTool.setKey(RedisConstant.USER_JWT_PREFIX + jti, sign);
            return sign;
        }
        return "登录失败!用户名或密码错误.";
    }


    @Modifying
    @Transactional
    public int test(String sql, Object... o) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        return nativeQuery.executeUpdate();
    }
}
