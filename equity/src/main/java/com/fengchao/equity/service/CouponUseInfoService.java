package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.CouponUseInfoX;
import java.util.List;

public interface CouponUseInfoService {

    CouponUseInfoBean collectCoupon(CouponUseInfoBean bean);

    PageBean findCollect(CouponUseInfoBean bean);

    int getCouponNum(CouponUseInfoBean bean);

    CouponUserResultBean selectCouponByOpenId(CouponUseInfoBean bean);

    int batchCode(CouponUseInfoBean bean);

    int importCode(CouponUseInfoBean bean);

    CouponUseInfoBean redemption(CouponUseInfoBean bean);

    CouponBean selectCouponByEquityId(CouponUseInfoBean bean);

    int deleteUserCoupon(CouponUseInfoBean bean);

    int releaseCoupon(CouponUseInfoBean bean);

    int occupyCoupon(CouponUseInfoBean bean);

    CouponUseInfoX findById(CouponUseInfoBean bean);

    /**
     * 根据id查询coupon_use_info集合
     *
     * @param idList
     * @return
     * @throws Exception
     */
    List<CouponUseInfoBean> queryByIdList(List<Integer> idList) throws Exception;

    int verifyCoupon(CouponUseInfoBean bean);
}
