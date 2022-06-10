package com.shiro.cloudshirosso.shiro.config;

import com.shiro.cloudshirosso.shiro.JwtDefaultSubjectFactory;
import com.shiro.cloudshirosso.shiro.JwtRealm;
import com.shiro.cloudshirosso.shiro.filter.JwtFilter;
import com.shiro.cloudshirosso.shiro.matcher.CustomHashedCredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    /*
     * a. 告诉shiro不要使用默认的DefaultSubject创建对象，因为不能创建Session
     * */
    @Bean
    public SubjectFactory subjectFactory() {
        return new JwtDefaultSubjectFactory();
    }

    @Bean
    public Realm realm() {
        JwtRealm jwtRealm = new JwtRealm();
        HashedCredentialsMatcher credentialsMatcher = new CustomHashedCredentialsMatcher();
        jwtRealm.setCachingEnabled(false);
        jwtRealm.setCredentialsMatcher(credentialsMatcher);
        return jwtRealm;
    }

    /**
     * Bean的方法的参数,spring容器会直接在容器中拿对应的bean来注入
     *
     * @param realm          认证
     * @param subjectFactory session
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(Realm realm, SubjectFactory subjectFactory) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(evaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        securityManager.setSubjectFactory(subjectFactory);
        return securityManager;
    }

    /**
     * 当前bean:ShiroFilterFactoryBean实现现BeanPostProcessor接口,但是它有依赖理别的bean,所以它说依赖的bean会在它初始化前初始化
     * 所以不会走postProcessBeforeInitialization,postProcessAfterInitialization
     *
     * @param securityManager 安全管理器
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/unauthenticated");
        factoryBean.setUnauthorizedUrl("/unauthorized");
        // 过滤器
        HashMap<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);

        // 拦截器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        filterRuleMap.put("/login", "anon");
        //filterRuleMap.put("/sso/addUserInfo", "anon");
        filterRuleMap.put("/logout", "logout");
        filterRuleMap.put("/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /*
     *  启用代理会导致一次操作走两次授权,关闭代理
     *  @return
     *
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }*/

    /*@Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return  new LifecycleBeanPostProcessor();
    }*/

    /**
     * 注解支持,不然shiro的相关注解无法识别,RequiresPermissions等等
     *
     * @param securityManager 安全管理器
     * @return sourceAdvisor
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

}
