package com.fengchao.order.service;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.supply.SupplyOrderBean;

/**
 * @author songbw
 * @date 2021/1/18 16:28
 */
public interface SupplyOrderService {

    OperaResponse preOrder(SupplyOrderBean supplyOrderBean) ;
}
