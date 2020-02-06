package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseBrandX;

import java.util.HashMap;
import java.util.List;

public interface AoyiBaseBrandXMapper {
    int deleteByPrimaryKey(Integer brandId);

    int insert(AoyiBaseBrandX record);

    int insertSelective(AoyiBaseBrandX record);

    AoyiBaseBrandX selectByPrimaryKey(Integer brandId);

    int updateByPrimaryKeySelective(AoyiBaseBrandX record);

    int updateByPrimaryKeyWithBLOBs(AoyiBaseBrandX record);

    int updateByPrimaryKey(AoyiBaseBrandX record);

    int selectLimitCount(HashMap map);

    List<AoyiBaseBrandX> selectLimit(HashMap map);

    List<AoyiBaseBrandX>  selectNameList(HashMap map);

    List<AoyiBaseBrandX>  selectByBrandIdList(List<Integer> brandIds);
}