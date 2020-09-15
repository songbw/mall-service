package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CategoryBean extends AoyiBaseCategory {
    private Integer id ;
    private String renterId ;
    private String appId ;
    private Date createdAt ;
    private Date updatedAt ;
}
