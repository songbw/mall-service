package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AoyiBaseCategoryMapper {
    long countByExample(AoyiBaseCategoryExample example);

    int deleteByExample(AoyiBaseCategoryExample example);

    int deleteByPrimaryKey(Integer categoryId);

    int insert(AoyiBaseCategory record);

    int insertSelective(AoyiBaseCategory record);

    List<AoyiBaseCategory> selectByExample(AoyiBaseCategoryExample example);

    AoyiBaseCategory selectByPrimaryKey(Integer categoryId);

    int updateByExampleSelective(@Param("record") AoyiBaseCategory record, @Param("example") AoyiBaseCategoryExample example);

    int updateByExample(@Param("record") AoyiBaseCategory record, @Param("example") AoyiBaseCategoryExample example);

    int updateByPrimaryKeySelective(AoyiBaseCategory record);

    int updateByPrimaryKey(AoyiBaseCategory record);
}