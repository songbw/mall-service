package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CouponUseInfoBean  extends QueryBean{

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
    private List<String> userCouponCodes;
    private Integer couponId;
    private String categoryId;
    private String categoryName;
    private Integer tagId;
    private String tagName;
    private String supplierMerchantId;
    private String couponCode;
    private String appId;
    private Integer orderId;

    /**
     * 券来源
     */
    private String supplierMerchantName;

}
