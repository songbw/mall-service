package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.CardTicketMapper;
import com.fengchao.equity.mapper.CardTicketMapperX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CardTicketDao {

    @Autowired
    private CardTicketMapper mapper;
    @Autowired
    private CardTicketMapperX mapperX;

    public List<CardTicket> findbyCardId(Integer id) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(id);

        return mapper.selectByExample(example);
    }

    public int insertBatch(List<CardTicket> tickets) {
        return mapperX.inserBatch(tickets);
    }

    public int activatesCardTicket(List<CardTicket> beans) {
        return mapperX.activatesCardTicket(beans);
    }
}
