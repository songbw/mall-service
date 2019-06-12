package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderParamBean implements Serializable {
    private String openId;
    private String companyCustNo;
    private Integer receiverId;
    private String remark;
    private Integer invoiceId;
    private String payment;
    private List<OrderMerchantBean> merchants;
    private String invoiceType; //电子普票
    private String invoiceTitleName;//企业名称
    private String invoiceEnterpriseNumber;//企业纳税号
}
