package com.fengchao.freight.mapper;

import com.fengchao.freight.model.FreeShippingTemplate;
import com.fengchao.freight.model.FreeShippingTemplateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreeShippingTemplateXMapper {
    long countByExample(FreeShippingTemplateExample example);

    int deleteByExample(FreeShippingTemplateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreeShippingTemplate record);

    int insertSelective(FreeShippingTemplate record);

    List<FreeShippingTemplate> selectByExample(FreeShippingTemplateExample example);

    FreeShippingTemplate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreeShippingTemplate record, @Param("example") FreeShippingTemplateExample example);

    int updateByExample(@Param("record") FreeShippingTemplate record, @Param("example") FreeShippingTemplateExample example);

    int updateByPrimaryKeySelective(FreeShippingTemplate record);

    int updateByPrimaryKey(FreeShippingTemplate record);
}