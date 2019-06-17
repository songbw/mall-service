package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.CategoryCouponBean;
import com.fengchao.equity.bean.CouponBean;
import com.fengchao.equity.bean.CouponSearchBean;
import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.model.Coupon;

public interface CouponService {

    int createCoupon(CouponBean bean);

    PageBean findCoupon(Integer offset, Integer limit);

    PageBean serachCoupon(CouponSearchBean bean);

    int updateCoupon(CouponBean bean);

    int deleteCoupon(Integer id);

    CouponBean findByCouponId(Integer id);

    PageBean activeCoupon(CouponUseInfoBean useInfoBean);

    CategoryCouponBean activeCategories();

    CouponBean selectSkuByCouponId(CouponUseInfoBean bean);

    Coupon consumeCoupon(CouponUseInfoBean bean);
}
