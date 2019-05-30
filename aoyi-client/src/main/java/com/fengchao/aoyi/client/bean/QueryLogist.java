package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryLogist {
//    private String orderNo;
    private String merchantNo;
    private List<LogistOrderItems> orderItemIds = new ArrayList<>();
}
