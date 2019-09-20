package com.fengchao.pingan.feign;


import com.fengchao.pingan.bean.CommonResult;
import com.fengchao.pingan.bean.PaymentBean;
import com.fengchao.pingan.bean.PrePayResultDTO;
import com.fengchao.pingan.feign.hystric.WSPayClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "aggpay", fallback = WSPayClientServiceH.class)
public interface WSPayClientService {

    @RequestMapping(value = "/wspay/prepay", method = RequestMethod.POST)
    CommonResult<PrePayResultDTO> payment(@RequestBody PaymentBean paymentBean);
}
