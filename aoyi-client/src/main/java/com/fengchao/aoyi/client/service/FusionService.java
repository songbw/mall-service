package com.fengchao.aoyi.client.service;

import com.fengchao.aoyi.client.bean.OperaResponse;

/**
 * @author songbw
 * @date 2021/1/27 17:58
 */
public interface FusionService {

    OperaResponse getAllKiosk() ;

    OperaResponse getAllSoltStatus(String status) ;

}
