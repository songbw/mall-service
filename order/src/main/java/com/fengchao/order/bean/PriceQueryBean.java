package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PriceQueryBean implements Serializable{
    private String cityId ;
    private List<PriceBean> skus ;
}
