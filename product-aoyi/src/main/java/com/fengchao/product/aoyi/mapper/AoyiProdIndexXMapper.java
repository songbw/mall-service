package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.bean.QueryProdBean;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;

import java.util.HashMap;
import java.util.List;

public interface AoyiProdIndexXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AoyiProdIndexWithBLOBs record);

    int insertSelective(AoyiProdIndex record);

    AoyiProdIndexWithBLOBs selectByPrimaryKey(Integer id);

    AoyiProdIndex selectBySkuId(String skuId);

    AoyiProdIndex selectByMpu(String sku);

    int updateByPrimaryKeySelective(AoyiProdIndex record);

    int updateByPrimaryKeyWithBLOBs(AoyiProdIndexWithBLOBs record);

    int updateByPrimaryKey(AoyiProdIndex record);

    int selectLimitCount(HashMap map) ;

    List<AoyiProdIndex> selectLimit(HashMap map) ;

    List<AoyiProdIndex> selectAll(HashMap map) ;

    List<AoyiProdIndex> selectSearchLimit(HashMap map) ;

    int selectSearchCount(HashMap map) ;

    List<AoyiProdIndex> selectProdAll();

    int selectSkuByCouponIdCount(QueryProdBean bean);

    List<AoyiProdIndex> selectSkuByCouponIdLimit(QueryProdBean bean);
}