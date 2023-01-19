package com.ztt.datainterface;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class PrivacySerializer extends JsonSerializer<String> implements ContextualSerializer {

    // 脱敏类型
    private PrivacyTypeEnum privacyTypeEnum;
    // 前几位不脱敏
    private Integer prefixNoMaskLen;
    // 最后几位不脱敏
    private Integer suffixNoMaskLen;
    // 用什么打码
    private String symbol;

    @Override
    public void serialize(String origin, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        if (StringUtils.isNotBlank(origin) && null != privacyTypeEnum) {
            switch (privacyTypeEnum) {
                case CUSTOMER ->
                        jsonGenerator.writeString(PrivacyUtil.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
                case NAME -> jsonGenerator.writeString(PrivacyUtil.hideChineseName(origin));
                case ID_CARD -> jsonGenerator.writeString(PrivacyUtil.hideIDCard(origin));
                case PHONE -> jsonGenerator.writeString(PrivacyUtil.hidePhone(origin));
                case EMAIL -> jsonGenerator.writeString(PrivacyUtil.hideEmail(origin));
                case PASSWORD -> jsonGenerator.writeString(PrivacyUtil.hidePassword(origin));
                default -> throw new IllegalArgumentException("unknown privacy type enum " + privacyTypeEnum);
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                PrivacyEncrypt privacyEncrypt = beanProperty.getAnnotation(PrivacyEncrypt.class);
                if (privacyEncrypt == null) {
                    privacyEncrypt = beanProperty.getContextAnnotation(PrivacyEncrypt.class);
                }
                if (privacyEncrypt != null) {
                    return new PrivacySerializer(privacyEncrypt.type(), privacyEncrypt.prefixNoMaskLen(),
                            privacyEncrypt.suffixNoMaskLen(), privacyEncrypt.symbol());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
