package com.fengchao.sso.controller;

import com.fengchao.sso.bean.BackRequest;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.service.IPaymentService;
import com.fengchao.sso.util.OperaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaymentController {

    private static Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private IPaymentService service;

    @PostMapping("/pingan")
    private OperaResult pingAnPayment(@RequestBody PaymentBean paymentBean, OperaResult result){
        return service.payment(paymentBean);
    }

    @PostMapping("/pingan/back")
    private String back(@RequestBody BackRequest bean) {
        return service.back(bean);
    }
}
