package com.fengchao.order.feign;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.hystric.BaseServiceClientFallbackFactory;
import com.fengchao.order.rpc.extmodel.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "base", fallbackFactory = BaseServiceClientFallbackFactory.class)
public interface BaseServiceClient {

    @RequestMapping(value = "/kuaidi", method = RequestMethod.GET)
    OperaResponse<JSONObject> kuaidi100(@RequestParam("num") String num, @RequestParam("code") String code);


    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    OperaResponse sendMail(@RequestBody Email email);
}
