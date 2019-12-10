package com.fengchao.sso.feign;


import com.fengchao.sso.bean.AuthUserBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.bean.RefundBean;
import com.fengchao.sso.feign.hystric.PinganClientServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "pingan-client", fallback = PinganClientServiceH.class)
public interface PinganClientService {

    @RequestMapping(value = "/pingan/payment", method = RequestMethod.POST)
    OperaResult payment(@RequestBody PaymentBean paymentBean);

    @RequestMapping(value = "/pingan/back", method = RequestMethod.POST)
    OperaResult back(@RequestBody RefundBean bean);

    @RequestMapping(value = "/pingan/token", method = RequestMethod.GET)
    OperaResult findToken(@RequestParam("initCode") String initCode);

    @RequestMapping(value = "/pingan/user", method = RequestMethod.GET)
    OperaResult findUser(@RequestParam("userToken") String userToken);

    @RequestMapping(value = "/pingan/checkRequestCode", method = RequestMethod.GET)
    OperaResponse<AuthUserBean> checkRequestCode(@RequestParam("requestCode") String requestCode, @RequestParam("appId") String appId) ;
}
