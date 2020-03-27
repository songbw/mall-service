package com.fengchao.aoyi.client.feign;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.Orders;
import com.fengchao.aoyi.client.bean.StarBackBean;
import com.fengchao.aoyi.client.feign.hystric.OrderServiceClientH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "order", fallbackFactory = OrderServiceClientH.class)
public interface OrderServiceClient {

    @RequestMapping(value = "/order/status/deliver", method = RequestMethod.PUT)
    OperaResponse deliverStatue(@RequestBody StarBackBean bean);

}
