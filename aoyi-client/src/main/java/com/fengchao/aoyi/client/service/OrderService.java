package com.fengchao.aoyi.client.service;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.aoyi.client.bean.OrderParamBean;
import com.fengchao.aoyi.client.bean.QueryLogist;
import com.fengchao.aoyi.client.bean.SubOrderT;
import com.fengchao.aoyi.client.exception.AoyiClientException;

import java.util.List;

/**
 * 订单服务
 */
public interface OrderService {

    List<SubOrderT> addOrder(OrderParamBean orderBean) throws AoyiClientException ;

    boolean confirmOrder(String orderId) throws AoyiClientException ;

    JSONArray getOrderLogist(QueryLogist queryLogist) throws AoyiClientException;

}
