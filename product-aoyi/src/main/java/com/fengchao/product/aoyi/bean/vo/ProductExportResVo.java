package com.fengchao.product.aoyi.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出商品返回vo
 *
 * @Author tom
 * @Date 19-8-29 上午10:49
 */
@Setter
@Getter
public class ProductExportResVo {

    /**
     * 商品供应商
     */
    private String merchantName;

    /**
     * 商品SKU
     */
    private String sku;

    /**
     * 商品状态
     */
    private int state;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品类别
     */
    private String category;

    /**
     * 商品型号
     */
    private String model;

    /**
     * 商品重量
     */
    private String weight;

    /**
     * 商品条形码
     */
    private String upc;

    /**
     * 销售价格(元)
     */
    private String sellPrice;

    /**
     * 进货价格(元)  成本价格
     */
    private String costPrice;

}
