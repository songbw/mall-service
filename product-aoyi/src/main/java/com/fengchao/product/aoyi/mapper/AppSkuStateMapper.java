package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AppSkuState;
import com.fengchao.product.aoyi.model.AppSkuStateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppSkuStateMapper {
    long countByExample(AppSkuStateExample example);

    int deleteByExample(AppSkuStateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppSkuState record);

    int insertSelective(AppSkuState record);

    List<AppSkuState> selectByExample(AppSkuStateExample example);

    AppSkuState selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppSkuState record, @Param("example") AppSkuStateExample example);

    int updateByExample(@Param("record") AppSkuState record, @Param("example") AppSkuStateExample example);

    int updateByPrimaryKeySelective(AppSkuState record);

    int updateByPrimaryKey(AppSkuState record);
}