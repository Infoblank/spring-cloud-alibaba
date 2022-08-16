package com.ztt.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZTT
 */
@Getter
@Setter
@ToString
public class CommonUser implements Serializable {

    private String id;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 登录密码
     */

    private String password;

    /**
     *
     */
    private String createTime;

    /**
     *
     */
    private String modifyTime;

    /**
     * 登录状态
     */
    private String loginState;

    /**
     * 逻辑删除标记
     */
    private String deletedFlag;

    private String loginTime;
}
