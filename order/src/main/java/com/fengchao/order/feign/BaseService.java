package com.fengchao.order.feign;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.hystric.AoyiClientServiceH;
import com.fengchao.order.feign.hystric.BaseServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "base", fallback = BaseServiceH.class)
public interface BaseService {

    @RequestMapping(value = "/kuaidi", method = RequestMethod.GET)
    OperaResponse<JSONObject> kuaidi100(@RequestParam("num") String num, @RequestParam("code") String code);

}
