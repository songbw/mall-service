package com.fengchao.equity.service;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.ThirdResult;
import com.fengchao.equity.bean.ToushiResult;
import com.fengchao.equity.exception.EquityException;

public interface CouponThirdService {

    OperaResult consumedToushi(ToushiResult bean) throws EquityException;

    OperaResult obtainCoupon(ToushiResult bean) throws EquityException;

    OperaResult userVerified(ToushiResult bean);

    OperaResult equityCreate(ThirdResult bean);
}
