package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderMerchantBean implements Serializable {
    private Integer id;
    private String openId;
    private String tradeNo;
    private String aoyiId;
    private String companyCustNo;
    private Integer receiverId;
    private String remark;
    private Integer invoiceId;
    private String payment;
    private Float servFee;
    private Float amount;
    private Integer status;
    private Integer type;
    private String merchantNo;
    private List<OrderDetail> skus;
}
