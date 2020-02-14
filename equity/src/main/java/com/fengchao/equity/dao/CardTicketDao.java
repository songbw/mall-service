package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.mapper.CardTicketMapper;
import com.fengchao.equity.mapper.CardTicketMapperX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import com.fengchao.equity.model.CardTicketX;
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
        criteria.andIsDeleteEqualTo((short) 1);

        return mapper.selectByExample(example);
    }

    public int insertBatch(List<CardTicket> tickets) {
        return mapperX.inserBatch(tickets);
    }

    public int activatesCardTicket(List<CardTicket> beans) {
        return mapperX.activatesCardTicket(beans);
    }

    public List<CardTicket> selectCardTicketByCard(CardTicketBean bean) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andCardEqualTo(bean.getCard());
        criteria.andPasswordEqualTo(bean.getPassword());

        return mapper.selectByExample(example);
    }

    public int update(CardTicket ticket) {
        return mapper.updateByPrimaryKeySelective(ticket);
    }

    public List<CardTicketX> seleteCardTicketByOpenId(String openId) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andOpenIdEqualTo(openId);

        return mapperX.selectByExample(example);
    }

    public CardTicket findById(int id) {
        return mapper.selectByPrimaryKey(id);
    }

}
