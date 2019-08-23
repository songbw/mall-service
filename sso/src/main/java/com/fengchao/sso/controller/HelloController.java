package com.fengchao.sso.controller;

import com.fengchao.sso.config.SSOConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author tom
 * @Date 19-8-23 上午11:03
 */
@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String getVerificationCode() {
        log.info("sso server is ok!");
        return "sso server is ok!";
    }
}
