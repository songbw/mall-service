package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
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

    private String appId;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date activateStartTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date activateEndTime;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String corporationCode;

    private String code;

}
