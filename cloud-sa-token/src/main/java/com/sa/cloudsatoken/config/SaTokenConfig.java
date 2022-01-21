package com.sa.cloudsatoken.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStyle;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class SaTokenConfig implements WebMvcConfigurer {
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStyle();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            // 登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            // 不需要加入配置文件里面定义的上下文路径：/cloud-rest
            SaRouter.match("/**", "/validation/validEntity/valid/json", r -> StpUtil.checkLogin()).notMatch("/**/*.js").notMatch("/**/*.css");
            // 角色认证 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
            // 权限认证 -- 不同模块认证不同权限
            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
            SaRouter.match("/**", r -> StpUtil.checkPermission("admin")).stop();
            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
            // 甚至你可以随意的写一个打印语句
            SaRouter.match("/**", r -> log.info("路由鉴权完毕..."));
            // 连缀写法
            SaRouter.match("/**").check(r -> log.info("链式路由鉴权...."));
        })).addPathPatterns("/**");
    }
}
