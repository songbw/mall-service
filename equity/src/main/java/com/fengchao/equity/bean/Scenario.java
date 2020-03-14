package com.fengchao.equity.bean;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class Scenario {
    private int type;
    private String[] couponMpus;
    private JSONArray couponSkus;
    private String[] excludeMpus;
    private JSONArray excludeSkus;
    private String[] categories;
    private String[] brands;
}
