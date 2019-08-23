package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.SkuCode;

import java.util.List;

public interface SkuCodeXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SkuCode record);

    int insertSelective(SkuCode record);

    SkuCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SkuCode record);

    int updateByPrimaryKey(SkuCode record);

    int updateSkuValueByPrimaryKey(SkuCode record);

    SkuCode selectByMerchantId(Integer merchantId) ;

    SkuCode selectLast() ;

    List<SkuCode> selectList() ;
}