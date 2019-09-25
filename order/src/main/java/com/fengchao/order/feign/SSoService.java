package com.fengchao.order.feign;

import com.fengchao.order.bean.BalanceDetail;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.hystric.SSoServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "sso", fallback = SSoServiceH.class)
public interface SSoService {

    @RequestMapping(value = "/balance/consum", method = RequestMethod.PUT)
    OperaResponse<BalanceDetail> consum(@RequestBody BalanceDetail detail);

}
