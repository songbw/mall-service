package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BillExportReqVo {

    /**
     * 查询开始日期（支付开始日期）
     */
    private String startDate;

    /**
     * 查询结束日期（支付结束日期）
     */
    private String endDate;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 交易类型
     */
    private String tradeType;

    private Integer pageSize = 50;

    private Integer pageNum = 1;

}
