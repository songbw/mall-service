package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CardInfoBean {
    private Integer id;

    private String name;

    private String amount;

    private Short type;

    private Integer effectiveDays;

    private List<Integer> couponIds;

    private Integer cardId;

    private Short status;

    private Integer pageNo = 1;

    private Integer pageSize = 10;
}
