package com.fengchao.product.aoyi.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * Created by song on 2018-03-12.
 */
 @Data
public class QueryBean implements Serializable {
        @Range(min = 1, message = "pageNo必须大于0")
        private Integer pageNo=1;
        @Range(min = 1, max = 100, message = "pageSize一次性获取最大列表数不能超过100")
        private Integer pageSize=10;
        private Integer status= -1;
        private String startTime;
        private String endTime;
        private String key="";
        private String openId;
        private String appId ;
}
