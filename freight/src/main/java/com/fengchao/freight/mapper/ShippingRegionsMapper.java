package com.fengchao.freight.mapper;

import com.fengchao.freight.model.ShippingRegions;
import com.fengchao.freight.model.ShippingRegionsExample;
import java.util.List;

import com.fengchao.freight.model.ShippingRegionsX;
import org.apache.ibatis.annotations.Param;

public interface ShippingRegionsMapper {
    long countByExample(ShippingRegionsExample example);

    int deleteByExample(ShippingRegionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShippingRegions record);

    int insertSelective(ShippingRegions record);

    List<ShippingRegions> selectByExample(ShippingRegionsExample example);

    ShippingRegions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShippingRegions record, @Param("example") ShippingRegionsExample example);

    int updateByExample(@Param("record") ShippingRegions record, @Param("example") ShippingRegionsExample example);

    int updateByPrimaryKeySelective(ShippingRegions record);

    int updateByPrimaryKey(ShippingRegions record);
}