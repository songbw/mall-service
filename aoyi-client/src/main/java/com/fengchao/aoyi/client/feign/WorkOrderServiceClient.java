package com.fengchao.aoyi.client.feign;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.StarBackBean;
import com.fengchao.aoyi.client.feign.hystric.WorkOrderServiceClientH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "workorders", fallbackFactory = WorkOrderServiceClientH.class)
public interface WorkOrderServiceClient {

    @RequestMapping(value = "/work_orders/aoyi/refund/status", method = RequestMethod.POST)
    OperaResponse refundStatus(@RequestBody StarBackBean bean);

}
