package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CouponThird;

public interface CouponThirdMapper {
    int insert(CouponThird record);

    int insertSelective(CouponThird record);

    CouponThird selectByUserCouponId(Integer couponUserId);
}