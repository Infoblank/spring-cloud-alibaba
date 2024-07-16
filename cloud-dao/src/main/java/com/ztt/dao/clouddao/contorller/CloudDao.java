package com.ztt.dao.clouddao.contorller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dao")
@RestController
public class CloudDao {

    @PostMapping("/queryFileName")
    public String queryFileName(String fileName){
        return "";
    }
}
