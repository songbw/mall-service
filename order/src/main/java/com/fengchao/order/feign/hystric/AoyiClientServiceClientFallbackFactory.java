package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiConfirmOrderRequest;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiRenderOrderRequest;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class AoyiClientServiceClientFallbackFactory implements FallbackFactory<AoyiClientService> {

    @Override
    public AoyiClientService create(Throwable throwable) {
        return new AoyiClientService() {
            @Override
            public OperaResponse<List<SubOrderT>> order(OrderParamBean orderParamBean) {
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
            public OperaResponse<List<PriceBean>> price(QueryCityPrice queryBean) {
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

            @Override
            public OperaResponse addOrder(StarOrderBean bean) {
                return HystrixDefaultFallback.defaultReponseFallback();
            }

            @Override
            public OperaResponse weipinhuiRenderOrder(AoyiRenderOrderRequest aoyiRenderOrderRequest) {
                return HystrixDefaultFallback.defaultReponseFallback();
            }

            @Override
            public OperaResponse weipinhuiCreateOrder(AoyiConfirmOrderRequest aoyiConfirmOrderRequest) {
                return HystrixDefaultFallback.defaultReponseFallback();
            }
        };
    }

}
