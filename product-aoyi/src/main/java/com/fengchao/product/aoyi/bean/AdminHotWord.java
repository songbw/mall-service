package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2020/11/5 19:42
 */
@Setter
@Getter
public class AdminHotWord {
    private String appId;
    private List<HotWord> hotWordList ;
}
