package com.fengchao.guanaitong.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.guanaitong.bean.JssdkSignBean;
import com.fengchao.guanaitong.bean.WXIds;
import com.fengchao.guanaitong.config.WeChatConfiguration;
import com.fengchao.guanaitong.constant.MyErrorEnum;
import com.fengchao.guanaitong.exception.MyException;
import com.fengchao.guanaitong.service.impl.JSSDKServiceImpl;
import com.fengchao.guanaitong.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    getJsApiTicket(HttpServletRequest request) {

        log.info("===WeChat jsapi_ticket enter");

        String iAppId = getIAppId(request);
        String token = null;
        try {
            token = jssdkService.getAccessToken(iAppId);
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
    getJsApiSignUrl(HttpServletRequest request, @RequestBody Map<String, Object> body) {

        if (null == body || null == body.get("url")){
            throw new MyException(MyErrorEnum.PARAM_URL_BLANK);
        }

        String iAppId = getIAppId(request);
        String url = body.get("url").toString();

        log.info("===WeChat jsapi_sign url enter, url {}, iAppId={} ",url,iAppId);

        JssdkSignBean sign = null;
        try {
            sign = jssdkService.getJsApiSign(url,iAppId);
        } catch (Exception e) {
            log.info("getJsApiSignUrl got exception : {}",e.getMessage());
        }
        if (null == sign) {
            return new ResultObject<>(400,"failed to create sign",null);
        }

        log.info("===get WeChat sign url : {}", JSON.toJSONString(sign));
        return new ResultObject<>(200,"success",sign);

    }

    private String getIAppId(HttpServletRequest request){
        String iAppId = request.getHeader("appId");
        if (null == iAppId){
            throw new MyException(MyErrorEnum.HTTP_HEADER_APPID_BLANK);
        }
        return iAppId;
    }
}
