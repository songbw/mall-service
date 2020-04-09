package com.fengchao.equity.bean;

import com.fengchao.equity.model.CardInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardInfoOfCorporationBean {

    private String corporationCode;
    private List<CardInfo> cardInfoList;
}
