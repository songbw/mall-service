package com.fengchao.product.aoyi.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * Created by song on 2018-03-12.
 */
 @Data
public class QueryBean implements Serializable {
        private Integer pageNo=1;
        @Range(min = 1, max = 80, message = "pageSize只能从1-80")
        private Integer pageSize=10;
        private Integer status= -1;
        private String startTime;
        private String endTime;
        private String key="";
        private String openId;
}
