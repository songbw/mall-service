package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CouponTagBean {

    private Integer num;
    private List<Integer> couponIds;

}
