package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.model.Platform;

/**
 * @author songbw
 * @date 2019/11/22 10:23
 */
public interface PlatformService {

    OperaResponse add(Platform bean) ;

    OperaResponse delete(Integer id) ;

    OperaResponse modify(Platform bean) ;

    OperaResponse findList(QueryBean bean) ;

    OperaResponse find(Integer id) ;
}
