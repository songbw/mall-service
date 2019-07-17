package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class OrderExportReqVo {

    private String tradeNo;

    private String subOrderId;

    /**
     * 收货人电话
     */
    private String phoneNo;

    private Integer orderStatus;

    /**
     * 商家id
     */
    private Integer merchantId;

    /**
     * 查询开始日期（支付开始日期）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payStartDate;

    /**
     * 查询结束日期（支付结束日期）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payEndDate;
}
