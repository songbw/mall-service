package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.EnterpriseSyncProductNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;

/**
 * @author songbw
 * @date 2020/12/30 14:56
 */
public interface EnterpriseSyncProductService {

    OperaResponse notifyEnterpriseSpu(EnterpriseSyncProductNotifyBean notifyBean) ;
}
