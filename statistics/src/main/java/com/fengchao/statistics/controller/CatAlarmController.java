package com.fengchao.statistics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author tom
 * @Date 19-8-2 下午5:35
 */
@RestController
@RequestMapping(value = "/cat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class CatAlarmController {

    @GetMapping("/alarm")
    private void alarm(String type, String key, String re, String to, String value) {
        log.info("type={}, key={}, re={}, to={}, value={}", type, key, re, to, value);
    }
}
