package com.fengchao.product.aoyi.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class QueryProdBean {
    private Integer offset;
    private Integer pageNo;
    @Range(min = 1, max = 100, message = "pageSize一次性获取最大列表数不能超过100")
    private Integer pageSize;
    private List<String> couponMpus;
    private String excludeMpus;
    private String categories;
    private String brands;
}
