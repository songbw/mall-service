package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PriceQueryBean extends QueryBean implements Serializable{
    private String cityId ;
    private List<PriceBean> skus ;
}
