package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.dao.CardInfoDao;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.service.CardInfoService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardInfoServiceImpl implements CardInfoService {

    @Autowired
    private CardInfoDao dao;
    @Autowired
    private CardTicketDao assignsDao;
    @Autowired
    private CardAndCouponDao cardAndCouponDao;

    @Override
    public int createCardInfo(CardInfoBean bean) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setAmount(bean.getAmount());
        cardInfo.setName(bean.getName());
        cardInfo.setEffectiveDays(bean.getEffectiveDays());
        cardInfo.setType(bean.getType());
        cardInfo.setAppId(bean.getAppId());
        int cardTicket = dao.createCardTicket(cardInfo);
        if(cardTicket == 1){
            for (int i = 0; i < bean.getCouponIds().size(); i++){
                CardAndCoupon cardAndCoupon = new CardAndCoupon();
                cardAndCoupon.setCardId(cardInfo.getId());
                cardAndCoupon.setCouponId(bean.getCouponIds().get(i));
                cardAndCouponDao.create(cardAndCoupon);
            }
        }
        return cardTicket;
    }

    @Override
    public int updateCardInfo(CardInfo bean) {
        return dao.updateCardTicket(bean);
    }

    @Override
    public CardInfoX findByCardId(Integer id) {
        CardInfoX cardTicket = dao.findByCardId(id);
        List<Integer> couponIds= cardAndCouponDao.findCouponIdByCardId(id);
        cardTicket.setCouponIds(couponIds);
        List<CardTicket> tickets = assignsDao.findbyCardId(id);
        cardTicket.setTickets(tickets);
        return cardTicket;
    }

    @Override
    public PageableData<CardInfo> findCardInfo(CardInfoBean bean) {
        PageableData<CardInfo> pageableData = new PageableData<>();

        PageInfo<CardInfo> cardTicket = dao.findCardTicket(bean);
        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(cardTicket);
        List<CardInfo> cardTicketList = cardTicket.getList();
        pageableData.setList(cardTicketList);
        pageableData.setPageInfo(pageVo);

        return pageableData;
    }

    @Override
    public int deleteCardInfo(Integer id) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setId(id);
        cardInfo.setIsDelete((short) 2);
        int num = dao.updateCardTicket(cardInfo);
        if(num == 1){
            CardAndCoupon cardAndCoupon = new CardAndCoupon();
            cardAndCoupon.setIsDelete((short) 2);
            cardAndCoupon.setCardId(id);
            cardAndCouponDao.updateCardAndCoupon(cardAndCoupon);
        }
        return dao.updateCardTicket(cardInfo);
    }
}
