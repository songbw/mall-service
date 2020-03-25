package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.ExportLoanSettlementVo;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.feign.FreightsServiceClient;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.FreightsRpcService;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.WsPayRpcService;
import com.fengchao.order.rpc.extmodel.ShipRegionsBean;
import com.fengchao.order.rpc.extmodel.ShipTemplateBean;
import com.fengchao.order.rpc.extmodel.WorkOrder;
import com.fengchao.order.service.ExportStatisticService;
import com.fengchao.order.utils.CalculateUtil;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportStatisticServiceImpl implements ExportStatisticService {

    private OrdersDao ordersDao;

    private OrderDetailDao orderDetailDao;

    private FreightsRpcService freightsRpcService;

    private WorkOrderRpcService workOrderRpcService;

    @Autowired
    public ExportStatisticServiceImpl(OrdersDao ordersDao,
                                      OrderDetailDao orderDetailDao,
                                      FreightsRpcService freightsRpcService,
                                      WorkOrderRpcService workOrderRpcService) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
        this.freightsRpcService = freightsRpcService;
        this.workOrderRpcService = workOrderRpcService;
    }

    @Override
    public ExportLoanSettlementVo exportSettlement(Date startTime, Date endTime,
                                                      List<String> appIdList, Integer merchantId) throws Exception {

        // 1. 查询入账子订单
        // 1.1 查询
        List<OrderDetail> originOrderDetailList =
                orderDetailDao.selectOrderDetailsForReconciliation(merchantId, startTime, endTime);

        if (CollectionUtils.isEmpty(originOrderDetailList)) {
            log.warn("导出货款结算表 数据为空");
            return null;
        }


        // 1.2 根据第1步查询主订单
        List<Integer> queryOrderIdList = originOrderDetailList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        List<Orders> ordersList = ordersDao.selectOrdersByIdsAndAppIds(queryOrderIdList, appIdList);
        List<Integer> ordersIdList = ordersList.stream().map(o -> o.getId()).collect(Collectors.toList());

        // 1.3 根据appIds过滤子订单
        List<OrderDetail> incomeOrderDetailList = new ArrayList<>(); // 根据appIds过滤后 剩下的子订单
        for (OrderDetail orderDetail : originOrderDetailList) {
            if (ordersIdList.contains(orderDetail.getOrderId())) {
                incomeOrderDetailList.add(orderDetail);
            }
        }
        if (CollectionUtils.isEmpty(incomeOrderDetailList)) {
            log.warn("导出货款结算表 数据为空!!");
            return null;
        }

        // 2. 查询出账的订单
        // 获取已退款的子订单信息集合
        List<WorkOrder> workOrderList = new ArrayList<>();
        if (CollectionUtils.isEmpty(appIdList)) {
            workOrderList = workOrderRpcService.queryRefundedOrderDetailList(merchantId, null, startTime, endTime);
        } else {
            for (String appId : appIdList) {
                List<WorkOrder> _tmpList = workOrderRpcService.queryRefundedOrderDetailList(merchantId, appId, startTime, endTime);

                workOrderList.addAll(_tmpList);
            }
        }
        if (CollectionUtils.isEmpty(workOrderList)) {
            return null;
        }

        log.info("导出货款结算表 获取退款信息:{}", JSONUtil.toJsonString(workOrderList));

        // 3. 查询运费模版
        List<Integer> merchantIdList = incomeOrderDetailList.stream()
                .map(f -> f.getMerchantId()).collect(Collectors.toList());
        List<ShipTemplateBean> shipTemplateBeanList = freightsRpcService.queryMerchantExceptionFee(merchantIdList);
        // 转map
        Map<Integer, ShipTemplateBean> shipTemplateBeanMap =
                shipTemplateBeanList.stream().collect(Collectors.toMap(s -> s.getMerchantId(), s -> s));


        // 5. 计算导出数据遍历入账的子订单
        String startTimeStr = DateUtil.dateTimeFormat(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        String endTimeStr = DateUtil.dateTimeFormat(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        // 5.1 处理入账
        Integer completeOrderAmount = 0; // 总计完成订单金额
        Integer expressAmount = 0; // 总计快递费
        for (OrderDetail orderDetail : incomeOrderDetailList) {
            expressAmount = expressAmount + calcExpressFee(orderDetail, shipTemplateBeanMap);
            completeOrderAmount = completeOrderAmount +
                    orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).intValue();

        }

        // 5.2 处理出账
        Integer refundOrderAmount = 0;
        for (WorkOrder workOrder : workOrderList) {
            refundOrderAmount = refundOrderAmount +
                    CalculateUtil.convertYuanToFen(String.valueOf(workOrder.getRefundAmount()));
        }

        // 5.3 组装导出数据

        // todo : 查询一下商户名称

        ExportLoanSettlementVo exportLoanSettlementVo = new ExportLoanSettlementVo();
        exportLoanSettlementVo.setMerchantName(""); // 商户名称
        exportLoanSettlementVo.setSettlementPeriod(startTimeStr + "-" + endTimeStr); // 结算周期
        exportLoanSettlementVo.setCompleteOrderAmount(CalculateUtil.converFenToYuan(completeOrderAmount)); // 已完成订单金额 单位元
        exportLoanSettlementVo.setRefundOrderAmount(CalculateUtil.converFenToYuan(refundOrderAmount)); // 已退款订单金额 单位元
        exportLoanSettlementVo.setRealyOrderAmount(CalculateUtil.converFenToYuan(completeOrderAmount - refundOrderAmount)); // 实际成交订单金额 单位元
        exportLoanSettlementVo.setCouponAmount("0"); // 优惠券金额 单位元
        exportLoanSettlementVo.setExpressFee(CalculateUtil.converFenToYuan(expressAmount)); // 运费金额 单位元
        exportLoanSettlementVo.setPayAmout(CalculateUtil.converFenToYuan(completeOrderAmount - refundOrderAmount + expressAmount)); // 应付款 单位元

        log.info("导出货款结算表 组装导出数据ExportLoanSettlementVo:{}", JSONUtil.toJsonString(exportLoanSettlementVo));

        return exportLoanSettlementVo;
    }


    /// ========================== private ======================================

    private Integer calcExpressFee(OrderDetail orderDetail, Map<Integer, ShipTemplateBean> expressFeeMap) {
        Integer fee = 0;

        ShipTemplateBean shipTemplateBean = expressFeeMap.get(orderDetail.getMerchantId());

        if (shipTemplateBean == null) {
            log.warn("商户ID:{} 没有运费模版", orderDetail.getMerchantId());
            return 0;
        }

        List<ShipRegionsBean> shipRegionsBeanList = shipTemplateBean.getRegions();
        if (CollectionUtils.isEmpty(shipRegionsBeanList)) {
            log.warn("商户ID:{} 没有区域运费模版", orderDetail.getMerchantId());
            return 0;
        }

        ShipRegionsBean shipRegionsBean = shipTemplateBean.getRegions().get(0);
        int basePrice = shipRegionsBean.getBasePrice();
        int baseAmount = shipRegionsBean.getBaseAmount();
        int cumulativePrice = shipRegionsBean.getCumulativePrice();
        int cumulativeUnit = shipRegionsBean.getCumulativeUnit();

        //
        int num = orderDetail.getNum();
        if (baseAmount <= num)  {
            fee = basePrice;
        } else {
            fee = basePrice + ((num - baseAmount) / cumulativeUnit) * cumulativePrice;
        }

        return fee;
    }
}


