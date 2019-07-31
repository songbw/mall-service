package com.fengchao.order.bean;

import com.fengchao.order.model.OrderDetailX;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderBean implements Serializable {

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
    private List<OrderDetailX> skus;
    private Integer pageIndex;
    private Integer pageSize;
    private String telephone;
    private String mobile;
    private String deliveryStatus;
    private String subOrderId;
    private String deliveryId;
    private String payDateStart;
    private String payDateEnd;
    private Integer merchantId;
    private Integer subStatus;
}
