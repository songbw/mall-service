package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.CouponMapper;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponExample;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-19 下午2:08
 */
@Component
public class CouponDao {

    private CouponMapper couponMapper;

    @Autowired
    public CouponDao(CouponMapper couponMapper) {
        this.couponMapper = couponMapper;
    }

    /**
     * 根据id集合查询
     *
     * @param idList
     * @return
     */
    public List<Coupon> selectCouponListByIdList(List<Integer> idList) {
        CouponExample couponExample = new CouponExample();
        CouponExample.Criteria criteria = couponExample.createCriteria();

        criteria.andIdIn(idList);

        List<Coupon> couponList = couponMapper.selectByExample(couponExample);

        return couponList;
    }
}
