package com.fengchao.base.controller;

import com.fengchao.base.bean.Email;
import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MailController {

    @Autowired
    private MailService service;

    @PostMapping("send")
    private OperaResponse send(@RequestBody Email email) {
        OperaResponse response = new OperaResponse();
        service.send(email);
        return response ;
    }

}
