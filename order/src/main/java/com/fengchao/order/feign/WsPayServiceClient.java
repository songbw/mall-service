package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.vo.BillExportReqVo;
import com.fengchao.order.feign.hystric.WspayServiceClientFallbackFactory;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.rpc.extmodel.PayInfoBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 *
 * http://192.168.200.122:8016/swagger-ui.html#/pay-controller 入参: a9f4a94c7ca24245a64888841aafc07b,130832f46d8240078a11d7b97150f2ac
 */
@FeignClient(value = "aggpay", fallbackFactory = WspayServiceClientFallbackFactory.class)
public interface WsPayServiceClient {

    @RequestMapping(value = "/wspay/batch/query/pay", method = RequestMethod.GET)
    OperaResponse<Map<String, List<OrderPayMethodInfoBean>>> queryBatchPayMethod(@RequestParam(value = "orderNo") List<String> orderNoList);

    @RequestMapping(value = "/wspay/query/candr", method = RequestMethod.POST)
    OperaResponse<PayInfoBean> queryConsumeRefundUsing(@RequestBody BillExportReqVo billExportReqVo);

    @RequestMapping(value = "/wspay/batch/query/refund", method = RequestMethod.GET)
    OperaResponse<Map<String,List<OrderPayMethodInfoBean>>> queryBatchRefundMethod(@RequestParam(value = "orderNo") List<String> orderNoList);
}
