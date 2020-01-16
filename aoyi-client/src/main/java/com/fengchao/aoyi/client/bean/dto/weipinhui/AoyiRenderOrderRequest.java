package com.fengchao.aoyi.client.bean.dto.weipinhui;

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
    private List<AoyiItem> items;

    /**
     * 订单地址信息
     */
    private AoyiDeliverAddress deliveryAddress;
}
