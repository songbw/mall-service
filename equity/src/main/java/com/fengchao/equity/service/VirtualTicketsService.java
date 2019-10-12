package com.fengchao.equity.service;

import com.fengchao.equity.bean.VirtualTicketsBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.model.VirtualTicketsX;

public interface VirtualTicketsService {
    int createVirtualticket(VirtualTicketsBean bean);

    int consumeTicket(VirtualTicketsBean bean);

    int cancelTicket(VirtualTicketsBean bean);

    PageableData<VirtualTicketsX> findVirtualTicket(String openId, Integer pageNo, Integer pageSize);

    VirtualTickets findVirtualTicketById(int virtualId);

    int ticketsInvalid(int virtualId);
}
