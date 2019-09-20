package com.fengchao.freight.bean.page;


import com.fengchao.freight.bean.page.PageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @param <T>
 */
@Setter
@Getter
public class PageableData<T> {

    /**
     * 业务数据
     */
    private List<T> list;

    /**
     * 分页信息
     */
    private PageVo pageInfo;
}
