package com.shiro.cloudshirosso.constant;

public interface JwtConstant {

    // JWT相关的key

    String ALGORITHM = "alg";
    String CONTENT_TYPE = "cty";
    String TYPE = "typ";
    String KEY_ID = "kid";

    //Payload
    /**
     * jwt签发者
     */
    String ISSUER = "iss";
    /**
     * jwt 面向的用户
     */
    String SUBJECT = "sub";
    /**
     * 过期时间
     */
    String EXPIRES_AT = "exp";
    /**
     * 定义在什么时间之前，该jwt都是不可用的
     */
    String NOT_BEFORE = "nbf";
    /**
     * 签发时间
     */
    String ISSUED_AT = "iat";
    /**
     * wt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     */
    String JWT_ID = "jti";
    /**
     * 接收jwt的一方
     */
    String AUDIENCE = "aud";

    String JWT_USER = "user";

    String JWT_SYSTEM = "system";

    String JWT_PC = "pc";

    int JWT_MINUTE = 30;
}
