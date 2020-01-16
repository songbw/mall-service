package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarBrand;
import com.fengchao.product.aoyi.model.StarBrandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarBrandMapper {
    long countByExample(StarBrandExample example);

    int deleteByExample(StarBrandExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarBrand record);

    int insertSelective(StarBrand record);

    List<StarBrand> selectByExample(StarBrandExample example);

    StarBrand selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarBrand record, @Param("example") StarBrandExample example);

    int updateByExample(@Param("record") StarBrand record, @Param("example") StarBrandExample example);

    int updateByPrimaryKeySelective(StarBrand record);

    int updateByPrimaryKey(StarBrand record);
}