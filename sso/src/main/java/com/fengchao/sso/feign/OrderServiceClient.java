package com.fengchao.sso.feign;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.Order;
import com.fengchao.sso.feign.hystric.OrderServiceClientFallbackFactory;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order", url = "${rpc.feign.client.order.url:}", fallbackFactory = OrderServiceClientFallbackFactory.class)
public interface OrderServiceClient {

    @RequestMapping(value = "/order/tradeNo", method = RequestMethod.GET)
    OperaResult findOrderListByTradeNo(@RequestParam("appId") String appId, @RequestParam("merchantNo") String merchantNo, @RequestParam("tradeNo") String tradeNo);

    @RequestMapping(value = "/order/outTradeNo", method = RequestMethod.GET)
    OperaResult findOrderListByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo);

    @RequestMapping(value = "/order/payment", method = RequestMethod.PUT)
    OperaResult payment(@RequestBody Order order);

    @RequestMapping(value = "/order/outTradeNo/payment", method = RequestMethod.GET)
    OperaResult findByOutTradeNoAndPaymentNo(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("paymentNo") String paymentNo);

    @RequestMapping(value = "/order/payment/openid/no", method = RequestMethod.GET)
    OperaResult findByPaymentNoAndOpenId(@RequestParam("paymentNo") String paymentNo, @RequestParam("openId") String openId);

    @RequestMapping(value = "/order/outTradeNo/payment", method = RequestMethod.PUT)
    OperaResult paymentByOutTradeNoAndPaymentNo(@RequestBody Order order);

    @RequestMapping(value = "/order/update/orderdetail/status", method = RequestMethod.GET)
    OperaResponse batchUpdateOrderDetailStatusByOrderIds(@RequestParam("orderIdList") List<Integer> orderIdList,
                                                   @RequestParam("status") Integer status);

    @RequestMapping(value = "/order/orderByopenId", method = RequestMethod.GET)
    OperaResult findOrderListByOpenId(@RequestParam("openId") String openId);

    @RequestMapping(value = "/order/send/tradeInfo", method = RequestMethod.GET)
    OperaResponse sendTradeInfo(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("paymentNo") String paymentNo);

}
