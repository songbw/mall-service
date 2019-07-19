package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.SsoServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "sso", fallback = SsoServiceH.class)
public interface SsoService {

    @RequestMapping(value = "/user/count", method = RequestMethod.GET)
    OperaResult count();

}
