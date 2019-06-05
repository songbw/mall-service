package com.fengchao.product.aoyi.bean;

import lombok.Data;

@Data
public class CategoryBean {
    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private String categoryClass;
    private String categoryIcon;
    private String categoryDesc;
    private Integer sortOrder;
}
