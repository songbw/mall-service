package com.fengchao.freight.mapper;

import com.fengchao.freight.model.FreeShippingRegions;
import com.fengchao.freight.model.FreeShippingRegionsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreeShippingRegionsXMapper {
    long countByExample(FreeShippingRegionsExample example);

    int deleteByExample(FreeShippingRegionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreeShippingRegions record);

    int insertSelective(FreeShippingRegions record);

    List<FreeShippingRegions> selectByExample(FreeShippingRegionsExample example);

    FreeShippingRegions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreeShippingRegions record, @Param("example") FreeShippingRegionsExample example);

    int updateByExample(@Param("record") FreeShippingRegions record, @Param("example") FreeShippingRegionsExample example);

    int updateByPrimaryKeySelective(FreeShippingRegions record);

    int updateByPrimaryKey(FreeShippingRegions record);
}