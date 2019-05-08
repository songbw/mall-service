package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryCityPrice {
    private String cityId;
    private List<PriceSkus> skus = new ArrayList<>();
}
