package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.EnterpriseSyncSkuNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;

/**
 * @author songbw
 * @date 2020/12/30 14:56
 */
public interface EnterpriseSyncSkuService {

    OperaResponse notifyEnterpriseSku(EnterpriseSyncSkuNotifyBean notifyBean) ;
}
