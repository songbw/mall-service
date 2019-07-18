package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.CouponX;

import java.util.List;

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

    CouponX consumeCoupon(CouponUseInfoBean bean);

    List<CouponBean> selectCouponByMpu(AoyiProdBean bean);

    int effective(int couponId);

    int end(int couponId);
}
