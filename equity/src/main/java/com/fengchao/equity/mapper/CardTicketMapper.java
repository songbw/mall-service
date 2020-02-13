package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CardTicketMapper {
    long countByExample(CardTicketExample example);

    int deleteByExample(CardTicketExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardTicket record);

    int insertSelective(CardTicket record);

    List<CardTicket> selectByExample(CardTicketExample example);

    CardTicket selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardTicket record, @Param("example") CardTicketExample example);

    int updateByExample(@Param("record") CardTicket record, @Param("example") CardTicketExample example);

    int updateByPrimaryKeySelective(CardTicket record);

    int updateByPrimaryKey(CardTicket record);
}