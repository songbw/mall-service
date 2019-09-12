package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionMpu;
import com.fengchao.equity.model.PromotionMpuExample;
import java.util.List;

import com.fengchao.equity.model.PromotionMpuX;
import org.apache.ibatis.annotations.Param;

public interface PromotionMpuMapper {
    long countByExample(PromotionMpuExample example);

    int deleteByExample(PromotionMpuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromotionMpu record);

    int insertSelective(PromotionMpu record);

    List<PromotionMpu> selectByExample(PromotionMpuExample example);

    PromotionMpu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromotionMpu record, @Param("example") PromotionMpuExample example);

    int updateByExample(@Param("record") PromotionMpu record, @Param("example") PromotionMpuExample example);

    int updateByPrimaryKeySelective(PromotionMpu record);

    int updateByPrimaryKey(PromotionMpu record);
}