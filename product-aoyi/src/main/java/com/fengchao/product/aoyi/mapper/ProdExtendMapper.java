package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.ProdExtend;
import com.fengchao.product.aoyi.model.ProdExtendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProdExtendMapper {
    long countByExample(ProdExtendExample example);

    int deleteByExample(ProdExtendExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProdExtend record);

    int insertSelective(ProdExtend record);

    List<ProdExtend> selectByExample(ProdExtendExample example);

    ProdExtend selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProdExtend record, @Param("example") ProdExtendExample example);

    int updateByExample(@Param("record") ProdExtend record, @Param("example") ProdExtendExample example);

    int updateByPrimaryKeySelective(ProdExtend record);

    int updateByPrimaryKey(ProdExtend record);
}