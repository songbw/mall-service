package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.model.Orders;
import com.fengchao.order.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:15
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private AdminOrderDao adminOrderDao;

    @Autowired
    public AdminOrderServiceImpl(AdminOrderDao adminOrderDao) {
        this.adminOrderDao = adminOrderDao;
    }

    @Override
    public List<Orders> queryExportOrderList(OrderExportReqVo orderExportReqVo) {
        // 转数据库实体
        Orders queryOrders = convertToOrdersForExport(orderExportReqVo);

        return null;
    }

    //=============================== private =============================

    private Orders convertToOrdersForExport(OrderExportReqVo orderExportReqVo) {
        Orders orders = new Orders();

        orders.setTradeNo(orderExportReqVo.getTradeNo());
        // orderExportReqVo.getSubOrderId();
        orders.setMobile(orderExportReqVo.getPhoneNo());
        orders.setStatus(orderExportReqVo.getOrderStatus());

        return orders;
    }
}
