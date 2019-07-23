package com.fengchao.sso.feign;


import com.fengchao.sso.bean.OpenId;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.bean.RefundBean;
import com.fengchao.sso.feign.hystric.GuanaitongClientServiceH;
import com.fengchao.sso.feign.hystric.PinganClientServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "guanaitong-client", fallback = GuanaitongClientServiceH.class)
public interface GuanaitongClientService {

    @RequestMapping(value = "/pingan/payment", method = RequestMethod.POST)
    OperaResult payment(@RequestBody PaymentBean paymentBean);

    @RequestMapping(value = "/pingan/back", method = RequestMethod.POST)
    OperaResult back(@RequestBody RefundBean bean);

    @RequestMapping(value = "/seller/person/getByAuthCode", method = RequestMethod.POST)
    OperaResult findOpenId(@RequestBody OpenId openId);

    @RequestMapping(value = "/seller/person/getDetailByOpenId", method = RequestMethod.POST)
    OperaResult findUser(@RequestBody OpenId openId);
}
