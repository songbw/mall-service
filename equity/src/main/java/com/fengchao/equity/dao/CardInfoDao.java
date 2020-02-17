package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.mapper.CardInfoMapper;
import com.fengchao.equity.mapper.CardInfoMapperX;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoExample;
import com.fengchao.equity.model.CardInfoX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CardInfoDao {

    @Autowired
    private CardInfoMapper mapper;
    @Autowired
    private CardInfoMapperX mapperX;

    public int createCardTicket(CardInfo bean) {
        return mapper.insertSelective(bean);
    }

    public int updateCardTicket(CardInfo bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    public CardInfoX findByCardId(Integer id) {
        return mapperX.selectByPrimaryKey(id);
    }

    public CardInfo findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    public PageInfo<CardInfo> findCardTicket(CardInfoBean bean) {
        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        if(bean.getType() != null){
            criteria.andTypeEqualTo(bean.getType());
        }
        if(bean.getStatus() != null){
            criteria.andStatusEqualTo(bean.getStatus());
        }
        if(bean.getName() != null){
            criteria.andNameLike("%" +bean.getName()+ "%");
        }

        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<CardInfo> cardTickets = mapper.selectByExample(example);
        return new PageInfo<>(cardTickets);
    }

    public List<CardInfoX> findByIds(List<Integer> ids) {
        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        if(ids != null && !ids.isEmpty()){
            criteria.andIdIn(ids);
        }

        return mapperX.selectByExample(example);
    }
}
