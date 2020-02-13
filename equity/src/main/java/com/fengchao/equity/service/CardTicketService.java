package com.fengchao.equity.service;

import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.bean.ExportCardBean;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;

import java.util.List;

public interface CardTicketService {
    int assignsCardTicket(CardTicketBean bean);

    int activatesCardTicket(List<CardTicket> beans);

    List<CardInfoX> exportCardTicket(ExportCardBean bean);
}
