package com.fengchao.aoyi.client.service;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.OrderParamBean;
import com.fengchao.aoyi.client.bean.QueryLogist;
import com.fengchao.aoyi.client.bean.SubOrderT;
import com.fengchao.aoyi.client.exception.AoyiClientException;

import java.util.List;

/**
 * 订单服务
 */
public interface OrderService {

    OperaResult addOrder(OrderParamBean orderBean) ;

    OperaResult confirmOrder(String orderId) ;

    OperaResult getOrderLogist(QueryLogist queryLogist) ;

    OperaResult addOrderGAT(OrderParamBean orderBean) ;

}
