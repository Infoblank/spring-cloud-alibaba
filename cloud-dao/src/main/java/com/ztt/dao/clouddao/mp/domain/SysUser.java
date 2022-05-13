package com.ztt.dao.clouddao.mp.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户表
 *
 * @TableName sys_user
 */
@TableName(value = "rm_pm_user")
public class SysUser implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 登录状态
     */
    private Byte loginState;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Byte deletedFlag;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime loginTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
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


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTim) {
        this.modifyTime = modifyTim;
    }


    public Byte getLoginState() {
        return loginState;
    }


    public void setLoginState(Byte loginState) {
        this.loginState = loginState;
    }


    public Byte getDeletedFlag() {
        return deletedFlag;
    }


    public void setDeletedFlag(byte deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public void setDeletedFlag(Byte deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysUser other = (SysUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLoginName() == null ? other.getLoginName() == null : this.getLoginName().equals(other.getLoginName()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
                && (this.getLoginState() == null ? other.getLoginState() == null : this.getLoginState().equals(other.getLoginState()))
                && (this.getLoginTime() == null ? other.getLoginTime() == null : this.getLoginTime().equals(other.getLoginTime()))
                && (this.getDeletedFlag() == null ? other.getDeletedFlag() == null : this.getDeletedFlag().equals(other.getDeletedFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLoginName() == null) ? 0 : getLoginName().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getLoginState() == null) ? 0 : getLoginState().hashCode());
        result = prime * result + ((getLoginTime() == null) ? 0 : getLoginTime().hashCode());
        result = prime * result + ((getDeletedFlag() == null) ? 0 : getDeletedFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + this.id +
                ", loginName=" + this.loginName +
                ", name=" + this.name +
                ", password=" + this.password +
                ", createTime=" + this.createTime +
                ", modifyTim=" + this.modifyTime +
                ", loginState=" + this.loginState +
                ", deletedFlag=" + this.deletedFlag +
                "]";
    }
}