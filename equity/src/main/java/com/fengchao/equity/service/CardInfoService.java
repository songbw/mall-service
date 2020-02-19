package com.fengchao.equity.service;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;

public interface CardInfoService {
    int createCardInfo(CardInfoBean bean);

    int updateCardInfo(CardInfo bean);

    CardInfoX findByCardId(Integer id);

    PageableData<CardInfo> findCardInfo(CardInfoBean bean);

    int deleteCardInfo(Integer id);

    PageableData<CardTicket> details(CardInfoBean bean);
}
