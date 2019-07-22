package com.fengchao.guanaitong.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.fengchao.guanaitong.service.impl.GuanAiTongServiceImpl;
import com.fengchao.guanaitong.util.*;

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
    public ResultObject<TokenData> getToken(HttpServletResponse response
                         ) throws RuntimeException {

        log.info("===getToken enter");

        String token = guanAiTongService.getAccessToken();
        TokenData tokenData = new TokenData();
        tokenData.access_token = token;

        return new ResultObject<>(200,"success",tokenData);

    }


}
