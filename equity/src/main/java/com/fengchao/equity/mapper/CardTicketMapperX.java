package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import com.fengchao.equity.model.CardTicketX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CardTicketMapperX {

    int inserBatch(List<CardTicket> tickets);

    int activatesCardTicket(List<CardTicket> beans);

    int batchInsertActiveTickets(List<CardTicket> tickets);

}
