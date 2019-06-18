package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionSku;

import java.util.List;

public interface PromotionSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionSku record);

    int insertSelective(PromotionSku record);

    int updateByPrimaryKeySelective(PromotionSku record);

    int updateByPrimaryKey(PromotionSku record);

    int deleteBypromotionId(PromotionSku record);

    List<PromotionSku> selectByPrimarySku(Integer id);
}