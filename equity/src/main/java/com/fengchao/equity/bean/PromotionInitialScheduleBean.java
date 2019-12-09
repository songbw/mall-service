package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PromotionInitialScheduleBean {

    private List<String> initialSchedules;
    private String appId;
}
