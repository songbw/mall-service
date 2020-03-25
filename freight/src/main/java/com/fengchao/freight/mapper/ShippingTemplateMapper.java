package com.fengchao.freight.mapper;

import com.fengchao.freight.model.ShippingTemplate;
import com.fengchao.freight.model.ShippingTemplateExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ShippingTemplateMapper {
    long countByExample(ShippingTemplateExample example);

    int deleteByExample(ShippingTemplateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShippingTemplate record);

    int insertSelective(ShippingTemplate record);

    List<ShippingTemplate> selectByExample(ShippingTemplateExample example);

    ShippingTemplate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShippingTemplate record, @Param("example") ShippingTemplateExample example);

    int updateByExample(@Param("record") ShippingTemplate record, @Param("example") ShippingTemplateExample example);

    int updateByPrimaryKeySelective(ShippingTemplate record);

    int updateByPrimaryKey(ShippingTemplate record);
}
