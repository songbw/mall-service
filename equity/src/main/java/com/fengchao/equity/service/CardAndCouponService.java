package com.fengchao.equity.service;

import com.fengchao.equity.model.CardAndCoupon;

public interface CardAndCouponService {

    int createCardAndCoupon(CardAndCoupon bean);

    int updateCardAndCoupon(CardAndCoupon bean);

    int deleteCardAndCoupon(Integer id);
}
