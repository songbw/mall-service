package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.CouponUseInfoX;
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

    CouponUseInfoX consumeCoupon(CouponUseInfoBean bean);

    List<CouponBean> selectCouponByMpu(AoyiProdBean bean);

    int effective(int couponId);

    int end(int couponId);

    /**
     * 根据coupon id集合查询 coupon列表
     *
     * @param idList
     * @return
     */
    List<CouponBean> queryCouponBeanListIdList(List<Integer> idList) throws Exception;

    int invalid(int couponId);

    CouponUseInfoX adminConsumeCoupon(CouponUseInfoBean bean);

    List<CouponBean> giftCoupon();
}
