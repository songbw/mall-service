package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CouponUseInfoBean {

    private String userOpenId;
    private String code;
    private String userCouponCode;
    private Integer id;
    private Integer status;
    private String collectedStartDate;
    private String collectedEndDate;
    private String consumedStartDate;
    private String consumedEndDate;
    private Integer couponCollectNum;
    private Integer couponId;
    private String categoryId;
    private String categoryName;
    private Integer tagId;
    private String tagName;
    private String supplierMerchantId;
    private String couponCode;

    /**
     * 券来源
     */
    private String supplierMerchantName;

}
