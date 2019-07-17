package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.MerchantOverview;

import java.util.HashMap;

public interface MerchantOverviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MerchantOverview record);

    int insertSelective(MerchantOverview record);

    MerchantOverview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MerchantOverview record);

    int updateByPrimaryKey(MerchantOverview record);

    MerchantOverview selectSum(HashMap map) ;
}