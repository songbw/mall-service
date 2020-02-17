package com.fengchao.equity.service;

import com.fengchao.equity.bean.CardBean;
import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoX;

public interface CardInfoService {
    int createCardInfo(CardInfoBean bean);

    int updateCardInfo(CardInfo bean);

    CardBean findByCardId(CardInfoBean id);

    PageableData<CardInfo> findCardInfo(CardInfoBean bean);

    int deleteCardInfo(Integer id);
}
