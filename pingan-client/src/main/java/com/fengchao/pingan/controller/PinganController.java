package com.fengchao.pingan.controller;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.feign.WSPayClientService;
import com.fengchao.pingan.service.PaymentService;
import com.fengchao.pingan.service.UserService;
import com.fengchao.pingan.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/pingan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PinganController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("token")
    private OperaResult findToken(String initCode, OperaResult result) {
        TokenResult tokenResult = userService.getAccessToken(initCode);
        if (tokenResult.isSuccess()) {
            result.getData().put("result", tokenResult.getData());
        } else {
            result.setCode(-1);
            result.setMsg(tokenResult.getMessage());
        }
        return result ;
    }

    @GetMapping("user")
    private OperaResult findUser(String userToken, OperaResult result) {
        UserResult userResult = userService.getUserInfo(userToken);
        if (userResult.isSuccess()) {
            result.getData().put("result", userResult.getData());
        } else {
            result.setCode(-1);
            result.setMsg(userResult.getMessage());
        }
        return result ;
    }

    @PostMapping("payment")
    private OperaResult payment(@RequestBody PaymentBean paymentBean, OperaResult result) {
//        result.getData().put("result", paymentService.paymentOrder(paymentBean)) ;
        result.getData().put("result", paymentService.wsPayClient(paymentBean)) ;
        return result;
    }

    @PostMapping("back")
    private OperaResult back(@RequestBody RefundBean bean, OperaResult result) {
        result.getData().put("result", paymentService.payRefund(bean));
        return result;
    }

    @GetMapping("initCode")
    private OperaResponse getInitCode(@RequestHeader("appId") String appId) {
        return userService.getInitCode(appId) ;
    }

    @GetMapping("authCode")
    private OperaResponse getAuthCode(String appId) {
        return userService.getAuthCode(appId) ;
    }

    @GetMapping("accessToken")
    private OperaResponse getAccessToken(String appId, String authCode) {
        return userService.getAuthAccessToken(appId, authCode) ;
    }

    @GetMapping("refreshToken")
    private OperaResponse getRefreshToken(String appId, String refreshToken) {
        return userService.getRefreshToken(appId, refreshToken) ;
    }

    @GetMapping("checkToken")
    private OperaResponse checkToken(String appId, String accessToken) {
        return userService.checkToken(appId, accessToken) ;
    }

    @GetMapping("checkRequestCode")
    private OperaResponse checkRequestCode(String appId, String requestCode) {
//        return userService.checkRequestCode(requestCode) ;
        return userService.getAuthUserInfoByRequestCode(appId, requestCode) ;
    }

    @GetMapping("userInfo")
    private OperaResponse getUserInfo(String appId, String userAccessToken) {
        return userService.getAuthUserInfo(appId, userAccessToken) ;
    }

    @PostMapping("payment/create")
    private OperaResponse paymentOrder(@RequestBody CreatePaymentOrderRequestBean paymentBean) {
        log.info("请求 payment/create 入口参数： {}", JSONUtil.toJsonString(paymentBean));
        return paymentService.createPaymentOrder(paymentBean);
    }

    @PostMapping("payment/refund")
    private OperaResponse orderRefund(@RequestBody OrderRefundRequestBean paymentBean) {
        return paymentService.orderRefund(paymentBean);
    }

    @PostMapping("payment/query")
    private OperaResponse queryPaymentOrder(@RequestBody QueryPaymentOrderRequestBean paymentBean) {
        return paymentService.queryPaymentOrder(paymentBean);
    }

    @PostMapping("payment/back")
    private String paymentBack(@RequestBody BackNotifyRequestBean paymentBean) {
        return paymentService.backNotify(paymentBean);
    }

}
