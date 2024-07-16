package com.ztt.entity;

import com.ztt.datainterface.PrivacyEncrypt;
import com.ztt.datainterface.PrivacyTypeEnum;
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


    @PrivacyEncrypt(type = PrivacyTypeEnum.NAME)
    private String name;

    /**
     * 登录密码
     */

    @PrivacyEncrypt(type = PrivacyTypeEnum.CUSTOMER, prefixNoMaskLen = 3, suffixNoMaskLen = 3)
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

    public void init(){
        this.loginName= "init";
        this.name = "init";
    }
}
