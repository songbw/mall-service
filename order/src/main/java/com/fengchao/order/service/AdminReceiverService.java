package com.fengchao.order.service;

import com.fengchao.order.model.Receiver;
import com.fengchao.order.model.ShoppingCart;

import java.util.List;

/**
 * 购物车服务
 */
public interface AdminReceiverService {

    List<Receiver> findByOpenId(String openId) ;

    Receiver find(Integer id) ;

}
