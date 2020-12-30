package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.EnterpriseBrand;
import com.fengchao.product.aoyi.model.EnterpriseBrandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnterpriseBrandMapper {
    long countByExample(EnterpriseBrandExample example);

    int deleteByExample(EnterpriseBrandExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseBrand record);

    int insertSelective(EnterpriseBrand record);

    List<EnterpriseBrand> selectByExample(EnterpriseBrandExample example);

    EnterpriseBrand selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EnterpriseBrand record, @Param("example") EnterpriseBrandExample example);

    int updateByExample(@Param("record") EnterpriseBrand record, @Param("example") EnterpriseBrandExample example);

    int updateByPrimaryKeySelective(EnterpriseBrand record);

    int updateByPrimaryKey(EnterpriseBrand record);
}