package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponUseInfoX;

import java.util.List;

public interface CouponService {

    int createCoupon(CouponBean bean);

    PageBean findCoupon(Integer offset, Integer limit, String appId);

    PageBean serachCoupon(CouponSearchBean bean);

    int updateCoupon(CouponBean bean);

    int deleteCoupon(Integer id);

    CouponBean findByCouponId(Integer id);

    PageBean activeCoupon(CouponUseInfoBean useInfoBean);

    CategoryCouponBean activeCategories(String appId);

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

    List<Object> giftCoupon(String openId, String iAppId);

    PageableData<Coupon> findReleaseCoupon(Integer pageNo, Integer pageSize, String appId, Integer couponType);

    List<CouponBean> findCouponListByIdList(List<Integer> ids, String openId, String appId);

    List<CouponAndPromBean> findCouponListByMpuList(List<AoyiProdBean> beans, String appId);


}
