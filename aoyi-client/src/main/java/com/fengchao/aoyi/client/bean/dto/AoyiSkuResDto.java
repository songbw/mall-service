package com.fengchao.aoyi.client.bean.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiSkuResDto {

    private String skuId; // 500005500

    /**
     * 商品规格 1
     */
    private String sepca; // "白色"

    /**
     * 商品规格 2
     */
    private String sepcb; // "12kg",

    /**
     * 商品规格 3
     */
    private String sepcc;

    /**
     * 商品成本价
     */
    private String priceCent; // "159.00",

    /**
     * 建议售价
     */
    private String sellPrice;

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
