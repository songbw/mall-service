package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.OrderParamBean;
import com.fengchao.order.feign.hystric.AoyiClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "aoyi-client", fallback = AoyiClientServiceH.class)
public interface AoyiClientService {

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    OperaResult order(@RequestBody OrderParamBean orderParamBean);

}
