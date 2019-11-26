package com.fengchao.order.service;

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

    PageBean findListV2(OrderQueryBean queryBean) ;

    Integer updateStatus(Order bean);

    PageBean searchOrderList(OrderBean orderBean);

    Integer updateRemark(Order bean);

    Integer updateOrderAddress(Order bean);

    Order searchDetail(OrderQueryBean queryBean) ;

    Integer uploadLogistics(Logisticsbean bean);

    OperaResult getLogist(String merchantNo, String orderId) ;

    OperaResult getSubOrderLogist(String merchantNo, String subOrderId) ;

    List<Order> findTradeNo(String appId, String merchantNo,String tradeNo) ;

    List<Order> findOutTradeNo(String outTradeNo) ;

    List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) ;

    Integer updatePaymentNo(Order order) ;

    Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) ;

    /**
     * 批量更新子订单的状态
     *
     * @param orderIdList
     * @param status
     * @return
     */
    Integer batchUpdateOrderDetailStatus(List<Integer> orderIdList, Integer status);

    /**
     * 获取平台的关于订单的总体统计数据
     *  1.获取订单支付总额-
     *  2.(已支付)订单总量-
     *  3.(已支付)下单人数-
     *
     * @return
     */
    DayStatisticsBean findOverviewStatistics() throws Exception;

    /**
     * 获取商户的关于订单的整体运营数据
     *
     * 1.获取订单支付总额-
     * 2.(已支付)订单总量-
     * ３.(已支付)下单人数-
     *
     * @param merchantId
     * @return
     * @throws Exception
     */
    DayStatisticsBean findMerchantOverallStatistics(Integer merchantId) throws Exception;

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

//    @Deprecated
//    Integer findDayPaymentCount(String dayStart, String dayEnd) ;

    String findPaymentStatus(String outerTradeNo);

    List<Orders> findByPaymentNoAndOpenId(String paymentNo, String openId) ;

    Integer updateSubOrder(OrderDetail bean);

    List<Orders> findOrderListByOpenId(String openId);

    Integer updateSubOrderStatus(OrderDetail bean);

    Integer subOrderCancel(OrderDetail bean);

    OperaResponse logistics(List<Logisticsbean> logisticsbeans) ;

    OrderDetail findDetailById(int id) ;

    Integer finishOrderDetail(Integer id);

    List<UnPaidBean> unpaid(String openId) ;

    OperaResponse unpaidCancel(String appId, String openId, String orderNos);
}
