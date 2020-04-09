package com.fengchao.equity.feign;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.feign.hystric.WelfareClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "welfare", fallback = WelfareClient.class)
public interface IWelfareClient {

    @RequestMapping(value = "/orders/cardInfoCodes", method = RequestMethod.GET)
    JSONObject getCardInfoCodes(@RequestParam(value = "corporationCode",required = false) String corporationCode);

    @RequestMapping(value = "/orders/cardInfoGroupCodes", method = RequestMethod.GET)
    JSONObject getCardInfoGroupCodes(@RequestParam(value = "corporationCode",required = false) String corporationCode);

}
