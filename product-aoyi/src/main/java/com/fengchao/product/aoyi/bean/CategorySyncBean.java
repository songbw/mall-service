package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2019/10/10 12:03
 */
@Setter
@Getter
public class CategorySyncBean {
    private String platformId ;
    private List<Integer> categories ;
}
