package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.AoyiClientService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AoyiClientServiceH implements AoyiClientService {
    @Override
    public OperaResponse order(OrderParamBean orderParamBean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(orderParamBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("添加订单服务失败 ");
        result.setData(orderParamBean);
        return result;
    }

    @Override
    public OperaResponse price(QueryCityPrice queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<InventoryBean> inventory(QueryInventory queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<FreightFareBean> shipCarriage(QueryCarriage queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<List<SubOrderT>> orderGAT(OrderParamBean orderParamBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse preHoldSkuInventory(HoldSkuInventoryQueryBean bean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse releaseSkuInventory(ReleaseSkuInventoryQueryBean bean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }


}
