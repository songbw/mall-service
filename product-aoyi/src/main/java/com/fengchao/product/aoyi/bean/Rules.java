package com.fengchao.product.aoyi.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class Rules implements Serializable {

    private String code;
    private String rulesDescription;
    private int perLimited;
    private String[] scopes;
    private Scenario scenario;
    private Collect collect;
    private Customer customer;
    private JSONObject couponRules;
}
