package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.model.Orders;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:15
 */
@Slf4j
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private AdminOrderDao adminOrderDao;

    @Autowired
    public AdminOrderServiceImpl(AdminOrderDao adminOrderDao) {
        this.adminOrderDao = adminOrderDao;
    }

    /**
     * 导出订单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * @param orderExportReqVo
     * @return
     */
    public List<ExportOrdersVo> exportOrders(OrderExportReqVo orderExportReqVo) throws Exception {
        // 1. 根据条件获取订单orders集合
        // 查询条件转数据库实体
        Orders queryOrders = convertToOrdersForExport(orderExportReqVo);
        log.info("导出订单 根据条件获取订单orders集合 查询数据库条件:{}", JSONUtil.toJsonString(queryOrders));
        // 执行查询
        List<Orders> ordersList = adminOrderDao.selectExportOrders(queryOrders,
                orderExportReqVo.getPayStartDate(), orderExportReqVo.getPayEndDate());

        // 2. 根据上一步，查询子订单


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
