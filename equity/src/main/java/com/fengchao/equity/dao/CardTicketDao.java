package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.mapper.CardTicketMapper;
import com.fengchao.equity.mapper.CardTicketMapperX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import com.fengchao.equity.model.CardTicketX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public class CardTicketDao {

    @Autowired
    private CardTicketMapper mapper;
    @Autowired
    private CardTicketMapperX mapperX;

    public List<CardTicket> findbyCardId(Integer id, Short status) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(id);
        criteria.andIsDeleteEqualTo((short) 1);
        if(status != null){
            criteria.andStatusEqualTo(status);
        }

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

    public PageInfo<CardTicket> searchCardTicket(CardInfoBean bean) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(bean.getId());
        criteria.andIsDeleteEqualTo((short) 1);

        if(bean.getStatus() != null){
            criteria.andStatusEqualTo(bean.getStatus());
        }
        if(bean.getActivateStartTime() != null){
            criteria.andActivateTimeGreaterThanOrEqualTo(bean.getActivateStartTime());
        }
        if(bean.getActivateEndTime() != null){
            criteria.andActivateTimeLessThanOrEqualTo(bean.getActivateEndTime());
        }
        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<CardTicket> tickets = mapper.selectByExample(example);
        return  new PageInfo<>(tickets);
    }

    public CardTicketX findbyCard(String card) {
        return mapperX.selectByCard(card);
    }

    public CardTicketX seleteCardTicketByCard(String openId, String card) {
        return mapperX.seleteCardTicketByCard(openId, card);
    }

    public int consumeCard(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        Date date = new Date();
        CardTicket ticket = new CardTicket();
        ticket.setStatus((short) 6);
        ticket.setConsumedTime(date);
        return mapper.updateByExampleSelective(ticket, example);
    }

    public int occupyCard(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        CardTicket ticket = new CardTicket();
        ticket.setStatus((short) 5);
        return mapper.updateByExampleSelective(ticket, example);
    }

    public CardTicketX findByuseCouponCode(String userCouponCode) {
        return mapperX.selectByUseCouponCode(userCouponCode);
    }
}
