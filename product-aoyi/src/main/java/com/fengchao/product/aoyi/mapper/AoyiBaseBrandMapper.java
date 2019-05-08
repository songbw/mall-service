package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseBrand;

public interface AoyiBaseBrandMapper {
    int deleteByPrimaryKey(Integer brandId);

    int insert(AoyiBaseBrand record);

    int insertSelective(AoyiBaseBrand record);

    AoyiBaseBrand selectByPrimaryKey(Integer brandId);

    int updateByPrimaryKeySelective(AoyiBaseBrand record);

    int updateByPrimaryKeyWithBLOBs(AoyiBaseBrand record);

    int updateByPrimaryKey(AoyiBaseBrand record);
}