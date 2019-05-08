package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;

import java.util.HashMap;
import java.util.List;

public interface AoyiProdIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AoyiProdIndexWithBLOBs record);

    int insertSelective(AoyiProdIndexWithBLOBs record);

    AoyiProdIndexWithBLOBs selectByPrimaryKey(Integer id);

    AoyiProdIndexWithBLOBs selectBySkuId(String skuId);

    int updateByPrimaryKeySelective(AoyiProdIndexWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AoyiProdIndexWithBLOBs record);

    int updateByPrimaryKey(AoyiProdIndex record);

    int selectLimitCount(HashMap map) ;

    List<AoyiProdIndex> selectLimit(HashMap map) ;
}