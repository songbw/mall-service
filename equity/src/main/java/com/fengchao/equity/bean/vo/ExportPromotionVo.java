package com.fengchao.equity.bean.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExportPromotionVo {
    private String appId;
    private String name;
    private String status;
    private String type;//价格类型
    private String symbol;//符号
    private Float coefficient;//系数
}
