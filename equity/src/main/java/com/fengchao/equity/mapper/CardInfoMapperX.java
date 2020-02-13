package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoExample;
import com.fengchao.equity.model.CardInfoX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardInfoMapperX {
    long countByExample(CardInfoExample example);

    int deleteByExample(CardInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardInfoX record);

    int insertSelective(CardInfoX record);

    List<CardInfoX> selectByExample(CardInfoExample example);

    CardInfoX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardInfoX record, @Param("example") CardInfoExample example);

    int updateByExample(@Param("record") CardInfoX record, @Param("example") CardInfoExample example);

    int updateByPrimaryKeySelective(CardInfoX record);

    int updateByPrimaryKey(CardInfoX record);
}
