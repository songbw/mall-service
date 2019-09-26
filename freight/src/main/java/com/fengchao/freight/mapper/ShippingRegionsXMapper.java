package com.fengchao.freight.mapper;

import com.fengchao.freight.model.FreeShippingRegionsX;
import com.fengchao.freight.model.ShippingRegions;
import com.fengchao.freight.model.ShippingRegionsExample;
import com.fengchao.freight.model.ShippingRegionsX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingRegionsXMapper {
    long countByExample(ShippingRegionsExample example);

    int deleteByExample(ShippingRegionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShippingRegionsX record);

    int insertSelective(ShippingRegionsX record);

    List<ShippingRegionsX> selectByExample(ShippingRegionsExample example);

    ShippingRegionsX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShippingRegionsX record, @Param("example") ShippingRegionsExample example);

    int updateByExample(@Param("record") ShippingRegionsX record, @Param("example") ShippingRegionsExample example);

    int updateByPrimaryKeySelective(ShippingRegionsX record);

    int updateByPrimaryKey(ShippingRegionsX record);

    List<ShippingRegionsX> findRegionsByTemplateId(Integer id);

    int deleteByTemplateId(Integer templateId);

    ShippingRegionsX selectByProvinceId(@Param("provinceId") String provinceId, @Param("templateId")Integer templateId);

    ShippingRegionsX selectDefaltShipRegions(int templateId);
}