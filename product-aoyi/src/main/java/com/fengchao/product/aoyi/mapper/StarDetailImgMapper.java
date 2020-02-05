package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarDetailImg;
import com.fengchao.product.aoyi.model.StarDetailImgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarDetailImgMapper {
    long countByExample(StarDetailImgExample example);

    int deleteByExample(StarDetailImgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarDetailImg record);

    int insertSelective(StarDetailImg record);

    List<StarDetailImg> selectByExample(StarDetailImgExample example);

    StarDetailImg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarDetailImg record, @Param("example") StarDetailImgExample example);

    int updateByExample(@Param("record") StarDetailImg record, @Param("example") StarDetailImgExample example);

    int updateByPrimaryKeySelective(StarDetailImg record);

    int updateByPrimaryKey(StarDetailImg record);
}