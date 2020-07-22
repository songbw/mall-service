package com.fengchao.guanaitong.controller;

import com.fengchao.guanaitong.constant.MyErrorEnum;
import com.fengchao.guanaitong.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.fengchao.guanaitong.service.impl.GuanAiTongServiceImpl;
import com.fengchao.guanaitong.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CommonController {

    private GuanAiTongServiceImpl guanAiTongService;

    private class TokenData{
        public String access_token;
    }

    @Autowired
    public CommonController(GuanAiTongServiceImpl guanAiTongService
                            ) {
        this.guanAiTongService = guanAiTongService;
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("token")
    public ResultObject<TokenData>
    getToken(HttpServletRequest request
            ) {

        String iAppId = request.getHeader("appId");
        if (null == iAppId){
            throw new MyException(MyErrorEnum.HTTP_HEADER_APPID_BLANK);
        }
        log.info("===getToken enter iAppId={}",iAppId);

        String token = null;
        try {
            token = guanAiTongService.getAccessToken(iAppId);
        } catch (Exception e) {
            log.info("getAccessToken got exception : {}",e.getMessage());

        }
        if (null == token) {
            return new ResultObject<>(400,"failed to access GuanAiTong",null);
        }
        TokenData tokenData = new TokenData();
        tokenData.access_token = token;
        log.info("get token exit normally, got token : ",token);
        return new ResultObject<>(200,"success",tokenData);

    }


}
