package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.EnterpriseSyncBrandNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 14:56
 */
public interface EnterpriseSyncBrandService {

    OperaResponse notifyEnterpriseBrand(EnterpriseSyncBrandNotifyBean notifyBean) ;
}
