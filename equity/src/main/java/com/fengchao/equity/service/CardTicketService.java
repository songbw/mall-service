package com.fengchao.equity.service;

import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.bean.ExportCardBean;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.model.CardTicketX;

import java.util.List;
import java.util.Map;

public interface CardTicketService {
    int assignsCardTicket(CardTicketBean bean);

    int activatesCardTicket(List<CardTicket> beans);

    List<CardInfoX> exportCardTicket(ExportCardBean bean);

    int verifyCardTicket(CardTicketBean bean) throws Exception;

    String exchangeCardTicket(CardTicketBean bean) throws Exception;

    List<CardTicketX> getCardTicket(String openId);

    CardTicket findById(int id);

    int invalid(int id);

    Map<String, String> selectPlatformAll();

    CardTicketX getCardTicketByCard(String openId, String card);
}
