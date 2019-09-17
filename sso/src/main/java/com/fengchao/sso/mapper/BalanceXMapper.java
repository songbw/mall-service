package com.fengchao.sso.mapper;

import com.fengchao.sso.model.Balance;

public interface BalanceXMapper {

    Balance selectForUpdateByPrimaryKey(Integer id);
}