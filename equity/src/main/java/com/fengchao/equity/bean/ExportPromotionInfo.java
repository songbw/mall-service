package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ExportPromotionInfo {

    private Integer id;

    private String name;

    private Integer status;

    private Date startDate;

    private Date endDate;

    private String appId;

    private String skuid;

    private String mpu;

    private String discount;//活动价

    private String comparePrice;//第三方价格

    private String sprice;//进货价

    private String price;//销售价

    private String skuName;

    private String appName;
}
