package com.shiro.cloudshirosso.shiro.filter;

import com.shiro.cloudshirosso.constant.Constant;
import com.shiro.cloudshirosso.shiro.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
 * isAccessAllowed()判断是否携带了有效的JwtToken
 * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {
    /**
     * 返回true,shiro直接允许访问url
     * 返回false,走onAccessDenied来判断是否允许访问
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /**
     * 返回true表明登录通过
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = httpServletRequest.getHeader(Constant.AUTHORIZATION);
        log.info("请求的 Header 中藏有 jwtToken {}", jwt);
        JwtToken jwtToken = new JwtToken(jwt);
        if (jwt == null) {
            requestNoContainJwt(response);
            return false;
        }
        try {
            // 委托 realm 进行登录认证
            //所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(httpServletRequest, response).login(jwtToken);
            //也就是subject.login(token)
        } catch (AuthenticationException e) {
            onLoginFail(response,e);
            return false;
        }
        return true;
    }

    //登录失败时默认返回 401 状态码
    private void onLoginFail(ServletResponse response,Exception e) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write(e.getMessage());
    }

    private void requestNoContainJwt(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 要求用户的身份认证的状态码:401
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        httpResponse.getWriter().write("请求头不含jwt,请登录");
    }
}
