package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.WorkOrderServiceClientH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "workorders", fallback = WorkOrderServiceClientH.class)
public interface WorkOrdersServiceClient {

    /**
     * 获取退货人数
     *
     * @return
     */
    @RequestMapping(value = "/work_orders/refunds", method = RequestMethod.GET)
    OperaResponse refundOrdersCount();
}
