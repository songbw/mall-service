package com.fengchao.aoyi.client.bean.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiSkuResDto {

    private String skuId; // 500005500

    /**
     * 商品 sku 名称
     */
    private String skuTitle; // "花色"

    /**
     * 商品成本价
     */
    private String priceCent; // ": "161",

    /**
     * 市场价格
     */
    private String reservePrice; // "159.00",

    /**
     * 是否出售
     */
    private String canSell; // "false"

    /**
     * sku 商品图片
     */
    private String skuImageUrl;

    /**
     * sku 主图
     */
    private String skuImage;

    /**
     * sku 详情图
     */
    private String skuDetailImage;
}
