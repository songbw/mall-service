package com.fengchao.product.aoyi.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Rules {

    private String code;
    private String rulesDescription;
    private int perLimited;
    private String[] scopes;
    private Scenario scenario;
    private Collect collect;
    private Customer customer;
    private JSONObject couponRules;
}
