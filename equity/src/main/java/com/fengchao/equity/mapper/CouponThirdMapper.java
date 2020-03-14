package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CouponThird;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CouponThirdMapper {
    int insert(CouponThird record);

    int insertSelective(CouponThird record);

    CouponThird selectByUserCouponId(Integer couponUserId);
}
