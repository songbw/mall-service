package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryQueryBean implements Serializable {
    private Integer id;
    private String name;
}
