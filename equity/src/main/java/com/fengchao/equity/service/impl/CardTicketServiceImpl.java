package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.bean.ExportCardBean;
import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.dao.CardInfoDao;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.service.CardTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardTicketServiceImpl implements CardTicketService {

    @Autowired
    private CardInfoDao infoDao;
    @Autowired
    private CardTicketDao assignsDao;
    @Autowired
    private CardAndCouponDao cardAndCouponDao;

    @Override
    public int assignsCardTicket(CardTicketBean bean) {
        List<CardTicket> tickets = new ArrayList<>();
        for (int i = 0; i < bean.getNum(); i++){
            CardTicket ticket = new CardTicket();
            String code = String.valueOf(System.currentTimeMillis()) + (int)((Math.random()*9+1)*100000);
            ticket.setCardId(bean.getCardId());
            ticket.setCard(code);
            ticket.setPassword(String.valueOf((int)((Math.random()*9+1)*100000)));
            ticket.setRemark(bean.getRemark());
            tickets.add(ticket);
        }
        return assignsDao.insertBatch(tickets);
    }

    @Override
    public int activatesCardTicket(List<CardTicket> beans) {

        return assignsDao.activatesCardTicket(beans);
    }

    @Override
    public List<CardInfoX> exportCardTicket(ExportCardBean bean) {

        List<CardInfoX> infoXES = infoDao.findByIds(bean.getIds());
        for (CardInfoX infoX: infoXES){
            List<Integer> couponIds= cardAndCouponDao.findCouponIdByCardId(infoX.getId());
            infoX.setCouponIds(couponIds);
            List<CardTicket> tickets = assignsDao.findbyCardId(infoX.getId());
            infoX.setTickets(tickets);
        }
        return infoXES;
    }
}
