package com.fengchao.equity.service;

import com.fengchao.equity.model.CardAndCoupon;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CardAndCouponService {

    int createCardAndCoupon(CardAndCoupon bean);

    int updateCardAndCoupon(CardAndCoupon bean);

    int deleteCardAndCoupon(Integer id);

    List<CardAndCoupon>
    getCouponIdList(Integer cardId);

}
