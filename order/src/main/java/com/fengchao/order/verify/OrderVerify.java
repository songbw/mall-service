package com.fengchao.order.verify;

import com.fengchao.order.bean.*;
import com.fengchao.order.feign.AoyiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author song
 * @Date 19-8-5 上午11:51
 */
@Component
@Slf4j
public class OrderVerify {

    @Autowired
    private AoyiClientService aoyiClientService ;

    /**
     * 验证订单中的商品价格
     * @param orderBean
     * @return
     */
    public OperaResult priceVerify(OrderParamBean orderBean) {
        QueryCityPrice queryBean = new QueryCityPrice();
        queryBean.setCityId(orderBean.getCityId());
        OperaResult operaResult = new OperaResult();
        List<PriceSkus> skus = new ArrayList<>() ;
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        // 只验证奥义价格
        orderMerchantBeans.removeIf(merchant -> (merchant.getMerchantId() != 2));
        // 批量添加SKU
        orderMerchantBeans.forEach(orderMerchantBean -> {
            orderMerchantBean.getSkus().forEach(sku -> {
                PriceSkus priceSkus = new PriceSkus();
                priceSkus.setSkuId(sku.getSkuId());
                skus.add(priceSkus) ;
            });
        });
        // 获取SKU对应的价格列表
        OperaResponse<List<PriceBean>> operaResponse = aoyiClientService.price(queryBean) ;
        if (operaResponse.getCode() == 200) {
            List<PriceBean> priceBeans = operaResponse.getData();
            orderMerchantBeans.forEach(orderMerchantBean -> {
                orderMerchantBean.getSkus().forEach(sku -> {
                    priceBeans.forEach(priceBean -> {
                        if (priceBean.getSkuId().equals(sku.getSkuId())) {
                            if (!sku.getCheckedPrice().toString().equals(priceBean.getPrice())) {
                                operaResult.setCode(operaResponse.getCode());
                                operaResult.setMsg(operaResponse.getMsg());
                            }
                        }
                    }) ;
                });
            });
        } else {
            operaResult.setCode(operaResponse.getCode());
            operaResult.setMsg(operaResponse.getMsg());
            return operaResult;
        }
        return operaResult;
    }

    /**
     * 验证订单中的商品库存
     * @param queryBean
     * @return
     */
    public OperaResult inventoryVerify(InventoryQueryBean queryBean) {
        OperaResult operaResult = new OperaResult();

        return operaResult;
    }

    /**
     * 验证优惠券有效性
     * @param coupon
     * @return
     */
    public OperaResult couponVerify( OrderCouponBean coupon) {
        OperaResult operaResult = new OperaResult();

        return operaResult;
    }

    public OperaResult promotionVerify( OrderCouponBean coupon) {
        OperaResult operaResult = new OperaResult();

        return operaResult;
    }

}
