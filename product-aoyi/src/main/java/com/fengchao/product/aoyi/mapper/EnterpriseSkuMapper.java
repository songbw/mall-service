package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.EnterpriseSku;
import com.fengchao.product.aoyi.model.EnterpriseSkuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnterpriseSkuMapper {
    long countByExample(EnterpriseSkuExample example);

    int deleteByExample(EnterpriseSkuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseSku record);

    int insertSelective(EnterpriseSku record);

    List<EnterpriseSku> selectByExample(EnterpriseSkuExample example);

    EnterpriseSku selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EnterpriseSku record, @Param("example") EnterpriseSkuExample example);

    int updateByExample(@Param("record") EnterpriseSku record, @Param("example") EnterpriseSkuExample example);

    int updateByPrimaryKeySelective(EnterpriseSku record);

    int updateByPrimaryKey(EnterpriseSku record);
}