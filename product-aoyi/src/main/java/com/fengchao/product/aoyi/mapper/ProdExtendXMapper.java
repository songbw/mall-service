package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.ProdExtendX;

public interface ProdExtendXMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProdExtendX record);

    int insertSelective(ProdExtendX record);

    ProdExtendX selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProdExtendX record);

    int updateByPrimaryKey(ProdExtendX record);
}