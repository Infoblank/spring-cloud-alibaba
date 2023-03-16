package com.ztt.datainterface;

import lombok.Getter;

@Getter
//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
public enum PrivacyTypeEnum {
    /* 自定义脱敏范围 */
    CUSTOMER,
    /*姓名*/
    NAME,
    /*身份证*/
    ID_CARD,
    /*电话*/
    PHONE,
    /*邮箱*/
    EMAIL,
    PASSWORD
}
