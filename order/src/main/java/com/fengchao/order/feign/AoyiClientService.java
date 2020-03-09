package com.fengchao.order.feign;

import com.fengchao.order.bean.*;
import com.fengchao.order.feign.hystric.AoyiClientServiceClientFallbackFactory;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiConfirmOrderRequest;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiRenderOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "aoyi-client", url = "${rpc.feign.client.aoyiclient.url:}", fallbackFactory = AoyiClientServiceClientFallbackFactory.class)
public interface AoyiClientService {

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    OperaResponse<List<SubOrderT>> order(@RequestBody OrderParamBean orderParamBean);

    @RequestMapping(value = "/product/price", method = RequestMethod.POST)
    OperaResponse<List<PriceBean>> price(@RequestBody QueryCityPrice queryBean);

    @RequestMapping(value = "/product/inventory", method = RequestMethod.POST)
    OperaResponse<InventoryBean> inventory(@RequestBody QueryInventory queryBean);

    @RequestMapping(value = "/product/carriage", method = RequestMethod.POST)
    OperaResponse<FreightFareBean> shipCarriage(@RequestBody QueryCarriage queryBean);

    @RequestMapping(value = "/order/gat", method = RequestMethod.POST)
    OperaResponse<List<SubOrderT>> orderGAT(@RequestBody OrderParamBean orderParamBean);

    @RequestMapping(value = "/star/product/inventory/hold", method = RequestMethod.POST)
    OperaResponse preHoldSkuInventory(@RequestBody HoldSkuInventoryQueryBean bean);

    @RequestMapping(value = "/star/product/inventory/release", method = RequestMethod.POST)
    OperaResponse releaseSkuInventory(@RequestBody ReleaseSkuInventoryQueryBean bean);

    @RequestMapping(value = "/star/orders", method = RequestMethod.POST)
    OperaResponse addOrder(@RequestBody StarOrderBean bean);

    // 唯品会相关 begin

    /**
     * 库存查询接口
     *
     * @param itemId
     * @param skuId
     * @param num          数量
     * @param divisionCode 地址code
     * @return
     */
    @RequestMapping(value = "/weipinhui/queryItemInventory", method = RequestMethod.GET)
    OperaResponse<AoyiQueryInventoryResDto> queryItemInventory(@RequestParam("itemId") String itemId,
                                                                    @RequestParam("skuId") String skuId,
                                                                    @RequestParam("num") Integer num,
                                                                    @RequestParam("divisionCode") String divisionCode);

    /**
     * 预下单
     *
     * @param aoyiRenderOrderRequest
     * @return
     */
    @RequestMapping(value = "/weipinhui/renderOrder", method = RequestMethod.POST)
    OperaResponse weipinhuiRenderOrder(@RequestBody AoyiRenderOrderRequest aoyiRenderOrderRequest);

    /**
     * 创建订单
     *
     * @param aoyiConfirmOrderRequest
     * @return
     */
    @RequestMapping(value = "/weipinhui/createOrder", method = RequestMethod.POST)
    OperaResponse weipinhuiCreateOrder(@RequestBody AoyiConfirmOrderRequest aoyiConfirmOrderRequest);
    // 唯品会相关 end

}
