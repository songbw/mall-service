package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.OrderServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order", fallback = OrderServiceH.class)
public interface OrderService {

    @RequestMapping(value = "/prod", method = RequestMethod.GET)
    OperaResult find(@RequestParam("mpu") String mpu);

}
