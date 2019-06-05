package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.ProdExtend;

public interface ProdExtendMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProdExtend record);

    int insertSelective(ProdExtend record);

    ProdExtend selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProdExtend record);

    int updateByPrimaryKey(ProdExtend record);
}