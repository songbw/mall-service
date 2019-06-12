package com.fengchao.order.service;

import com.fengchao.order.bean.InvoiceInfoQueryBean;
import com.fengchao.order.bean.PageBean;
import com.fengchao.order.model.InvoiceInfo;

/**
 * 发票信息服务
 */
public interface InvoiceInfoService {

    Integer add(InvoiceInfo bean) ;

    Integer delete(Integer id) ;

    Integer modify(InvoiceInfo bean) ;

    PageBean findList(InvoiceInfoQueryBean bean) ;

    InvoiceInfo find(Integer id) ;

}
