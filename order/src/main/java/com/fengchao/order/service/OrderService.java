package com.fengchao.order.service;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.order.bean.*;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.model.Order;

import java.util.List;

/**
 * 订单服务
 */
public interface OrderService {

//    List<SubOrder> add(OrderParamBean bean) ;

    List<SubOrderT> add2(OrderParamBean bean);

    Integer cancel(Integer id);

    Order findById(Integer id) ;

    Integer delete(Integer id) ;

    PageBean findList(OrderQueryBean queryBean) ;

    Integer updateStatus(Order bean);

    PageBean searchOrderList(OrderBean orderBean);

    Integer updateRemark(Order bean);

    Integer updateOrderAddress(Order bean);

    PageBean searchDetail(OrderQueryBean queryBean) ;

    Integer uploadLogistics(Logisticsbean bean);

    JSONArray getLogist(String merchantNo, String orderId) ;

    List<Order> findTradeNo(String tradeNo) ;

    List<Order> findOutTradeNo(String outTradeNo) ;

    List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) ;

    Integer updatePaymentNo(Order order) ;

    Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) ;
}
