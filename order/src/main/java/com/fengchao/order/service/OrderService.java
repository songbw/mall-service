package com.fengchao.order.service;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.order.bean.*;
import com.fengchao.order.model.Order;

import java.util.List;

/**
 * 订单服务
 */
public interface OrderService {

//    List<SubOrder> add(OrderParamBean bean) ;

    List<OrderMerchantBean> add2(OrderParamBean bean);

    Integer cancel(Integer id);

    Order findById(Integer id) ;

    Integer delete(Integer id) ;

    PageBean findList(OrderQueryBean queryBean) ;

    Integer updateStatus(Order bean);

    PageBean searchOrderList(OrderBean orderBean);

    Integer updateRemark(Order bean);

    Integer updateOrderAddress(Order bean);

    Order searchDetail(OrderQueryBean queryBean) ;

    Integer uploadLogistics(Logisticsbean bean);

    JSONArray getLogist(String merchantNo, String orderId) ;

    List<Order> findTradeNo(String appId, String merchantNo,String tradeNo) ;

    List<Order> findOutTradeNo(String outTradeNo) ;

    List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) ;

    Integer updatePaymentNo(Order order) ;

    Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) ;

    DayStatisticsBean findDayStatisticsBean(String dayStart, String dayEnd) ;

    String queryLogisticsInfo(String logisticsId);
}
