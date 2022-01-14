package com.sa.cloudsatoken.contorller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token/api/v2")
public class MainApiController {

    @RequestMapping("/info")
    public String getInfo(){
        return "hello sa-token";
    }

    @RequestMapping("/error")
    public String error(){
       int i = 9/0;
       return String.valueOf(i);
    }
}
