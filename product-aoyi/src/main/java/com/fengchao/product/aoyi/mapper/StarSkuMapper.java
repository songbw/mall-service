package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarSkuBean;
import com.fengchao.product.aoyi.model.StarSkuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarSkuMapper {
    long countByExample(StarSkuExample example);

    int deleteByExample(StarSkuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarSkuBean record);

    int insertSelective(StarSkuBean record);

    List<StarSkuBean> selectByExample(StarSkuExample example);

    StarSkuBean selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarSkuBean record, @Param("example") StarSkuExample example);

    int updateByExample(@Param("record") StarSkuBean record, @Param("example") StarSkuExample example);

    int updateByPrimaryKeySelective(StarSkuBean record);

    int updateByPrimaryKey(StarSkuBean record);
}