package com.fengchao.equity.bean;


import lombok.Data;

import java.util.List;

@Data
public class CouponUserResultBean extends PageBean{
    private List<CouponBean> grantCoupons;
}
