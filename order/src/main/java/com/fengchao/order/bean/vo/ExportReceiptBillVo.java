package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出发票
 */
@Setter
@Getter
public class ExportReceiptBillVo {

    private String mpu;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 品类名称
     */
    private String category;

    /**
     * 销售单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 含税单价 单位分
     */
    private Integer unitPrice;

    /**
     * 含税总额 单位分
     */
    private Integer totalPrice;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 税额 单位分
     */
    private Integer taxPrice;
}
