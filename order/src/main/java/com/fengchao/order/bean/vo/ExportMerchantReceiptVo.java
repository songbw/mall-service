package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExportMerchantReceiptVo {

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 品类
     */
    private String categoryId;

    private String categoryName;

    /**
     * 销售单位
     */
    private String saleUnit;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 进货单价
     */
    private String sprice;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 含税金额
     */
    private String amount;

    /**
     * 税额
     */
    private String taxAmount;

    /**
     * 税收类型
     */
    private String taxType;

}
