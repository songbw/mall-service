package com.fengchao.order.rpc.extmodel.weipinhui;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiItem {

    /**
     * 子订单号
     */
    private String subOrderNo;

    private String itemId;

    private String skuId;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 商品销售单价
     */
    private String prodPrice;

    /**
     * 子订单金额
     */
    private String subAmount;
}
