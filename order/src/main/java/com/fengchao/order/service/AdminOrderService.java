package com.fengchao.order.service;

import com.fengchao.order.bean.vo.BillExportReqVo;
import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;

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

    /**
     * 导出订单入账对账单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * 1.获取"已完成","已退款"状态的子订单
     * 2.拼装导出数据
     *
     * @param orderExportReqVo
     * @return
     */
    List<ExportOrdersVo> exportOrdersReconciliationIncome(OrderExportReqVo orderExportReqVo) throws Exception;

    /**
     * 导出订单出账对账单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * 1.获取"已退款"状态的子订单
     * 2.拼装导出数据
     *
     * @param orderExportReqVo
     * @return
     */
    List<ExportOrdersVo> exportOrdersReconciliationOut(OrderExportReqVo orderExportReqVo) throws Exception;

    List<OrderPayMethodInfoBean> exportCandRBill(BillExportReqVo billExportReqVo, String tradeType);
}
