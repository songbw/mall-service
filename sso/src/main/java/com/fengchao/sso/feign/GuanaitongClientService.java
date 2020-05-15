package com.fengchao.sso.feign;

import com.fengchao.sso.bean.AuthCode;
import com.fengchao.sso.bean.OpenId;
import com.fengchao.sso.bean.RefundBean;
import com.fengchao.sso.bean.Result;
import com.fengchao.sso.feign.hystric.GuanaitongClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "guanaitong-client", fallback = GuanaitongClientServiceH.class)
public interface GuanaitongClientService {

    @RequestMapping(value = "/seller/sign/param", method = RequestMethod.POST)
    Result payment(@RequestBody Map<String, String> map, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/pingan/back", method = RequestMethod.POST)
    Result back(@RequestBody RefundBean bean);

    @RequestMapping(value = "/seller/person/getByAuthCode", method = RequestMethod.POST)
    Result findOpenId(@RequestBody AuthCode authCode, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/seller/person/getDetailByOpenId", method = RequestMethod.POST)
    Result findUser(@RequestBody OpenId openId, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/seller/pay/syncRefund", method = RequestMethod.POST)
    Result refund(@RequestBody Map<String, String> map, @RequestHeader("appId") String appId);
}
