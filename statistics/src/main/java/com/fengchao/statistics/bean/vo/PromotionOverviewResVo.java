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
    private String promotionType; // 活动类型 1:秒杀 2:优选 3:普通
    private String orderCount; // 订单数量

    private String statisticDate; // yyyyMMdd
}
