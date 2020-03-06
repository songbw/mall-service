package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.service.CardAndCouponService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<CardAndCoupon>
    getCouponIdList(Integer cardId){

        return dao.selectCouponIdByCardId(cardId);

    }
}
