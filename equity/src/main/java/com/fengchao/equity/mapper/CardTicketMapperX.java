package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import com.fengchao.equity.model.CardTicketX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardTicketMapperX {
    long countByExample(CardTicketExample example);

    int deleteByExample(CardTicketExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardTicket record);

    int insertSelective(CardTicket record);

    List<CardTicketX> selectByExample(CardTicketExample example);

    CardTicketX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardTicketX record, @Param("example") CardTicketExample example);

    int updateByExample(@Param("record") CardTicketX record, @Param("example") CardTicketExample example);

    int updateByPrimaryKeySelective(CardTicketX record);

    int updateByPrimaryKey(CardTicketX record);

    int inserBatch(List<CardTicket> tickets);

    int activatesCardTicket(List<CardTicket> beans);

    CardTicketX selectByCard(String card);

    CardTicketX seleteCardTicketByCard(@Param("openId") String openId, @Param("card") String card);
}