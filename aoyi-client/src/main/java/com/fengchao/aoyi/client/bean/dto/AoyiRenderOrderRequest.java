package com.fengchao.aoyi.client.bean.dto;

import java.util.List;

public class AoyiRenderOrderRequest extends BaseRquestParam {

    private String orderNo;

    /**
     * 订单总金额(不 含运费) 单位元
     */
    private String amount;

    /**
     * 运费 单位元
     */
    private String freight;

    /**
     * 商品信息
     */
    private List<AoyiItemRequest> items;

    private AoyiDeliverAddressRequest deliveryAddress;
}
