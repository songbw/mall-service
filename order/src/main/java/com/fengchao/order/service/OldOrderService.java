package com.fengchao.order.service;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.github.pagehelper.PageInfo;

/**
 * @author songbw
 * @date 2019/10/30 18:12
 */
public interface OldOrderService {

    PageInfo findList(OldOrderQueryBean queryBean) ;
}
