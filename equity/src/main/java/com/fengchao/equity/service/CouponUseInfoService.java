package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.model.CouponUseInfo;

public interface CouponUseInfoService {

    CouponUseInfoBean collectCoupon(CouponUseInfoBean bean);

    PageBean findCollect(CouponUseInfoBean bean);

    int getCouponNum(CouponUseInfoBean bean);

    PageBean selectCouponByOpenId(CouponUseInfoBean bean);

    int batchCode(CouponUseInfoBean bean);

    int importCode(CouponUseInfoBean bean);

    CouponUseInfoBean redemption(CouponUseInfoBean bean);

    CouponBean selectCouponByEquityId(CouponUseInfoBean bean);

    int deleteUserCoupon(CouponUseInfoBean bean);

    CouponUseInfo findById(CouponUseInfoBean bean);

    OperaResult consumedToushi(ToushiResult bean) throws EquityException;

    OperaResult obtainCoupon(ToushiResult bean) throws EquityException;

    OperaResult userVerified(ToushiResult bean);
}
