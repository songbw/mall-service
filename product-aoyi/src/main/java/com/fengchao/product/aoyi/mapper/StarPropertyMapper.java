package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarProperty;
import com.fengchao.product.aoyi.model.StarPropertyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StarPropertyMapper {
    long countByExample(StarPropertyExample example);

    int deleteByExample(StarPropertyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StarProperty record);

    int insertSelective(StarProperty record);

    List<StarProperty> selectByExample(StarPropertyExample example);

    StarProperty selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StarProperty record, @Param("example") StarPropertyExample example);

    int updateByExample(@Param("record") StarProperty record, @Param("example") StarPropertyExample example);

    int updateByPrimaryKeySelective(StarProperty record);

    int updateByPrimaryKey(StarProperty record);
}