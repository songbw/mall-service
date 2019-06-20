package com.fengchao.order.service;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.RefundOrderQueryBean;
import com.fengchao.order.model.RefundOrder;

import java.util.List;

/**
 * 退换货服务
 */
public interface RefundOrderService {

    Integer add(RefundOrder bean) ;

    Integer update(RefundOrder bean) ;

    Integer delete(Integer id) ;

    Integer updateStatus(RefundOrder bean) ;

    PageBean findList(RefundOrderQueryBean bean) ;

    List<RefundOrder> findBySubOrderId(String subOrderId);

}
