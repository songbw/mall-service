package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.model.StarSkuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarSkuMapper {
    long countByExample(StarSkuExample example);

    int deleteByExample(StarSkuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarSku record);

    int insertSelective(StarSku record);

    List<StarSku> selectByExample(StarSkuExample example);

    StarSku selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarSku record, @Param("example") StarSkuExample example);

    int updateByExample(@Param("record") StarSku record, @Param("example") StarSkuExample example);

    int updateByPrimaryKeySelective(StarSku record);

    int updateByPrimaryKey(StarSku record);
}