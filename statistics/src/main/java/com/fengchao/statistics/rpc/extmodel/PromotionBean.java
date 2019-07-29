package com.fengchao.statistics.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PromotionBean {

    private Integer id;

    private String name;

    private Integer promotionType;

    /**
     * 活动类型 1:秒杀 2:优选 3:普通
     */
    private Integer type;

    private Integer status;

}

