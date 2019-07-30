package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.OrderServiceClientH;
import com.fengchao.statistics.rpc.extmodel.DayStatisticsBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order", url = "${rpc.feign.client.order.url:}", fallback = OrderServiceClientH.class)
public interface OrderServiceClient {

    @RequestMapping(value = "/order/statistics", method = RequestMethod.GET)
    OperaResponse<DayStatisticsBean> statistics();

    @RequestMapping(value = "/order/payment/count", method = RequestMethod.GET)
    OperaResult paymentCount(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/order/payment/promotion/count", method = RequestMethod.GET)
    OperaResult paymentPromotionCount(@RequestParam("start") String start, @RequestParam("end") String end);

//    @RequestMapping(value = "/order/payment/merchant/count", method = RequestMethod.GET)
//    OperaResult paymentMerchantCount(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/order/orderdetail/payed/list", method = RequestMethod.GET)
    OperaResult queryPayedOrderDetailList(@RequestParam("start") String start, @RequestParam("end") String end);


}
