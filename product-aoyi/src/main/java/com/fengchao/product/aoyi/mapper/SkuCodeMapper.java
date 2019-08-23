package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.model.SkuCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SkuCodeMapper {
    long countByExample(SkuCodeExample example);

    int deleteByExample(SkuCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SkuCode record);

    int insertSelective(SkuCode record);

    List<SkuCode> selectByExample(SkuCodeExample example);

    SkuCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SkuCode record, @Param("example") SkuCodeExample example);

    int updateByExample(@Param("record") SkuCode record, @Param("example") SkuCodeExample example);

    int updateByPrimaryKeySelective(SkuCode record);

    int updateByPrimaryKey(SkuCode record);
}