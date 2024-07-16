package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.aop.ProhibitDupAccess;
import com.ztt.entity.AppValidation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/consumer/validation")
public class ValidationController {

	/**
	 * // @RestController + @RequestBody + @Valid 会IO
	 *
	 * @param appValidation
	 * @return
	 */

	@PostMapping(path = "/appValidation")
	@ProhibitDupAccess
	public String validation(@Validated @RequestBody AppValidation appValidation) {
		return appValidation.toString();
	}
}
