package com.fengchao.equity.bean;

import lombok.Data;

import java.util.List;

@Data
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

}
