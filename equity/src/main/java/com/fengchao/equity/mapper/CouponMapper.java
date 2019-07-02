package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.AoyiProdBean;
import com.fengchao.equity.bean.CouponResultBean;
//import com.fengchao.equity.model.AoyiProdIndex;
import com.fengchao.equity.model.Coupon;

import java.util.HashMap;
import java.util.List;

public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    int selectCount(HashMap map);

    List<CouponResultBean> selectLimit(HashMap map);

    List<Coupon> selectActiveCouponLimit(HashMap map);

    int selectActiveCouponCount(HashMap map);

    Coupon selectByCodeKey(String code);

    List<String> selectActiveCategories();

    List<String> selectTags();

    List<Coupon> selectCouponBySku(AoyiProdBean aoyiProdBean);

    int couponEffective(int couponId);

    int couponEnd(int couponId);
}