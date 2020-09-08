package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AppRatePrice;
import com.fengchao.product.aoyi.model.AppRatePriceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppRatePriceMapper {
    long countByExample(AppRatePriceExample example);

    int deleteByExample(AppRatePriceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppRatePrice record);

    int insertSelective(AppRatePrice record);

    List<AppRatePrice> selectByExample(AppRatePriceExample example);

    AppRatePrice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppRatePrice record, @Param("example") AppRatePriceExample example);

    int updateByExample(@Param("record") AppRatePrice record, @Param("example") AppRatePriceExample example);

    int updateByPrimaryKeySelective(AppRatePrice record);

    int updateByPrimaryKey(AppRatePrice record);
}