package com.fengchao.order.bean;

import com.fengchao.order.model.OrderDetailX;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private String completeDateStart;
    private String completeDateEnd;
    private String appId;
    private String receiverName;
    private Integer merchantHeader;
    private String renterHeader;
    private List<Integer> merchantIds ;
    private String renterId ;
}
