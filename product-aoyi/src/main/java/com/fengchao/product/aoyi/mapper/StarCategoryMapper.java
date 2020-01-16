package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarCategory;
import com.fengchao.product.aoyi.model.StarCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarCategoryMapper {
    long countByExample(StarCategoryExample example);

    int deleteByExample(StarCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarCategory record);

    int insertSelective(StarCategory record);

    List<StarCategory> selectByExample(StarCategoryExample example);

    StarCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarCategory record, @Param("example") StarCategoryExample example);

    int updateByExample(@Param("record") StarCategory record, @Param("example") StarCategoryExample example);

    int updateByPrimaryKeySelective(StarCategory record);

    int updateByPrimaryKey(StarCategory record);
}