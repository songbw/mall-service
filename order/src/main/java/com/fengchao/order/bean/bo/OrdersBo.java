package com.fengchao.order.bean.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午2:22
 */
@Getter
@Setter
public class OrdersBo {

    private Integer id;

    private String openId;

    private String appId;

    private String tradeNo;

    private String aoyiId;

    private Integer merchantId;

    private String merchantNo;

    private String couponCode;

    private Float couponDiscount;

    private Integer couponId;

    private String companyCustNo;

    private String receiverName;

    private String telephone;

    private String mobile;

    private String email;

    private String provinceName;

    private String provinceId;

    private String cityName;

    private String cityId;

    private String countyName;

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

    private Float saleAmount;

    private Float amount;

    private Integer status;

    private Integer type;

    private String outTradeNo;

    private String paymentNo;

    private Date paymentAt;

    private Integer paymentAmount;

    private String payee;

    private String payType;

    private Integer paymentTotalFee;

    private String payer;

    private Integer payStatus;

    private Integer payOrderCategory;

    private Integer refundFee;

    private Date createdAt;

    private Date updatedAt;

    /**
     * 订单详情
     */
    private List<OrderDetailBo> orderDetailBoList;

    /**
     * 该主订单下所有子订单的sale_price*num之和  单位 分
     */
    private Integer totalSalePrice;
}
