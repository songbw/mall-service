package com.fengchao.equity.bean;

import com.fengchao.equity.bean.vo.PageVo;
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
