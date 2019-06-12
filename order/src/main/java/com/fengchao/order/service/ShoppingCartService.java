package com.fengchao.order.service;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.model.ShoppingCart;

/**
 * 购物车服务
 */
public interface ShoppingCartService {

    Integer add(ShoppingCart bean) ;

    Integer delete(Integer id) ;

    Integer modifyNum(ShoppingCart bean) ;

    PageBean findList(ShoppingCartQueryBean bean) ;

}
