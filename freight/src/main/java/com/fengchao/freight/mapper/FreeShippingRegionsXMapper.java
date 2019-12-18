package com.fengchao.freight.mapper;

import com.fengchao.freight.model.FreeShippingRegionsExample;
import com.fengchao.freight.model.FreeShippingRegionsX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreeShippingRegionsXMapper {
    long countByExample(FreeShippingRegionsExample example);

    int deleteByExample(FreeShippingRegionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreeShippingRegionsX record);

    int insertSelective(FreeShippingRegionsX record);

    List<FreeShippingRegionsX> selectByExample(FreeShippingRegionsExample example);

    FreeShippingRegionsX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreeShippingRegionsX record, @Param("example") FreeShippingRegionsExample example);

    int updateByExample(@Param("record") FreeShippingRegionsX record, @Param("example") FreeShippingRegionsExample example);

    int updateByPrimaryKeySelective(FreeShippingRegionsX record);

    int updateByPrimaryKey(FreeShippingRegionsX record);

    List<FreeShippingRegionsX> selectByTemplateId(Integer id);

    int deleteByTemplateId(Integer templateId);

    FreeShippingRegionsX selectByProvinceId(@Param("provinceId") String provinceId, @Param("templateId")Integer templateId);

    FreeShippingRegionsX selectDefaltShipRegions(Integer templateId);
}