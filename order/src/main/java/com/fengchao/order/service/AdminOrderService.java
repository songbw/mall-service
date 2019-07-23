package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.model.Orders;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:14
 */
public interface AdminOrderService {

    /**
     * 根据条件获取导出的订单列表
     *
     * @param orderExportReqVo
     * @return
     */
    List<ExportOrdersVo> exportOrders(OrderExportReqVo orderExportReqVo) throws Exception;

    /**
     * 注意这是个mock
     *
     * @return
     * @throws Exception
     */
    List<ExportOrdersVo> exportOrdersMock() throws Exception;
}
