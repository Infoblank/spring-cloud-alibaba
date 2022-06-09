package com.shiro.cloudshirosso.constant;

public interface Constant {

    /**
     * 用户HASH加密算法的名称
     */
    String HASH_ALGORITHM_NAME = "MD5";

    /**
     * 加密散列的次数
     */
    int HASH_ITERATIONS = 1024;

    /**
     * 请求头里面token的key
     */

    String AUTHORIZATION = "Authorization";

    int JWT_MINUTE = 30;

}
