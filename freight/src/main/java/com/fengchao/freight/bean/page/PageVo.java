package com.fengchao.freight.bean.page;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageVo {

    /**
     * 总记录数
     */
    private Long totalCount;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 当前页
     */
    private Integer pageNo;

    /**
     * 页面记录数
     */
    private Integer pageSize;
}