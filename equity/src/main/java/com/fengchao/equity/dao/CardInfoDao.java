package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.ExportCardBean;
import com.fengchao.equity.mapper.CardInfoMapper;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoExample;
import com.fengchao.equity.utils.CardInfoStatusEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardInfoDao {

    @Autowired
    private CardInfoMapper mapper;

    public int createCardTicket(CardInfo bean) {
        return mapper.insertSelective(bean);
    }

    public int updateCardTicket(CardInfo bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    public CardInfo findByCardId(Integer id) {
        return mapper.selectByPrimaryKey(id);
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
        if(bean.getAppId() != null){
            criteria.andAppIdEqualTo(bean.getAppId());
        }
        if(bean.getName() != null){
            criteria.andNameLike("%" +bean.getName()+ "%");
        }
        String code = bean.getCode();
        if(null != code && !code.isEmpty()){
            criteria.andCodeEqualTo(code);
        }
        String corporationCode = bean.getCorporationCode();
        if (null != corporationCode && !corporationCode.isEmpty()){
            criteria.andCorporationCodeEqualTo(corporationCode);
        }
        example.setOrderByClause("id DESC");

        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<CardInfo> cardTickets = mapper.selectByExample(example);
        return new PageInfo<>(cardTickets);
    }

    public List<CardInfo> findByIds(ExportCardBean bean) {
        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        if(bean.getIds() != null && !bean.getIds().isEmpty()){
            criteria.andIdIn(bean.getIds());
        }
        if(bean.getAppId() != null){
            criteria.andAppIdEqualTo(bean.getAppId());
        }
        if(null != bean.getStatus() && 0 != bean.getStatus()){
            criteria.andStatusEqualTo(bean.getStatus());
        }

        return mapper.selectByExample(example);
    }

    public List<CardInfo> findByCodeList(List<String> codeList) {
        if(null == codeList || 0 == codeList.size()){
            return new ArrayList<>();
        }

        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andCodeIn(codeList);

        return mapper.selectByExample(example);
    }

    public CardInfo
    findByCardCode(String code){
        if(null == code || code.isEmpty()){
            return null;
        }

        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andCodeEqualTo(code);

        List<CardInfo> list = mapper.selectByExample(example);
        if(null == list || 0 == list.size()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<CardInfo> findByCorporation(String corporationCode, Integer status) {
        CardInfoExample example = new CardInfoExample();
        CardInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        if(null != status){
            criteria.andStatusEqualTo((short)(int)status);
        }
        if (null != corporationCode && !corporationCode.isEmpty()){
            criteria.andCorporationCodeEqualTo(corporationCode);
        }

        return mapper.selectByExample(example);
    }
}
