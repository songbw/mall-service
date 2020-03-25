package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.WsPayRpcService;
import com.fengchao.order.service.ExportStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportStatisticServiceImpl implements ExportStatisticService {

    private static final Integer LIST_PARTITION_SIZE_200 = 200;
    private static final Integer LIST_PARTITION_SIZE_50 = 50;

    private OrdersDao ordersDao;

    private OrderDetailDao orderDetailDao;

    private WsPayRpcService wsPayRpcService;

    private ProductRpcService productRpcService;

    private WorkOrderRpcService workOrderRpcService;

    @Autowired
    public ExportStatisticServiceImpl(OrdersDao ordersDao,
                                      OrderDetailDao orderDetailDao,
                                      WsPayRpcService wsPayRpcService,
                                      ProductRpcService productRpcService,
                                      WorkOrderRpcService workOrderRpcService) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
        this.wsPayRpcService = wsPayRpcService;
        this.productRpcService = productRpcService;
        this.workOrderRpcService = workOrderRpcService;
    }

    @Override
    public List<ExportReceiptBillVo> exportSettlement(Date startTime, Date endTime,
                                                      List<String> appIdList, Integer merchantId) throws Exception {

        // 查询子订单
        List<OrderDetail> originOrderDetailList =
                orderDetailDao.selectOrderDetailsForReconciliation(merchantId, startTime, endTime);

        if (CollectionUtils.isEmpty(originOrderDetailList)) {
            log.warn("导出货款结算表 数据为空");
            return Collections.emptyList();
        }


        // 查询主订单
        List<Integer> queryOrderIdList = originOrderDetailList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        List<Orders> ordersList = ordersDao.selectOrdersByIdsAndAppIds(queryOrderIdList, appIdList);
        List<Integer> ordersIdList = ordersList.stream().map(o -> o.getId()).collect(Collectors.toList());

        // 过滤子订单
        List<OrderDetail> filterOrderDetailList = new ArrayList<>();
        for (OrderDetail orderDetail : originOrderDetailList) {
            if (ordersIdList.contains(orderDetail.getOrderId())) {
                filterOrderDetailList.add(orderDetail);
            }
        }

        // 遍历
        for (OrderDetail orderDetail : filterOrderDetailList) {
            orderDetail.getSprice() * orderDetail.getNum();
        }

        return null;
    }
}
