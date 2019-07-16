package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.OrderServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order", fallback = OrderServiceH.class)
public interface OrderService {

    @RequestMapping(value = "/order/statistics", method = RequestMethod.GET)
    OperaResult statistics(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/order/payment/count", method = RequestMethod.GET)
    OperaResult paymentCount(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/order/payment/promotion/count", method = RequestMethod.GET)
    OperaResult paymentPromotionCount(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/order/payment/merchant/count", method = RequestMethod.GET)
    OperaResult paymentMerchantCount(@RequestParam("start") String start, @RequestParam("end") String end);


}
