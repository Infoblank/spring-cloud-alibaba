package com.shiro.cloudshirosso.shiro;

import com.shiro.cloudshirosso.constant.RedisConstant;
import com.shiro.cloudshirosso.entity.Permissions;
import com.shiro.cloudshirosso.entity.Role;
import com.shiro.cloudshirosso.entity.UserInfo;
import com.shiro.cloudshirosso.jpa.repositories.UserInfoRepositories;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import com.shiro.cloudshirosso.utils.ApplicationContextUtils;
import com.shiro.cloudshirosso.utils.RedisTool;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

@Slf4j
public class JwtRealm extends AuthorizingRealm {


    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }


    //授权 改造到去redis里面查询权限,加速访问
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String principal = principals.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        fromRedisValidationPerAndRole(principal, authorizationInfo);
        return authorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getPrincipal();
        long start = System.currentTimeMillis();
        String jti = JWTUtil.getJTI(jwt);
        Object redisToken = RedisTool.get(RedisConstant.USER_JWT_PREFIX + jti);
        long end = System.currentTimeMillis();
        log.info("redis获取token耗时:{}", end - start);
        // 用redis来实现token提前失效的功能,用户退出后删除redis的jwt,就算jwt还有效也认为无效
        if (redisToken == null || !jwt.equals(redisToken.toString())) {
            throw new AuthenticationException("token过期了...");
        } else {
            JWTUtil.validationToken(jwt);
            // 直接注入对象会导致类的对象过大,此方式同时还能保证事物,注入会导致userInfoRepositories提前初始化无法保证事物
            return new SimpleAuthenticationInfo(jwt, jwt, this.getName());
        }
    }

    /**
     * 从redis里面获取用户的角色和权限
     *
     * @param token                   当前用户token
     * @param simpleAuthorizationInfo 授权信息
     */
    private void fromRedisValidationPerAndRole(String token, SimpleAuthorizationInfo simpleAuthorizationInfo) {
        // token的唯一Id
        String jti = JWTUtil.getJTI(token);
        String id = JWTUtil.getClaimFiledUserId(token);
        // redis查询权限和角色
        Set<Object> perValues = RedisTool.getValuesBySetKey(RedisConstant.USER_PER_PREFIX + id);
        Set<Object> roleValues = RedisTool.getValuesBySetKey(RedisConstant.USER_ROLE_PREFIX + id);
        if (perValues.size() > 0) {
            log.info("在Redis当中获取权限赋值..");
            perValues.forEach(v -> {
                // 权限字符串
                simpleAuthorizationInfo.addStringPermissions(perOrRole(v.toString()));
            });
        }
        if (roleValues.size() > 0) {
            log.info("在Redis当中获取角色赋值..");
            roleValues.forEach(v -> simpleAuthorizationInfo.addRoles(perOrRole(v.toString())));
        }
        // 当前用户没有权限角色信息就去数据库查询
        if (perValues.size() == 0 || roleValues.size() == 0) {
            log.info("从数据库查询权限和角色赋值给用户:{}", JWTUtil.getClaimFiledUserName(token));
            UserInfoRepositories userInfoRepositories = (UserInfoRepositories) ApplicationContextUtils.getBean("userInfoRepositories");
            String userId = JWTUtil.getClaimFiledUserId(token);
            Optional<UserInfo> byId = userInfoRepositories.findById(Long.valueOf(userId));
            if (byId.isPresent()) {
                UserInfo info = byId.get();
                // 获取当前对象的全部角色
                List<Role> infoRoles = info.getRoles();
                HashSet<String> perSet = new HashSet<>();
                HashSet<String> roleSet = new HashSet<>();
                infoRoles.forEach(v -> {
                    roleSet.add(v.getRoleName());
                    List<Permissions> list = v.getPermissions();
                    list.forEach(s -> perSet.add(s.getPerStrName()));
                });
                simpleAuthorizationInfo.addStringPermissions(perSet);
                simpleAuthorizationInfo.addRoles(roleSet);
                log.info("数据库查询到角色{}", Arrays.toString(roleSet.toArray()));
                log.info("数据库查询到权限{}", Arrays.toString(perSet.toArray()));
                Long aLongPer = RedisTool.setKeyBySet(RedisConstant.USER_PER_PREFIX + id, perSet);
                Long aLongRole = RedisTool.setKeyBySet(RedisConstant.USER_ROLE_PREFIX + id, roleSet);
                log.info("redis插入角色条数:{}", aLongRole);
                log.info("redis插入权限条数:{}", aLongPer);
            }
        }
    }

    /**
     * 对权限和角色字符串进行处理,权限字符串会被[]包含,先去掉,暂时还会发现是什么地方导致的
     *
     * @param str
     * @return
     */
    private Set<String> perOrRole(String str) {
        Set<String> strings = new HashSet<>();
        if (!StringUtil.isNullOrEmpty(str)) {
            str = str.replace("[", "").replace("]", "");
            String[] split = str.split(",");
            strings.addAll(Arrays.asList(split));
        }
        return strings;
    }
}
