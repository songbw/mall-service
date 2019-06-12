package com.fengchao.order.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubOrderT {
    private String orderNo ;
    private String merchantNo;
    private String payment;
    private String servfee;
    private String amount;
    private String orderType = "1";
    private List<SkusT> aoyiSkus = new ArrayList<>();

}
