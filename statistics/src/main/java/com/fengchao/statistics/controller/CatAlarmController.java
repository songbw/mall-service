package com.fengchao.statistics.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @Author tom
 * @Date 19-8-2 下午5:35
 */
@RestController
@RequestMapping(value = "/cat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class CatAlarmController {

    @GetMapping("/alarm")
    public void alarm(String type, String key, String re, String to, String title, String content) {
        log.info("type={}, key={}, re={}, to={}, value={}, content={}", type, key, re, to, title, content);

//        String []mail = to.split(",");
//
//        ArrayList<String> tos = CollUtil.newArrayList(mail);
//
//        MailUtil.send(tos, "测试", "邮件来自Hutool群发测试", false);
    }

    @GetMapping("/mail")
    public String testmail() {
        log.info("======asdfasdfasdf");
        MailUtil.send("peng.zhang@weesharing.com", "测试", "邮件来自Hutool测试", false);
        log.info("============end");

        return "finish ssss";
    }


}
