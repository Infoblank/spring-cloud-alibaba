package com.ztt.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping
@RestController
public class FaviconController {
    @RequestMapping("favicon.ico")
    public void error() {
    }
}
