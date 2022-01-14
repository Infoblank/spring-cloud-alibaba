package com.sa.cloudsatoken.contorller.api.valid;

import com.sa.cloudsatoken.entity.ValidEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation/validEntity")
@Slf4j
public class ValidEntityController {

    @PostMapping("/valid/json")
    public String validJson(@RequestBody @Validated ValidEntity validEntity){
        log.info("ValidEntityController-->validJson");
        return validEntity.toString();
    }

    @PostMapping("/valid")
    public String valid(@Validated ValidEntity validEntity){
        log.info("ValidEntityController-->valid");
        return validEntity.toString();
    }
}
