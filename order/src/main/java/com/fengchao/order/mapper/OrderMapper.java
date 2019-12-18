package com.fengchao.order.mapper;

import com.fengchao.order.bean.CategoryPaymentBean;
import com.fengchao.order.bean.MerchantPaymentBean;
import com.fengchao.order.bean.OrderDetailBean;
import com.fengchao.order.bean.PromotionPaymentBean;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    int selectLimitCount(HashMap map) ;

    int selectLimitCountV2(HashMap map) ;

    List<Order> selectLimit(HashMap map) ;

    List<Order> selectLimitV2(HashMap map) ;

    int updateStatusById(Order order) ;

    int updatePaymentNo(Order order) ;

    int selectCount(HashMap map) ;

    List<OrderDetailBean> selectOrderLimit(HashMap map);

    List<Order> selectByTradeNo(String tradeNo) ;

    List<Order> selectByOutTradeNo(String outTradeNo) ;

    List<Order> selectByOutTradeNoAndPaymentNo(Order order) ;

    int updatePaymentByOutTradeNoAndPaymentNo(Order order) ;

    /**
     * 查询已支付订单的总额
     *
     * @return
     */
    Float selectPayedOrdersAmount();

    /**
     * 查询已支付的订单总数
     *
     * @return
     */
    int selectPayedOrdersCount();

    /**
     * 查询下单(已支付)人数
     *
     * @return
     */
    int selectPayedOdersUserCount();

    List<PromotionPaymentBean> selectDayPromotionPaymentCount(HashMap map) ;

    /**
     * 按商户统计订单支付总额
     *
     * @param map
     * @return
     */
    List<MerchantPaymentBean> selectDayMerchantPaymentCount(HashMap map) ;

    List<CategoryPaymentBean> selectDayCategoryPaymentList(HashMap map) ;

    AoyiProdIndex selectForUpdateByMpu(String mpu);

    List<Order> selectByOpenIdAndStatus(HashMap map) ;


}