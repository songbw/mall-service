package com.fengchao.order.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailBean {

    private Integer id;
    private String openId;
    private String subOrderId;
    private String tradeNo;
    private String aoyiId;
    private String companyCustNo;
    private Integer merchantId;
    private String merchantNo;
    private String receiverName;
    private String telephone;
    private String mobile;
    private String email;
    private String provinceId;
    private String cityId;
    private String countyId;
    private String townId;
    private String address;
    private String zip;
    private String remark;
    private String invoiceState;
    private String invoiceType;
    private String invoiceTitle;
    private String invoiceContent;
    private String payment;
    private Float servFee;
    private Float amount;
    private Integer status;
    private Integer type;
    private Date createdAt;
    private Date updatedAt;
    private String skuId;
    private Integer num;
    private BigDecimal unitPrice;
    private String image;
    private String model;
    private String name;
    private String paymentNo;
    private Date paymentAt;
    private String logisticsId;
    private String logisticsContent;
}
