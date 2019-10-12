package com.fengchao.equity.dao;

import com.fengchao.equity.bean.VirtualTicketsBean;
import com.fengchao.equity.mapper.VirtualTicketsMapper;
import com.fengchao.equity.mapper.VirtualTicketsMapperX;
import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.model.VirtualTicketsExample;
import com.fengchao.equity.model.VirtualTicketsX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VirtualTicketsDao {
    @Autowired
    private VirtualTicketsMapper ticketsMapper;
    @Autowired
    private VirtualTicketsMapperX ticketsMapperX;


    public int createVirtualticket(VirtualTickets virtualTickets) {
        return ticketsMapper.insertSelective(virtualTickets);
    }

    public int consumeTicket(VirtualTicketsBean bean) {
        VirtualTickets virtualTickets = new VirtualTickets();
        virtualTickets.setStatus(2);
        virtualTickets.setUpdateTime(new Date());
        VirtualTicketsExample example = new VirtualTicketsExample();
        VirtualTicketsExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(bean.getCode());
        if(bean.getOpenId() != null){
            criteria.andOpenIdEqualTo(bean.getOpenId());
        }
        return ticketsMapper.updateByExampleSelective(virtualTickets, example);
    }

    public int cancelTicket(VirtualTicketsBean bean) {
        VirtualTickets virtualTickets = new VirtualTickets();
        virtualTickets.setStatus(4);
        virtualTickets.setUpdateTime(new Date());
        VirtualTicketsExample example = new VirtualTicketsExample();
        VirtualTicketsExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(bean.getCode());
        if(bean.getOpenId() != null){
            criteria.andOpenIdEqualTo(bean.getOpenId());
        }
        return ticketsMapper.updateByExampleSelective(virtualTickets, example);
    }

    public PageInfo<VirtualTicketsX> findVirtualTicket(String openId, Integer pageNo, Integer pageSize) {
        VirtualTicketsExample example = new VirtualTicketsExample();
        VirtualTicketsExample.Criteria criteria = example.createCriteria();
        criteria.andOpenIdEqualTo(openId);

        PageHelper.startPage(pageNo, pageSize);
        List<VirtualTicketsX> virtualProds = ticketsMapperX.selectByExample(example);

        return new PageInfo<>(virtualProds);
    }

    public VirtualTickets findVirtualTicketById(int id) {
        return ticketsMapper.selectByPrimaryKey(id);
    }

    public int ticketsInvalid(int virtualId) {
        VirtualTickets virtualTickets = new VirtualTickets();
        virtualTickets.setStatus(3);
        virtualTickets.setUpdateTime(new Date());
        VirtualTicketsExample example = new VirtualTicketsExample();
        VirtualTicketsExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(virtualId);
        return ticketsMapper.updateByExampleSelective(virtualTickets, example);
    }
}
