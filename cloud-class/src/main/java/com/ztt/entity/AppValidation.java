package com.ztt.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class AppValidation implements Serializable {

	@Min(1)
	private BigDecimal id;
	@NotEmpty(message = "appId不能为空字符串")
	@NotNull(message = "appId不能为null")
	@Length(max = 225)
	private String appId;
	@NotNull(message = "name不能为空")
	private String name;
	@Email(message = "邮箱不正确")
	private String email;

	@Override
	public String toString() {
		return covert().toString();
	}

	private HashMap<String, Object> covert() {
		HashMap<String, Object> objectHashMap = new HashMap<>();
		objectHashMap.put("Id", this.id);
		objectHashMap.put("appId", this.appId);
		objectHashMap.put("name", this.name);
		objectHashMap.put("email", this.email);
		return objectHashMap;
	}
}
