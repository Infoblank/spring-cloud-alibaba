package com.shiro.cloudshirosso.jpa.audit;

import com.shiro.cloudshirosso.constant.Constant;
import com.shiro.cloudshirosso.shiro.utils.JWTUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 在表发生改变时修改创建人和修改人的id
 * 只是jpa的@CreatedBy和@LastModifiedBy两个注解的返回数据,会在执行插入的时候自动的写入这两个值
 */
@Configuration
@Slf4j
public class EntityAuditorAware implements AuditorAware<Long> {
    /**
     * 获取当前的请求对象,拿到token去获取其中存放的用户id
     */
    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        // 获取token,拿到里面的userid
        String authorization = request.getHeader(Constant.AUTHORIZATION);
        String userId = JWTUtil.getClaimFiledUserId(authorization);
        log.info("当前修改人token:{},用户Id:{}", authorization, userId);
        // 获取到当前的用户id,返回当前用户的id
        return Optional.of(Long.parseLong(userId));
    }
}
