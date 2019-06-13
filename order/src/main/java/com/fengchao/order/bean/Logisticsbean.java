package com.fengchao.order.bean;

import lombok.Data;

import java.util.List;

@Data
public class Logisticsbean {

    private Integer total;
    private List<Logisticsbean> logisticsList;
    private String logisticsId;
    private String logisticsContent;
    private Integer orderId;
    private String subOrderId;
    private Integer merchantId;
}
