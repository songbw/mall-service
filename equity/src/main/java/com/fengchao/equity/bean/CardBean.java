package com.fengchao.equity.bean;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.model.CardTicket;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CardBean {
    private List<CardAndCoupon> couponIds;
    private PageableData<CardTicket> tickets;
}
