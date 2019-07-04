package com.fengchao.order.controller;

import com.fengchao.order.bean.*;
import com.fengchao.order.model.Order;
import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.Kuaidi100;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单列表
 */
@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Autowired
    private OrderService service;


    @PostMapping("/all")
    private OperaResult findList(@RequestBody OrderQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody OrderParamBean bean, OperaResult result) {
        List<SubOrderT> subOrderTS = null;
        subOrderTS = service.add2(bean);
        result.getData().put("result", subOrderTS) ;
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @GetMapping("cancel")
    private OperaResult cancel(Integer id, OperaResult result) {
        result.getData().put("result", service.cancel(id)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        result.getData().put("result", service.findById(id)) ;
        return result;
    }

    @PutMapping("status")
    private OperaResult updateStatus(@RequestBody Order bean, OperaResult result) {
        result.getData().put("result", service.updateStatus(bean)) ;
        return result;
    }

    @PostMapping("/searchOrder")
    private OperaResult searchOrderList(@RequestBody OrderBean orderBean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        orderBean.setMerchantId(merchantId);
        result.getData().put("result", service.searchOrderList(orderBean)) ;
        return result;
    }

    @PostMapping("/updateRemark")
    private OperaResult updateRemark(@RequestBody Order bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        bean.setMerchantId(merchantId);
        result.getData().put("result", service.updateRemark(bean)) ;
        return result;
    }

    @PostMapping("/updateAddress")
    private OperaResult updateOrderAddress(@RequestBody Order bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        bean.setMerchantId(merchantId);
        result.getData().put("result", service.updateOrderAddress(bean)) ;
        return result;
    }

    @PostMapping("/searchDetail")
    private OperaResult searchDetail(@RequestBody OrderQueryBean queryBean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        queryBean.setMerchantId(merchantId);
        result.getData().put("result", service.searchDetail(queryBean)) ;
        return result;
    }

    @PostMapping("/uploadLogistics")
    private OperaResult uploadLogistics(@RequestBody Logisticsbean bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        System.out.println(bean.getLogisticsList());
        bean.setMerchantId(merchantId);
        result.getData().put("result", service.uploadLogistics(bean)) ;

        return result;
    }

    @GetMapping("/queryLogisticsInfo")
    private OperaResult queryLogisticsInfo(@RequestHeader("merchant") Integer merchantId, String logisticsId, OperaResult result) {
        result.getData().put("result", service.queryLogisticsInfo(logisticsId)) ;
        return result;
    }

    @GetMapping("/logistics")
    private OperaResult getLogistics(String orderId, String merchantNo, OperaResult result) {
        result.getData().put("result", service.getLogist(merchantNo, orderId)) ;
        return result;
    }

    @GetMapping("/tradeNo")
    private OperaResult findOrderListByTradeNo(String appId, String merchantNo, String tradeNo, OperaResult result) {
        result.getData().put("result", service.findTradeNo(appId, merchantNo, tradeNo)) ;
        return result;
    }

    @GetMapping("/outTradeNo")
    private OperaResult findOrderListByOutTradeNo(String outTradeNo, OperaResult result) {
        result.getData().put("result", service.findOutTradeNo(outTradeNo)) ;
        return result;
    }

    @GetMapping("/outTradeNo/payment")
    private OperaResult findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo, OperaResult result) {
        result.getData().put("result", service.findByOutTradeNoAndPaymentNo(outTradeNo, paymentNo)) ;
        return result;
    }

    @PutMapping("/payment")
    private OperaResult payment(@RequestBody Order order, OperaResult result) {
        result.getData().put("result", service.updatePaymentNo(order)) ;
        return result;
    }

    @PutMapping("/outTradeNo/payment")
    private OperaResult paymentByOutTradeNoAndPaymentNo(@RequestBody Order order, OperaResult result) {
        result.getData().put("result", service.updatePaymentByOutTradeNoAndPaymentNo(order)) ;
        return result;
    }

}
