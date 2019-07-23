package com.fengchao.sso.controller;

import com.fengchao.sso.bean.BackRequest;
import com.fengchao.sso.bean.GATBackBean;
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
@RequestMapping(value = "/payment")
public class PaymentController {

    private static Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private IPaymentService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OperaResult pingAnPayment(@RequestBody PaymentBean paymentBean, OperaResult result){
        return service.payment(paymentBean);
    }

    @PostMapping(value = "/pingan/back", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private String back(@RequestBody BackRequest bean) {
        return service.back(bean);
    }

    @PostMapping(value = "/back", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private String gBack(@RequestBody GATBackBean bean) {
        return service.gNotify(bean) ;
    }
}
