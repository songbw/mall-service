package com.fengchao.equity.service.impl;

import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.service.CardAndCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardAndCouponServiceImpl implements CardAndCouponService {

    @Autowired
    private CardAndCouponDao dao;

    @Override
    public int createCardAndCoupon(CardAndCoupon bean) {
        return dao.create(bean);
    }

    @Override
    public int updateCardAndCoupon(CardAndCoupon bean) {
        return dao.update(bean);
    }

    @Override
    public int deleteCardAndCoupon(Integer id) {
        CardAndCoupon cardAndCoupon = new CardAndCoupon();
        cardAndCoupon.setId(id);
        cardAndCoupon.setIsDelete((short) 2);
        return dao.update(cardAndCoupon);
    }
}
