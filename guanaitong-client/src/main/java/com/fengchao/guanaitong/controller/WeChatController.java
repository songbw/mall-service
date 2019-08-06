package com.fengchao.guanaitong.controller;

import com.fengchao.guanaitong.service.impl.JSSDKServiceImpl;
import com.fengchao.guanaitong.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WeChatController {

    private JSSDKServiceImpl jssdkService;

    private class TokenData{
        public String access_token;
    }

    @Autowired
    public WeChatController(JSSDKServiceImpl jssdkService) {
        this.jssdkService = jssdkService;
    }

    @GetMapping("jssdk/token")
    public ResultObject<TokenData> getJsApiTicket() {

        log.info("===WeChat jsapi_ticket enter");

        String token = null;
        try {
            token = jssdkService.getAccessToken();
        } catch (Exception e) {
            log.info("getAccessToken got exception : {}",e.getMessage());
        }
        if (null == token) {
            return new ResultObject<>(400,"failed to access WeChat",null);
        }
        TokenData tokenData = new TokenData();
        tokenData.access_token = token;
        log.info("get WeChat token exit normally, got token : ",token);
        return new ResultObject<>(200,"success",tokenData);

    }

    @GetMapping("jssdk/sign")
    public ResultObject<String> getJsApiSignUrl(@RequestParam String url) {

        log.info("===WeChat jsapi_sign url enter, url {} ",url);

        String sign = null;
        try {
            sign = jssdkService.getJsApiSign(url);
        } catch (Exception e) {
            log.info("getJsApiSignUrl got exception : {}",e.getMessage());
        }
        if (null == sign) {
            return new ResultObject<>(400,"failed to create sign",null);
        }

        log.info("get WeChat sign url : {}",sign);
        return new ResultObject<>(200,"success",sign);

    }
}
