package com.fengchao.pingan.controller;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.feign.WSPayClientService;
import com.fengchao.pingan.service.PaymentService;
import com.fengchao.pingan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    private OperaResponse getInitCode() {
        return userService.getInitCode() ;
    }

    @GetMapping("authCode")
    private OperaResponse getAuthCode() {
        return userService.getAuthCode() ;
    }

    @GetMapping("accessToken")
    private OperaResponse getAccessToken(String authCode) {
        return userService.getAuthAccessToken(authCode) ;
    }

    @GetMapping("refreshToken")
    private OperaResponse getRefreshToken(String refreshToken) {
        return userService.getRefreshToken(refreshToken) ;
    }

    @GetMapping("checkToken")
    private OperaResponse checkToken(String accessToken) {
        return userService.checkToken(accessToken) ;
    }

    @GetMapping("checkRequestCode")
    private OperaResponse checkRequestCode(String requestCode) {
        return userService.checkRequestCode(requestCode) ;
    }

    @GetMapping("userInfo")
    private OperaResponse getUserInfo(String userAccessToken) {
        return userService.getAuthUserInfo(userAccessToken) ;
    }

}
