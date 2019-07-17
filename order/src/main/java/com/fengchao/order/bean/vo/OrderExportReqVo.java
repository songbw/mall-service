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

    private String phoneNo;

    private Integer orderStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payStartDate;

    private Date payEndDate;
}
