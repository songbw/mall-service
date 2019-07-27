package com.fengchao.statistics.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author tom
 * @Date 19-7-27 上午11:10
 */
@Setter
@Getter
public class PromotionOverviewResVo {

    private Integer promotionId;
    private String promotionName;
    private String orderAmount; // 单位元

    private String statisticDate; // yyyyMMdd
}
