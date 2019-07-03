package com.fengchao.sso.feign;

import com.fengchao.sso.bean.Order;
import com.fengchao.sso.feign.hystric.OrderServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order", fallback = OrderServiceH.class)
public interface OrderService {

    @RequestMapping(value = "/order/tradeNo", method = RequestMethod.GET)
    OperaResult findOrderListByTradeNo(@RequestParam("appId") String appId, @RequestParam("merchantNo") String merchantNo, @RequestParam("tradeNo") String tradeNo);

    @RequestMapping(value = "/order/outTradeNo", method = RequestMethod.GET)
    OperaResult findOrderListByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo);

    @RequestMapping(value = "/order/payment", method = RequestMethod.PUT)
    OperaResult payment(@RequestBody Order order);

    @RequestMapping(value = "/order/outTradeNo/payment", method = RequestMethod.GET)
    OperaResult findByOutTradeNoAndPaymentNo(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("paymentNo") String paymentNo);

    @RequestMapping(value = "/order/outTradeNo/payment", method = RequestMethod.PUT)
    OperaResult paymentByOutTradeNoAndPaymentNo(@RequestBody Order order);

}
