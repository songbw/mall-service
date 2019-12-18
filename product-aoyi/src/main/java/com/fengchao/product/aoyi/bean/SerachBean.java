package com.fengchao.product.aoyi.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class SerachBean {
    @Range(min = 1, max = 100, message = "limit一次性获取最大列表数不能超过100")
    private Integer limit = 10;
    @Range(min = 1, message = "offset必须大于0")
    private Integer offset = 1; //
    private String query;
    private String categoryID;
    private String skuid;
    private String state;
    private String brand;
    private Integer merchantHeader;
    private Integer merchantId;
    private Integer id;
    private String order;
    private String mpu;
}
