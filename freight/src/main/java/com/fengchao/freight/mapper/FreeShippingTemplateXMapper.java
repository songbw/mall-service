package com.fengchao.freight.mapper;

import com.fengchao.freight.model.FreeShippingTemplate;
import com.fengchao.freight.model.FreeShippingTemplateExample;
import com.fengchao.freight.model.FreeShippingTemplateX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreeShippingTemplateXMapper {
    long countByExample(FreeShippingTemplateExample example);

    int deleteByExample(FreeShippingTemplateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreeShippingTemplateX record);

    int insertSelective(FreeShippingTemplateX record);

    List<FreeShippingTemplateX> selectByExample(FreeShippingTemplateExample example);

    FreeShippingTemplateX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreeShippingTemplateX record, @Param("example") FreeShippingTemplateExample example);

    int updateByExample(@Param("record") FreeShippingTemplateX record, @Param("example") FreeShippingTemplateExample example);

    int updateByPrimaryKeySelective(FreeShippingTemplateX record);

    int updateByPrimaryKey(FreeShippingTemplateX record);

    FreeShippingTemplateX selectByMerchantId(Integer merchantId);
}