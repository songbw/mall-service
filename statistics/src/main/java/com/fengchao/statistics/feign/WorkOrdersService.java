package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.WorkOrderServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "workorders", fallback = WorkOrderServiceH.class)
public interface WorkOrdersService {

    @RequestMapping(value = "/work_orders/refunds", method = RequestMethod.GET)
    OperaResult refundCount(@RequestParam("timeStart") String timeStart, @RequestParam("timeEnd") String timeEnd);

}
