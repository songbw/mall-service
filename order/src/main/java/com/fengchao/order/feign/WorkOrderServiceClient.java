package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.hystric.WorkOrderServiceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 */
@FeignClient(value = "workorders", fallbackFactory = WorkOrderServiceClientFallbackFactory.class)
public interface WorkOrderServiceClient {

    @RequestMapping(value = "/refund/query/refunded", method = RequestMethod.GET)
    OperaResponse<List<String>> queryRefundedOrderDetailIdList(@RequestParam(value = "merchantId", required = false) Long merchantId,
                                                                    @RequestParam(value = "startTime") Date startTime,
                                                                    @RequestParam(value = "endTime") Date endTime);
}
