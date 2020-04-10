package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.mapper.CardTicketMapper;
import com.fengchao.equity.mapper.CardTicketMapperX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketExample;
import com.fengchao.equity.utils.CardTicketStatusEnum;
import com.fengchao.equity.utils.MyErrorEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
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

    public CardTicket findbyCardCode(String cardCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardEqualTo(cardCode);
        criteria.andIsDeleteEqualTo((short) 1);

        List<CardTicket> list = mapper.selectByExample(example);
        if(null == list || 0 == list.size()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<CardTicket> batchFindByCardCodeList(List<String> cardCodes, CardTicketStatusEnum statusEnum) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardIn(cardCodes);
        criteria.andIsDeleteEqualTo((short) 1);
        if (null != statusEnum){
            criteria.andStatusEqualTo((short)statusEnum.getCode());
        }

       return mapper.selectByExample(example);

    }

    public int insertBatch(List<CardTicket> tickets) {
        return mapperX.inserBatch(tickets);
    }

    public int batchAssignCardTicket(List<CardTicket> beans) {

        try {
            return mapperX.batchAssignCardTicket(beans);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new EquityException(MyErrorEnum.ASSIGN_TICKETS_ERROR);
        }
    }

    public int activatesCardTicket(List<CardTicket> beans) {
        return mapperX.activatesCardTicket(beans);
    }

    public int batchInsertActiveCardTickets(List<CardTicket> beans) {
        return mapperX.batchInsertActiveTickets(beans);
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

    public List<CardTicket> selectCardTicketByOpenId(String openId) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andOpenIdEqualTo(openId);

        return mapper.selectByExample(example);
    }

    public CardTicket findById(int id) {
        return mapper.selectByPrimaryKey(id);
    }

    public PageInfo<CardTicket> searchCardTicket(CardInfoBean bean) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andCardIdEqualTo(bean.getId());
        //删除标记对用户来说只是逻辑显示，运营后台是需要查看的，所以此处不过滤了
        //criteria.andIsDeleteEqualTo((short) 1);
        example.setOrderByClause("update_time DESC");

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

    public PageInfo<CardTicket> getTicketPages(CardTicketBean bean,
                                               Integer status,Date activateStart,Date activateEnd,
                                               Date consumeStart, Date consumeEnd) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();

        criteria.andIsDeleteEqualTo((short) 1);
        example.setOrderByClause("update_time DESC");

        if(null != status){
            criteria.andStatusEqualTo((short)(int)status);
        }
        if (null != activateStart){
            criteria.andActivateTimeGreaterThanOrEqualTo(activateStart);
        }
        if (null != activateEnd){
            criteria.andActivateTimeLessThanOrEqualTo(activateEnd);
        }
        if (null != consumeStart){
            criteria.andConsumedTimeGreaterThanOrEqualTo(consumeStart);
        }
        if (null != consumeEnd){
            criteria.andConsumedTimeLessThanOrEqualTo(consumeEnd);
        }

        if(null != bean.getWelfareOrderNo() && !bean.getWelfareOrderNo().isEmpty()){
            criteria.andWelfareOrderNoEqualTo(bean.getWelfareOrderNo());
        }
        if(null != bean.getCorporationCode() && !bean.getCorporationCode().isEmpty()){
            criteria.andCorporationCodeEqualTo(bean.getCorporationCode());
        }
        if(null != bean.getEmployeeCode() && !bean.getEmployeeCode().isEmpty()){
            criteria.andEmployeeCodeEqualTo(bean.getEmployeeCode());
        }
        if(null != bean.getCardInfoCode() && !bean.getCardInfoCode().isEmpty()){
            criteria.andCardInfoCodeEqualTo(bean.getCardInfoCode());
        }
        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<CardTicket> tickets = mapper.selectByExample(example);
        return new PageInfo<>(tickets);
    }

    public CardTicket findByCard(String card) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andCardEqualTo(card);

        List<CardTicket> list = mapper.selectByExample(example);
        if(null == list || 0 == list.size()){
            return null;
        }else{
            return list.get(0);
        }

    }

    public CardTicket selectCardTicketByCardOpenId(String openId, String card) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andOpenIdEqualTo(openId);
        criteria.andCardEqualTo(card);

        List<CardTicket> list = mapper.selectByExample(example);
        if(null != list && 0 < list.size()){
            return list.get(0);
        }else{
            return null;
        }

    }

    public int consumeCard(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        Date date = new Date();
        CardTicket ticket = new CardTicket();
        ticket.setStatus((short) CardTicketStatusEnum.USED.getCode());
        ticket.setConsumedTime(date);
        return mapper.updateByExampleSelective(ticket, example);
    }

    public int occupyCard(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        CardTicket ticket = new CardTicket();
        ticket.setStatus((short)CardTicketStatusEnum.OCCUPIED.getCode());
        return mapper.updateByExampleSelective(ticket, example);
    }

    public CardTicket findByUseCouponCode(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        List<CardTicket> list = mapper.selectByExample(example);
        if(null == list || 0 == list.size()){
            return null;
        }else{
            return list.get(0);
        }

    }

    public List<CardTicket> findActivateTicket(List<String> cards) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andCardIn(cards);
        criteria.andStatusNotEqualTo((short)CardTicketStatusEnum.CREATED.getCode());

        return mapper.selectByExample(example);
    }

    public int deleteCardTicket(Integer id) {
        CardTicket ticket = new CardTicket();
        ticket.setId(id);
        ticket.setIsDelete((short) 2);
        return mapper.updateByPrimaryKeySelective(ticket);
    }

    public int releaseCard(String userCouponCode) {
        CardTicketExample example = new CardTicketExample();
        CardTicketExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 1);
        criteria.andUserCouponCodeEqualTo(userCouponCode);

        CardTicket ticket = new CardTicket();
        ticket.setStatus((short)CardTicketStatusEnum.EXCHANGED.getCode());
        return mapper.updateByExampleSelective(ticket, example);
    }
}
