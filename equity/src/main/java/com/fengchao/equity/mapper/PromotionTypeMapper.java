package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionType;
import com.fengchao.equity.model.PromotionTypeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PromotionTypeMapper {
    long countByExample(PromotionTypeExample example);

    int deleteByExample(PromotionTypeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PromotionType record);

    int insertSelective(PromotionType record);

    List<PromotionType> selectByExample(PromotionTypeExample example);

    PromotionType selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PromotionType record, @Param("example") PromotionTypeExample example);

    int updateByExample(@Param("record") PromotionType record, @Param("example") PromotionTypeExample example);

    int updateByPrimaryKeySelective(PromotionType record);

    int updateByPrimaryKey(PromotionType record);
}