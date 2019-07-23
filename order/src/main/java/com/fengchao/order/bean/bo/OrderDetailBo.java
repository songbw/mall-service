package com.fengchao.order.bean.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author tom
 * @Date 19-7-18 下午2:22
 */
@Setter
@Getter
public class OrderDetailBo {

    private Integer id;

    private Integer merchantId;

    private Integer orderId;

    private String subOrderId;

    private String mpu;

    private String skuId;

    private Integer num;

    private Integer promotionId;

    private Float promotionDiscount;

    private BigDecimal salePrice;

    private BigDecimal unitPrice;

    private String image;

    private String model;

    private String name;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String logisticsId;

    private String logisticsContent;

    private String comcode;

    private Integer skuCouponDiscount;
}
