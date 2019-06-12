package com.fengchao.order.service;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ReceiverQueryBean;
import com.fengchao.order.model.Receiver;

/**
 * 收货地址服务
 */
public interface ReceiverService {

    Integer add(Receiver bean) ;

    Integer delete(Integer id) ;

    Integer modify(Receiver bean) ;

    PageBean findList(ReceiverQueryBean bean) ;

    Receiver find(Integer id) ;

    Integer setDefault(Receiver bean) ;

}
