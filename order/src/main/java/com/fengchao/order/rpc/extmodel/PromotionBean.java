package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PromotionBean {

    private Integer id;

    private String name;

    private String tag;

    private Integer promotionType;

    private Integer status;

    private Date startDate;

    private Date endDate;

    private Date createdDate;

    private Integer limit;
    private Integer offset;

    /**
     * 结算类型（0：普通类结算， 1：秒杀类结算， 2：精品类结算）
     */
    private Integer accountType;
}

