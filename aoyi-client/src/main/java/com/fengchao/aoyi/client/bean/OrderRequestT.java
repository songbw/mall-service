package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestT {

    private String tradeNo;

    private String companyCustNo;

    private String receiverName;

    private String telephone;

    private String mobile;

    private String email;

    private String provinceId;

    private String cityId;

    private String countyId;

    private String townId;

    private String address;

    private String zip;

    private String invoiceState;

    private String invoiceType;

    private String invoiceTitle;

    private String invoiceContent;

//    private String orderStatus = "1";
//    private Date createDate;

    private List<SubOrderT> aoyiOrderEntries = new ArrayList<>();
}
