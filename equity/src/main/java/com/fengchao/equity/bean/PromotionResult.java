package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class PromotionResult {

    private Integer num;
    private Set<String> mpus;
    private Integer promotionId;
}
