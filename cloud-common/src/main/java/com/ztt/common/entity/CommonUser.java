package com.ztt.common.entity;

import java.io.Serializable;

/**
 * @author ZTT
 */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
