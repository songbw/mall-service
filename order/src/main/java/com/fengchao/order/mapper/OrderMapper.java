package com.fengchao.order.mapper;

import com.fengchao.order.bean.CategoryPaymentBean;
import com.fengchao.order.bean.MerchantPaymentBean;
import com.fengchao.order.bean.OrderDetailBean;
import com.fengchao.order.bean.PromotionPaymentBean;
import com.fengchao.order.model.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    int selectLimitCount(HashMap map) ;

    List<Order> selectLimit(HashMap map) ;

    int updateStatusById(Order order) ;

    int updatePaymentNo(Order order) ;

    int selectCount(HashMap map) ;

    List<OrderDetailBean> selectOrderLimit(HashMap map);

    List<Order> selectByTradeNo(String tradeNo) ;

    List<Order> selectByOutTradeNo(String outTradeNo) ;

    List<Order> selectByOutTradeNoAndPaymentNo(Order order) ;

    int updatePaymentByOutTradeNoAndPaymentNo(Order order) ;

    int selectDayPaymentCount(HashMap map) ;

    int selectDayCount(HashMap map);

    int selectDayPeopleCount(HashMap map) ;

    List<PromotionPaymentBean> selectDayPromotionPaymentCount(HashMap map) ;

    List<MerchantPaymentBean> selectDayMerchantPaymentCount(HashMap map) ;

    List<CategoryPaymentBean> selectDayCategoryPaymentList(HashMap map) ;
}