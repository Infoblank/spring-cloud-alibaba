package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.entity.AppValidation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/consumer/validation")
@RestController
public class ValidationController {

    /**
     * // @RestController + @RequestBody + @Valid 会IO
     *
     * @param appValidation
     * @return
     */

    @PostMapping(path = "/appValidation")
    public String validation(@Valid @RequestBody AppValidation appValidation) {
        return appValidation.toString();
    }
}
