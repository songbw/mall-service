package com.fengchao.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author tom
 * @Date 19-7-23 下午6:18
 */

@RestController
@RequestMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class HelloController {

    @GetMapping
    private String hello() {
        log.info("I am ok!!!!");
        return "I am ok!";
    }
}
