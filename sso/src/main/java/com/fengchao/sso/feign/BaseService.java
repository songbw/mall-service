package com.fengchao.sso.feign;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.SMSPostBean;
import com.fengchao.sso.feign.hystric.BaseServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "base", fallback = BaseServiceH.class)
public interface BaseService {

    @RequestMapping(value = "/sms/send", method = RequestMethod.POST)
    OperaResponse<String> send(@RequestBody SMSPostBean bean);

}
