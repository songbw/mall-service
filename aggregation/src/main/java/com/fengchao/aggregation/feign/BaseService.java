package com.fengchao.aggregation.feign;

import com.fengchao.aggregation.bean.Email;
import com.fengchao.aggregation.bean.OperaResponse;
import com.fengchao.aggregation.feign.hystric.BaseServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "base", fallback = BaseServiceH.class)
public interface BaseService {

    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    OperaResponse sendMail(@RequestBody Email email);
}
