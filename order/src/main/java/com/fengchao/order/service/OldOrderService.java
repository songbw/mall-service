package com.fengchao.order.service;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.fengchao.order.bean.OperaResponse;
import com.github.pagehelper.PageInfo;

/**
 * @author songbw
 * @date 2019/10/30 18:12
 */
public interface OldOrderService {

    OperaResponse findList(OldOrderQueryBean queryBean) ;
}
