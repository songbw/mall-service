package com.fengchao.freight.mapper;

import com.fengchao.freight.model.ShippingTemplate;
import com.fengchao.freight.model.ShippingTemplateExample;
import com.fengchao.freight.model.ShippingTemplateX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ShippingTemplateXMapper {
    long countByExample(ShippingTemplateExample example);

    int deleteByExample(ShippingTemplateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShippingTemplateX record);

    int insertSelective(ShippingTemplateX record);

    List<ShippingTemplateX> selectByExample(ShippingTemplateExample example);

    ShippingTemplateX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShippingTemplate record, @Param("example") ShippingTemplateExample example);

    int updateByExample(@Param("record") ShippingTemplate record, @Param("example") ShippingTemplateExample example);

    int updateByPrimaryKeySelective(ShippingTemplateX record);

    int updateByPrimaryKey(ShippingTemplateX record);

    List<ShippingTemplateX> findShipTemplateByMpu(Integer id);

    ShippingTemplateX selectDefaultTemplate();
}
