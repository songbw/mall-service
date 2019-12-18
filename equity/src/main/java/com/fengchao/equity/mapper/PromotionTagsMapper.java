package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionTags;
import com.fengchao.equity.model.PromotionTagsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PromotionTagsMapper {
    long countByExample(PromotionTagsExample example);

    int deleteByExample(PromotionTagsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromotionTags record);

    int insertSelective(PromotionTags record);

    List<PromotionTags> selectByExample(PromotionTagsExample example);

    PromotionTags selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromotionTags record, @Param("example") PromotionTagsExample example);

    int updateByExample(@Param("record") PromotionTags record, @Param("example") PromotionTagsExample example);

    int updateByPrimaryKeySelective(PromotionTags record);

    int updateByPrimaryKey(PromotionTags record);
}