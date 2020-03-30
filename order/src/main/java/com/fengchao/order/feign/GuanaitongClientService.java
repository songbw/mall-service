package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.SendTradeInfoBean;
import com.fengchao.order.feign.hystric.GuanaitongClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "guanaitong-client", fallback = GuanaitongClientServiceH.class)
public interface GuanaitongClientService {

    @RequestMapping(value = "/seller/payV2/transfer_trade_info", method = RequestMethod.POST)
    OperaResponse sendTradeInfo(@RequestBody SendTradeInfoBean bean);
}
