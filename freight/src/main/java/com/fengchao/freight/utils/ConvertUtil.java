package com.fengchao.freight.utils;

import com.fengchao.freight.bean.page.PageBean;
import com.fengchao.freight.bean.page.PageVo;
import com.github.pagehelper.PageInfo;

public class ConvertUtil {

    /**
     * PageVo 转 PageBean
     *
     * @param pageVo
     * @return
     */
    public static PageBean convertToPageBean(PageVo pageVo) {
        PageBean pageBean = new PageBean();
        pageBean.setTotal(pageVo.getTotalCount() == null ? null : pageVo.getTotalCount().intValue());
        pageBean.setPages(pageVo.getTotalPage());
        pageBean.setPageNo(pageVo.getPageNo());
        pageBean.setPageSize(pageVo.getPageSize());

        return pageBean;
    }

    /**
     * PageInfo 转 PageVo
     *
     * @param pageInfo
     * @return
     */
    public static PageVo convertToPageVo(PageInfo pageInfo) {
        PageVo pageVo = new PageVo();

        pageVo.setTotalCount((pageInfo.getTotal()));
        pageVo.setTotalPage(pageInfo.getPages());
        pageVo.setPageNo(pageInfo.getPageNum());
        pageVo.setPageSize(pageInfo.getPageSize());

        return pageVo;
    }
}


