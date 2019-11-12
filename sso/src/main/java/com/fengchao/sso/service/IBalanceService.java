package com.fengchao.sso.service;

import com.fengchao.sso.bean.BalanceDetailQueryBean;
import com.fengchao.sso.bean.BalanceQueryBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.model.BalanceDetail;

public interface IBalanceService {

    OperaResponse add(Balance bean) ;

    OperaResponse update(Balance bean) ;

    OperaResponse findByOpenId(String openId) ;

    Integer delete(Integer id) ;

    OperaResponse findList(BalanceQueryBean bean) ;

    OperaResponse findDetailList(BalanceDetailQueryBean bean) ;

    OperaResponse consume(BalanceDetail bean) ;

    OperaResponse refund(BalanceDetail bean) ;

    OperaResponse updateDetailStatus(BalanceDetail bean) ;

    OperaResponse init(Balance bean) ;

}
