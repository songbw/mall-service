package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.EnterpriseCategory;
import com.fengchao.product.aoyi.model.EnterpriseCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnterpriseCategoryMapper {
    long countByExample(EnterpriseCategoryExample example);

    int deleteByExample(EnterpriseCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseCategory record);

    int insertSelective(EnterpriseCategory record);

    List<EnterpriseCategory> selectByExample(EnterpriseCategoryExample example);

    EnterpriseCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EnterpriseCategory record, @Param("example") EnterpriseCategoryExample example);

    int updateByExample(@Param("record") EnterpriseCategory record, @Param("example") EnterpriseCategoryExample example);

    int updateByPrimaryKeySelective(EnterpriseCategory record);

    int updateByPrimaryKey(EnterpriseCategory record);
}