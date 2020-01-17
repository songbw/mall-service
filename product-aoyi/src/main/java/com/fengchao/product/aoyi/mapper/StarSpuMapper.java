package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarSpu;
import com.fengchao.product.aoyi.model.StarSpuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarSpuMapper {
    long countByExample(StarSpuExample example);

    int deleteByExample(StarSpuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarSpu record);

    int insertSelective(StarSpu record);

    List<StarSpu> selectByExampleWithBLOBs(StarSpuExample example);

    List<StarSpu> selectByExample(StarSpuExample example);

    StarSpu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarSpu record, @Param("example") StarSpuExample example);

    int updateByExampleWithBLOBs(@Param("record") StarSpu record, @Param("example") StarSpuExample example);

    int updateByExample(@Param("record") StarSpu record, @Param("example") StarSpuExample example);

    int updateByPrimaryKeySelective(StarSpu record);

    int updateByPrimaryKeyWithBLOBs(StarSpu record);

    int updateByPrimaryKey(StarSpu record);
}