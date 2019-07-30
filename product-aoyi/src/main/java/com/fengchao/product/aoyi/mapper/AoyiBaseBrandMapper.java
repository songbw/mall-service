package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.List;

public interface AoyiBaseBrandMapper {
    int deleteByPrimaryKey(Integer brandId);

    int insert(AoyiBaseBrand record);

    int insertSelective(AoyiBaseBrand record);

    AoyiBaseBrand selectByPrimaryKey(Integer brandId);

    int updateByPrimaryKeySelective(AoyiBaseBrand record);

    int updateByPrimaryKeyWithBLOBs(AoyiBaseBrand record);

    int updateByPrimaryKey(AoyiBaseBrand record);

    int selectLimitCount(HashMap map);

    List<AoyiBaseBrand> selectLimit(HashMap map);

    List<AoyiBaseBrand>  selectNameList(HashMap map);
}