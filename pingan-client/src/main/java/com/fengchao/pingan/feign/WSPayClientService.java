package com.fengchao.pingan.feign;


import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.feign.hystric.WSPayClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "aggpay", fallback = WSPayClientServiceH.class)
public interface WSPayClientService {

    @RequestMapping(value = "/wspay/prepay", method = RequestMethod.POST)
    CommonResult<PrePayResultDTO> payment(@RequestBody PrePayDTO paymentBean);

    @RequestMapping(value = "/wspay_notify/bj/weesharing_pay", method = RequestMethod.POST)
    CommonResult<String> aggPayBack(@RequestBody AggPayBackBean aggPayBackBean);

    @RequestMapping(value = "/wspay_notify/bj/weesharing_refund", method = RequestMethod.POST)
    CommonResult<String> aggRefundBack(@RequestBody AggPayBackBean aggPayBackBean);
}
