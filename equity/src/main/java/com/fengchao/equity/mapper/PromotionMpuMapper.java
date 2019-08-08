package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionMpu;
import com.fengchao.equity.model.PromotionMpuX;

import java.util.List;

public interface PromotionMpuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionMpu record);

    int insertSelective(PromotionMpu record);

    int updateByPrimaryKeySelective(PromotionMpu record);

    int updateByPrimaryKey(PromotionMpu record);

    int deleteBypromotionId(PromotionMpu record);

    List<PromotionMpuX> selectByPrimaryMpu(Integer id);
}