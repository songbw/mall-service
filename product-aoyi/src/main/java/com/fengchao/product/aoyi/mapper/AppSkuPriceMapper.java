package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.model.AppSkuPriceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppSkuPriceMapper {
    long countByExample(AppSkuPriceExample example);

    int deleteByExample(AppSkuPriceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppSkuPrice record);

    int insertSelective(AppSkuPrice record);

    List<AppSkuPrice> selectByExample(AppSkuPriceExample example);

    AppSkuPrice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppSkuPrice record, @Param("example") AppSkuPriceExample example);

    int updateByExample(@Param("record") AppSkuPrice record, @Param("example") AppSkuPriceExample example);

    int updateByPrimaryKeySelective(AppSkuPrice record);

    int updateByPrimaryKey(AppSkuPrice record);
}