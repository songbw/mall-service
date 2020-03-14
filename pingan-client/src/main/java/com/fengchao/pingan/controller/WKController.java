package com.fengchao.pingan.controller;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.service.WKPaymentService;
import com.fengchao.pingan.service.WKUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/wkyc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WKController {

    @Autowired
    private WKUserService userService;

    @Autowired
    private WKPaymentService paymentService;

    @GetMapping("userInfo")
    private OperaResponse getUserInfo(String openId, String accessToken) {
        OperaResponse operaResponse = new OperaResponse() ;
        WKUserRequestBean bean = new WKUserRequestBean() ;
        bean.setOpenid(openId);
        bean.setAccess_token(accessToken);
        WKOperaResponse wkOperaResponse = userService.getWKUserInfo(bean) ;
        if ("0".equals(wkOperaResponse.getRet())) {
            operaResponse.setData(wkOperaResponse.getData());
        } else {
            operaResponse.setCode(800100);
            operaResponse.setMsg(wkOperaResponse.getErrorCode());
            operaResponse.setMessage(wkOperaResponse.getSmsg());
        }
        return operaResponse;
    }

    @PostMapping("payment/refund")
    private OperaResponse orderRefund(@RequestBody WKRefundRequestBean paymentBean) {
        WKOperaResponse wkOperaResponse = paymentService.refundApply(paymentBean) ;
        OperaResponse operaResponse = new OperaResponse() ;
        if ("0".equals(wkOperaResponse.getRet())) {
            operaResponse.setData(wkOperaResponse.getData());
        } else {
            operaResponse.setCode(800100);
            operaResponse.setMsg(wkOperaResponse.getMsg());
            operaResponse.setMessage(wkOperaResponse.getSmsg());
        }
        return operaResponse;
    }

    @PostMapping("payment/back")
    private String paymentBack(@RequestBody WKPaymentNotifyRequestBean paymentBean) {
        return paymentService.paymentNotify(paymentBean);
    }

    @PostMapping("refund/back")
    private String refundBack(@RequestBody WKRefundNotifyRequestBean paymentBean) {
        return paymentService.refundNotify(paymentBean);
    }

}
