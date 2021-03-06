package com.fengchao.order.bean;

import com.fengchao.order.model.OrderDetailX;
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
    private Float saleAmount;
    private Integer status;
    private Integer type;
    private String merchantNo;
    private Integer merchantId;
    private List<OrderDetailX> skus;
}
