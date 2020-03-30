package com.fengchao.order.feign;

import com.fengchao.order.bean.*;
import com.fengchao.order.feign.hystric.EquityServiceClientH;
import com.fengchao.order.feign.hystric.FreightsServiceClientFallbackFactory;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import com.fengchao.order.rpc.extmodel.ShipTemplateBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "freight", url = "${rpc.feign.client.freight.url:}", fallbackFactory = FreightsServiceClientFallbackFactory.class)
public interface FreightsServiceClient {

    /**
     * 查询运费
     *
     * @param merchantIdList
     * @return
     */
    @RequestMapping(value = "/freights/adminShip/findIdList", method = RequestMethod.GET)
    OperaResponse<List<ShipTemplateBean>> queryMerchantExceptionFee(@RequestParam List<Integer> merchantIdList);

}
