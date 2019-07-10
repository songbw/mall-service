package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionMpu;

import java.util.List;

public interface PromotionMpuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionMpu record);

    int insertSelective(PromotionMpu record);

    int updateByPrimaryKeySelective(PromotionMpu record);

    int updateByPrimaryKey(PromotionMpu record);

    int deleteBypromotionId(PromotionMpu record);

    List<PromotionMpu> selectByPrimarySku(Integer id);
}