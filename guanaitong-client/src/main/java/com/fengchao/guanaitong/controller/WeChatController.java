package com.fengchao.guanaitong.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.guanaitong.bean.JssdkSignBean;
import com.fengchao.guanaitong.service.impl.JSSDKServiceImpl;
import com.fengchao.guanaitong.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    public ResultObject<TokenData>
    getJsApiTicket() {

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

    @PostMapping("jssdk/sign")
    public ResultObject<JssdkSignBean>
    getJsApiSignUrl(@RequestBody Map<String, Object> body) {

        if (null == body || null == body.get("url")){
            return new ResultObject<>(400002,"缺少输入参数",null);
        }
        String url = body.get("url").toString();
        log.info("===WeChat jsapi_sign url enter, url {} ",url);

        JssdkSignBean sign = null;
        try {
            sign = jssdkService.getJsApiSign(url);
        } catch (Exception e) {
            log.info("getJsApiSignUrl got exception : {}",e.getMessage());
        }
        if (null == sign) {
            return new ResultObject<>(400,"failed to create sign",null);
        }

        log.info("get WeChat sign url : {}", JSON.toJSONString(sign));
        return new ResultObject<>(200,"success",sign);

    }
}
