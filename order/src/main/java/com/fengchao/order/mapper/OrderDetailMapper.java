package com.fengchao.order.mapper;

import com.fengchao.order.model.OrderDetail;

import java.util.HashMap;
import java.util.List;

public interface OrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);

    List<OrderDetail> selectByOrderId(Integer orderId) ;

    List<OrderDetail> selectLimit(HashMap map) ;

    int selectCount(HashMap map) ;

    int updateByOrderId(OrderDetail record);

    int updateBySubOrderId(OrderDetail record);

    List<OrderDetail> selectBySubOrderId(String orderId);
}