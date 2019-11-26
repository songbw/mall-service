package com.fengchao.sso.mapper;

import com.fengchao.sso.bean.BalanceQueryBean;
import com.fengchao.sso.bean.BalanceSumBean;
import com.fengchao.sso.model.Balance;

import java.util.List;

public interface BalanceXMapper {

    Balance selectForUpdateByPrimaryKey(Integer id);

    int batchUpdate(Balance balance);

    List<BalanceSumBean> selectInitAmount(BalanceQueryBean queryBean) ;

    int selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(BalanceQueryBean queryBean) ;

    List<BalanceSumBean> selectChargeList(BalanceQueryBean queryBean) ;
}