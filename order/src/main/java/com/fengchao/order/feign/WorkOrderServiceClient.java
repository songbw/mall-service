package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.hystric.WorkOrderServiceClientFallbackFactory;
import com.fengchao.order.rpc.extmodel.WorkOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
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

    /**
     * 获取已退款的子订单id集合
     *
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/refund/query/refunded", method = RequestMethod.GET)
    OperaResponse<List<String>> queryRefundedOrderDetailIdList(@RequestParam(value = "merchantId", required = false) Long merchantId,
                                                                    @RequestParam(value = "startTime") String startTime,
                                                                    @RequestParam(value = "endTime") String endTime);

    /**
     * 获取已退款的子订单信息集合
     *
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/refund/query/refundedDetail", method = RequestMethod.GET)
    OperaResponse<List<WorkOrder>> queryRefundedOrderDetailList(@RequestParam(value = "merchantId", required = false) Long merchantId,
                                                                @RequestParam(value = "startTime") String startTime,
                                                                @RequestParam(value = "endTime") String endTime);
}
