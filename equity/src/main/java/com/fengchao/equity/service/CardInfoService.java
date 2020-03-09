package com.fengchao.equity.service;

import com.fengchao.equity.bean.CardDetailsBean;
import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;

public interface CardInfoService {
    int createCardInfo(CardInfoBean bean);

    int updateCardInfo(CardInfo bean);

    CardInfoX findByCardId(Integer id);

    PageableData<CardInfoX> findCardInfo(CardInfoBean bean);

    int deleteCardInfo(Integer id);

    PageableData<CardDetailsBean> details(CardInfoBean bean);
}
