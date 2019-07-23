package com.fengchao.order.mapper;

import com.fengchao.order.model.OrderDetailX;

import java.util.HashMap;
import java.util.List;

public interface OrderDetailXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetailX record);

    int insertSelective(OrderDetailX record);

    OrderDetailX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetailX record);

    int updateByPrimaryKey(OrderDetailX record);

    List<OrderDetailX> selectByOrderId(Integer orderId) ;

    List<OrderDetailX> selectLimit(HashMap map) ;

    int selectCount(HashMap map) ;

    int updateByOrderId(OrderDetailX record);

    int updateBySubOrderId(OrderDetailX record);

    List<OrderDetailX> selectBySubOrderId(String orderId);

    String selectComCode(String logisticsId);
}