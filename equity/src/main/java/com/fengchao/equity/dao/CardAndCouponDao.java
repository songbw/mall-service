package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.CardAndCouponMapper;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.model.CardAndCouponExample;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.utils.CardAndCouonIsDeleteEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardAndCouponDao {

    @Autowired
    private CardAndCouponMapper mapper;

    public int create(CardAndCoupon cardAndCoupon) {
        return mapper.insertSelective(cardAndCoupon);
    }

    public int update(CardAndCoupon cardAndCoupon) {
        return mapper.updateByPrimaryKeySelective(cardAndCoupon);
    }

    public int updateCardAndCoupon(CardAndCoupon cardAndCoupon) {
        CardAndCouponExample example = new CardAndCouponExample();
        CardAndCouponExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(cardAndCoupon.getCardId());
        return mapper.updateByExampleSelective(cardAndCoupon, example);
    }

    public List<CardAndCoupon> findCouponIdByCardId(Integer id) {
        CardAndCouponExample example = new CardAndCouponExample();
        CardAndCouponExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(id);
        criteria.andIsDeleteEqualTo((short) 1);

        return mapper.selectByExample(example);
    }

    public List<CardAndCoupon>
    selectCouponIdByCardId(Integer cardId){

        CardAndCouponExample example = new CardAndCouponExample();
        CardAndCouponExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short)CardAndCouonIsDeleteEnum.VALID.getCode());
        criteria.andCardIdEqualTo(cardId);

        return mapper.selectByExample(example);


    }
}
