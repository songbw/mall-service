package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出
 */
@Setter
@Getter
public class ExportExpressFeeVo {

    /**
     * 商户名称
     */
    private String merchantName;

    private Integer merchantId;

    /**
     * 用户支付运费金额 单位元
     */
    private String userExpressFee;

    /**
     * 供应商运费金额 单位元
     */
    private String merchantExpressFee;


}
