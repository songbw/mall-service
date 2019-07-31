package com.fengchao.order.service;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.order.bean.*;
import com.fengchao.order.model.Order;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;

import java.util.List;

/**
 * 订单服务
 */
public interface OrderService {

//    List<SubOrder> add(OrderParamBean bean) ;

    OperaResult add2(OrderParamBean bean);

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

    DayStatisticsBean findDayStatisticsCount(String dayStart, String dayEnd) ;

    String queryLogisticsInfo(String logisticsId);

    List<PromotionPaymentBean> findDayPromotionPaymentCount(String dayStart, String dayEnd) ;

    /**
     * 按商户统计订单支付总额
     * 
     * @param dayStart
     * @param dayEnd
     * @return
     */
    List<MerchantPaymentBean> findDayMerchantPaymentCount(String dayStart, String dayEnd) ;

    /**
     * 查询已支付的子订单集合
     *
     * @param dayStart 开始时间
     * @param dayEnd 结束时间
     * @return
     */
    List<OrderDetailBean> queryPayedOrderDetail(String dayStart, String dayEnd) throws Exception;

    Integer findDayPaymentCount(String dayStart, String dayEnd) ;

    String findPaymentStatus(String outerTradeNo);

    List<Orders> findByPaymentNoAndOpenId(String paymentNo, String openId) ;

    Integer updateSubOrder(OrderDetail bean);
}
