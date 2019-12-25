package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.vo.*;
import com.fengchao.order.constants.OrderDetailStatusEnum;
import com.fengchao.order.constants.OrderPayMethodTypeEnum;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.rpc.*;
import com.fengchao.order.rpc.extmodel.*;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author tom
 * @Date 19-7-17 下午3:15
 */
@Slf4j
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private static final Integer LIST_PARTITION_SIZE = 100;

    private static final Integer LIST_PARTITION_SIZE_200 = 200;

    private AdminOrderDao adminOrderDao;

    private EquityRpcService equityRpcService;

    private ProductRpcService productRpcService;

    private VendorsRpcService vendorsRpcService;

    private OrderDetailDao orderDetailDao;

    private OrdersDao ordersDao;

    private WorkOrderRpcService workOrderRpcService;

    private WsPayRpcService wsPayRpcService;

    @Autowired
    public AdminOrderServiceImpl(AdminOrderDao adminOrderDao,
                                 EquityRpcService equityRpcService,
                                 ProductRpcService productRpcService,
                                 VendorsRpcService vendorsRpcService,
                                 OrderDetailDao orderDetailDao,
                                 OrdersDao ordersDao,
                                 WorkOrderRpcService workOrderRpcService,
                                 WsPayRpcService wsPayRpcService) {
        this.adminOrderDao = adminOrderDao;
        this.equityRpcService = equityRpcService;
        this.productRpcService = productRpcService;
        this.vendorsRpcService = vendorsRpcService;
        this.orderDetailDao = orderDetailDao;
        this.ordersDao = ordersDao;
        this.workOrderRpcService = workOrderRpcService;
        this.wsPayRpcService = wsPayRpcService;
    }

    /**
     * 导出订单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * @param orderExportReqVo
     * @return
     */
    @Override
    public List<ExportOrdersVo> exportOrders(OrderExportReqVo orderExportReqVo) throws Exception {
        // 1. 根据条件获取订单orders集合
        // 查询条件转数据库实体
        Orders queryConditions = convertToOrdersForExport(orderExportReqVo);
        log.info("导出订单 根据条件获取订单orders集合 查询数据库条件:{}", JSONUtil.toJsonString(queryConditions));

        // 执行查询
        Date startDate = orderExportReqVo.getPayStartDate();
        Date endDate = orderExportReqVo.getPayEndDate();
        String _start = DateUtil.dateTimeFormat(startDate, DateUtil.DATE_YYYY_MM_DD);
        String _end = DateUtil.dateTimeFormat(endDate, DateUtil.DATE_YYYY_MM_DD);
        Date startDateTime = DateUtil.parseDateTime(_start + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDateTime = DateUtil.parseDateTime(_end + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        List<Orders> ordersList = adminOrderDao.selectExportOrders(queryConditions, startDateTime, endDateTime);
        log.info("导出订单 查询数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        if (CollectionUtils.isEmpty(ordersList)) {
            return Collections.emptyList();
        }

        // 2. 根据上一步，查询子订单(注意条件：子订单号、商家id)
        // 获取主订单id列表
        List<Integer> ordersIdList = new ArrayList<>();
        for (Orders orders : ordersList) {
            ordersIdList.add(orders.getId());
        }

        List<OrderDetail> orderDetailList =
                adminOrderDao.selectExportOrderDetail(ordersIdList, orderExportReqVo.getSubOrderId(), orderExportReqVo.getMerchantId());
        log.debug("导出订单 查询数据库结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        // 支付方式信息   key : paymentNo,  value : 支付方式列表(List<OrderPayMethodInfoBean>)
        List<String> paymentNoList = new ArrayList<>();
        for (Orders orders : ordersList) {
            if (StringUtils.isNotBlank(orders.getPaymentNo())) {
                paymentNoList.add(orders.getPaymentNo());
            }
        }
        List<List<String>> paymentNoPatitionLists = Lists.partition(paymentNoList, LIST_PARTITION_SIZE);

        Map<String, List<OrderPayMethodInfoBean>> paymentMethodInfoMap = new HashMap<>();
        for (List<String> queryPaymentNoList : paymentNoPatitionLists) {
            Map<String, List<OrderPayMethodInfoBean>> _map = wsPayRpcService.queryBatchPayMethod(queryPaymentNoList);

            paymentMethodInfoMap.putAll(_map);
        }

        // 4.获取组装结果的其他相关数据
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList, null, paymentMethodInfoMap , null);

        log.info("导出订单 获取导出结果List<ExportOrdersVo>:{}", JSONUtil.toJsonString(exportOrdersVoList));

        return exportOrdersVoList;
    }

    /**
     * 导出订单入账对账单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * 1.获取"已完成","已退款"状态的子订单
     * 2.拼装导出数据
     *
     * @param orderExportReqVo
     * @return
     */
    @Override
    public List<ExportOrdersVo> exportOrdersReconciliationIncome(OrderExportReqVo orderExportReqVo) throws Exception {
        // 1. 查询"已完成" "已取消，申请售后"状态的订单(入账的订单)
        // 执行查询
        Date startDate = orderExportReqVo.getPayStartDate();
        Date endDate = orderExportReqVo.getPayEndDate();
        String _start = DateUtil.dateTimeFormat(startDate, DateUtil.DATE_YYYY_MM_DD);
        String _end = DateUtil.dateTimeFormat(endDate, DateUtil.DATE_YYYY_MM_DD);
        Date startDateTime = DateUtil.parseDateTime(_start + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDateTime = DateUtil.parseDateTime(_end + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsForReconciliation(orderExportReqVo.getMerchantId(), startDateTime, endDateTime);
        log.debug("导出订单对账单(入账) 查询数据库入账的子订单 结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        if (CollectionUtils.isEmpty(orderDetailList)) {
            return Collections.emptyList();
        }

        // 2. 根据上一步，查询主订单
        // 获取主订单id列表
        List<Integer> ordersIdList = orderDetailList.stream().map(d -> d.getOrderId()).collect(Collectors.toList());
        // 查询主订单
        List<Orders> ordersList = ordersDao.selectOrdersListByIdList(ordersIdList);
        log.debug("导出订单对账单(入账) 查询主订单 数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        // 支付方式信息   key : paymentNo,  value : 支付方式列表(List<OrderPayMethodInfoBean>)
        List<String> paymentNoList = new ArrayList<>();
        for (Orders orders : ordersList) {
            if (StringUtils.isNotBlank(orders.getPaymentNo())) {
                paymentNoList.add(orders.getPaymentNo());
            }
        }
        List<List<String>> paymentNoPatitionLists = Lists.partition(paymentNoList, LIST_PARTITION_SIZE);

        Map<String, List<OrderPayMethodInfoBean>> paymentMethodInfoMap = new HashMap<>();
        for (List<String> queryPaymentNoList : paymentNoPatitionLists) {
            Map<String, List<OrderPayMethodInfoBean>> _map = wsPayRpcService.queryBatchPayMethod(queryPaymentNoList);

            paymentMethodInfoMap.putAll(_map);
        }

        // 3.组装结果
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList, null, paymentMethodInfoMap, null);

        // patch, 将状态统一为"已退款"
        if (CollectionUtils.isNotEmpty(exportOrdersVoList)) {
            exportOrdersVoList.stream().forEach(e -> e.setOrderDetailStatus("已完成"));
        }

        log.info("导出订单对账单(入账) 获取导出结果List<ExportOrdersVo>:{}", JSONUtil.toJsonString(exportOrdersVoList));

        return exportOrdersVoList;
    }

    /**
     * 导出订单出账对账单 - 获取导出的vo : List<ExportOrdersVo>
     *
     * 1.获取"已完成","已退款"状态的子订单
     * 2.拼装导出数据
     *
     * @param orderExportReqVo
     * @return
     */
    @Override
    public List<ExportOrdersVo> exportOrdersReconciliationOut(OrderExportReqVo orderExportReqVo) throws Exception {
        // 1. 获取"已退款"状态的子订单(出账的订单)
        // 执行查询
        Date startDate = orderExportReqVo.getPayStartDate();
        Date endDate = orderExportReqVo.getPayEndDate();
        String _start = DateUtil.dateTimeFormat(startDate, DateUtil.DATE_YYYY_MM_DD);
        String _end = DateUtil.dateTimeFormat(endDate, DateUtil.DATE_YYYY_MM_DD);
        Date startDateTime = DateUtil.parseDateTime(_start + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDateTime = DateUtil.parseDateTime(_end + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        // 1.1 获取已退款的子订单信息集合
        List<WorkOrder> workOrderList =
                workOrderRpcService.queryRefundedOrderDetailList(orderExportReqVo.getMerchantId(), startDateTime, endDateTime);
        if (CollectionUtils.isEmpty(workOrderList)) {
            return Collections.emptyList();
        }

        // 1.1.1 获取子订单id集合
        // 1.1.2 获取子订单退款金额map , key: 子订单id， value: 退款金额
        List<String> orderDetailNoList = new ArrayList<>();
        Map<String, Float> orderDetailRefundAmountMap = new HashMap<>();
        // 1.1.3 获取主订单的退款方式信息
        List<String> refundNoList = new ArrayList<>();
        for (WorkOrder workOrder : workOrderList) {
            orderDetailNoList.add(workOrder.getOrderId());
            orderDetailRefundAmountMap.put(workOrder.getOrderId(), workOrder.getGuanaitongRefundAmount()); // 单位元
            if (StringUtils.isNotBlank(workOrder.getGuanaitongTradeNo())) {
                refundNoList.add(workOrder.getGuanaitongTradeNo());
            }
        }

        log.info("导出订单对账单(出账) 获取子订单的退款金额map:{}", JSONUtil.toJsonString(orderDetailRefundAmountMap));

        // 1.2 根据子订单No查询子订单信息
        List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailListBySubOrderIds(orderDetailNoList);
        log.info("导出订单对账单(出账) 查询已退款的子订单 结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        if (CollectionUtils.isEmpty(orderDetailList)) {
            return Collections.emptyList();
        }

        // 2. 根据上一步，查询主订单
        // 获取主订单id列表
        List<Integer> ordersIdList = orderDetailList.stream().map(d -> d.getOrderId()).collect(Collectors.toList());
        // 查询主订单
        List<Orders> ordersList = ordersDao.selectOrdersListByIdList(ordersIdList);
        log.info("导出订单对账单(出账) 查询已退款的主订单 数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        List<List<String>> refundNoPatitionLists = Lists.partition(refundNoList, LIST_PARTITION_SIZE);
        // 退款方式信息   key : paymentNo,  value : 支付方式列表(List<OrderPayMethodInfoBean>)
        Map<String, List<RefundMethodInfoBean>> refundMethodInfoMap = new HashMap<>();
        for (List<String> queryRefundNoList : refundNoPatitionLists) {
            Map<String, List<RefundMethodInfoBean>> _map = wsPayRpcService.queryBatchRefundMethod(queryRefundNoList);
            refundMethodInfoMap.putAll(_map);
        }

        Map<String, List<RefundMethodInfoBean>> refundNoOrderInfoMap = new HashMap<>();
        for (WorkOrder workOrder : workOrderList) {
            List<RefundMethodInfoBean> refundMethodInfoBeans = refundMethodInfoMap.get(workOrder.getGuanaitongTradeNo());
            if(CollectionUtils.isNotEmpty(refundMethodInfoBeans)){
                refundNoOrderInfoMap.put(workOrder.getOrderId(), refundMethodInfoBeans);
            }
        }

        // 3.组装结果!!!
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList, orderDetailRefundAmountMap,null, refundNoOrderInfoMap);

        // patch, 将状态统一为"已退款"
        if (CollectionUtils.isNotEmpty(exportOrdersVoList)) {
            exportOrdersVoList.stream().forEach(e -> e.setOrderDetailStatus("已退款"));
        }

        log.info("导出订单对账单(出账) 获取导出结果List<ExportOrdersVo>:{}", JSONUtil.toJsonString(exportOrdersVoList));

        return exportOrdersVoList;
    }

    @Override
    public List<OrderPayMethodInfoBean> exportCandRBill(BillExportReqVo billExportReqVo, String tradeType) {
        billExportReqVo.setTradeType(tradeType);
        billExportReqVo.setPageNum(1);
        List<OrderPayMethodInfoBean> payMethodInfoBeans = wsPayRpcService.queryPayCandRList(billExportReqVo);
        return payMethodInfoBeans;
    }


    @Override
    public List<ExportOrdersVo> exportOrdersMock() {
        List<ExportOrdersVo> exportOrdersVoList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ExportOrdersVo exportOrdersVo = new ExportOrdersVo();

            exportOrdersVo.setOpenId("openid_" + i);
            exportOrdersVo.setTradeNo("tradeno_" + i);
            exportOrdersVo.setSubOrderId("suborderid_" + i);
            exportOrdersVo.setPaymentTime(new Date());
            exportOrdersVo.setCreateTime(new Date());
            exportOrdersVo.setCategory("category_" + i);
            exportOrdersVo.setBrand("brand_" + i);
            exportOrdersVo.setSku("sku_" + i);
            exportOrdersVo.setMpu("mpu_" + i);
            exportOrdersVo.setCommodityName("commodiyname_" + 1);
            exportOrdersVo.setQuantity(i);
            exportOrdersVo.setPromotion("promotion_" + i);
            exportOrdersVo.setPromotionId(Long.valueOf(i));
            exportOrdersVo.setCouponCode("couponcode_" + i);
            exportOrdersVo.setCouponId(Long.valueOf(i));
            exportOrdersVo.setCouponSupplier("couponsupplier_" + i);
            exportOrdersVo.setPurchasePrice(i);
            exportOrdersVo.setUnitPrice(i);
            exportOrdersVo.setSkuPayPrice(i);
            exportOrdersVo.setCouponPrice(i);
            exportOrdersVo.setPayPrice(i);
            exportOrdersVo.setShareBenefitPercent("/");
            exportOrdersVo.setBuyerName("buyername_" + i);
            exportOrdersVo.setProvinceName("provincename_" + i);
            exportOrdersVo.setCityName("cityname_" + i);
            exportOrdersVo.setCountyName("countyname_" + i);

            exportOrdersVoList.add(exportOrdersVo);
        }

        return exportOrdersVoList;
    }


    /**
     * 导出每日统计
     *
     * @return
     */
    @Override
    public Map<String, Object> exportDailyOrderStatistic() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<DailyExportOrderStatisticVo> dailyExportOrderStatisticVoList = new ArrayList<>();

            // 获取所有供应商
            List<SysCompanyX> sysCompanyXList = vendorsRpcService.queryAllCompanyList();

            log.info("每日统计 获取商户列表:{}", JSONUtil.toJsonString(sysCompanyXList));


            //
            long totalCompletedOrderCount = 0; // 总计 已完成子订单数量
            long totalDeliveredOrderCount = 0; // 总计 已发货子订单数量
            long totalUnDeliveryOrderCount = 0; // 总计 未发货子订单数量
            long totalApplyRefundOrderCount = 0; // 总计 售后子订单数量
            long totalOrderDetailCount = 0; // 总计 所有子订单数量
            for (SysCompanyX sysCompanyX : sysCompanyXList) { // 0：已下单；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：已取消，申请售后
                DailyExportOrderStatisticVo dailyExportOrderStatisticVo = new DailyExportOrderStatisticVo();

                // 1. 供应商名称
                dailyExportOrderStatisticVo.setSupplierName(sysCompanyX.getName());

                // 2. 统计已完成子订单个数
                Long completedOrderCount = orderDetailDao.selectCountInSupplierAndStatus(sysCompanyX.getId().intValue(), 3);
                dailyExportOrderStatisticVo.setCompletedOrderCount(completedOrderCount);
                totalCompletedOrderCount = totalCompletedOrderCount + completedOrderCount;

                // 3. 统计已发货子订单数量
                Long deliveredOrderCount = orderDetailDao.selectCountInSupplierAndStatus(sysCompanyX.getId().intValue(), 2);
                dailyExportOrderStatisticVo.setDeliveredOrderCount(deliveredOrderCount);
                totalDeliveredOrderCount = totalDeliveredOrderCount + deliveredOrderCount;

                // x.获取待发货的子订单
                List<OrderDetail> unDeliveryOrderDetailList = orderDetailDao.selectOrderDetailsInSupplierAndStatus(sysCompanyX.getId().intValue(), 1);
                totalUnDeliveryOrderCount = totalUnDeliveryOrderCount + unDeliveryOrderDetailList.size();

                // 4. 统计待发货子订单数量
                Long unDeliveryOrderCount = Long.valueOf(unDeliveryOrderDetailList.size());
                dailyExportOrderStatisticVo.setUnDeliveryOrderCount(unDeliveryOrderCount);

                if (CollectionUtils.isNotEmpty(unDeliveryOrderDetailList)) {
                    // 5. 待发货的订单中，最早的子订单号
                    String unDeliveryEarliestOrderNo = unDeliveryOrderDetailList.get(0).getSubOrderId();
                    dailyExportOrderStatisticVo.setUnDeliveryEarliestOrderNo(unDeliveryEarliestOrderNo);

                    // 6. 待发货的订单中，最早的子订单交易时间
                    String unDeliveryEarliestOrderTime =
                            DateUtil.dateTimeFormat(unDeliveryOrderDetailList.get(0).getCreatedAt(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
                    dailyExportOrderStatisticVo.setUnDeliveryEarliestOrderTime(unDeliveryEarliestOrderTime);
                }

                // 7. 统计申请售后的子点单数量
                Long applyRefundCount = orderDetailDao.selectCountInSupplierAndStatus(sysCompanyX.getId().intValue(), 5);
                dailyExportOrderStatisticVo.setApplyRefundCount(applyRefundCount);
                totalApplyRefundOrderCount = totalApplyRefundOrderCount + applyRefundCount;

                // 8. 统计所有订单数量
                List<Integer> statusList = new ArrayList<>(); // 0：已下单；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：已取消，申请售后
                statusList.add(0);
                statusList.add(1);
                statusList.add(2);
                statusList.add(3);
                statusList.add(5);
                Long orderDetailCount = orderDetailDao.selectCountInSupplierAndStatusList(sysCompanyX.getId().intValue(), statusList);
                dailyExportOrderStatisticVo.setOrderDetailCount(orderDetailCount);
                totalOrderDetailCount = totalOrderDetailCount + orderDetailCount;

                //// !!
                dailyExportOrderStatisticVoList.add(dailyExportOrderStatisticVo);
            }

            dailyExportOrderStatisticVoList.sort((o1, o2) ->
                    o2.getUnDeliveryOrderCount().intValue() - o1.getUnDeliveryOrderCount().intValue());
            // dailyExportOrderStatisticVoList.sort(Comparator.comparing(DailyExportOrderStatisticVo::getUnDeliveryOrderCount));


            log.info("每日统计 获取导出的data数据:{}", JSONUtil.toJsonString(dailyExportOrderStatisticVoList));

            // 查询从0点到目前时间的新增订单
            String startTime = DateUtil.nowDate(DateUtil.DATE_YYYY_MM_DD) + " 00:00:00";
            Date startTimeDate = DateUtil.parseDateTime(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            List<OrderDetail> increasedOrderDetailList = orderDetailDao.selectOrderDetailsByPeriod(startTimeDate, new Date());

            // 处理返回结果
            resultMap.put("statisticTime", DateUtil.nowDate(DateUtil.DATE_YYYYMMDDHHMMSS));
            resultMap.put("data", dailyExportOrderStatisticVoList);
            resultMap.put("increasedCount", increasedOrderDetailList.size());
            resultMap.put("totalCompletedOrderCount", totalCompletedOrderCount); // 总计 已完成子订单数量
            resultMap.put("totalDeliveredOrderCount", totalDeliveredOrderCount); // 总计 已发货子订单数量
            resultMap.put("totalUnDeliveryOrderCount", totalUnDeliveryOrderCount); // 总计 未发货子订单数量
            resultMap.put("totalApplyRefundOrderCount", totalApplyRefundOrderCount); // 总计 售后子订单数量
            resultMap.put("totalOrderDetailCount", totalOrderDetailCount); // 总计 所有子订单数量

            log.info("每日统计 统计数据:{}", JSONUtil.toJsonString(resultMap));
            return resultMap;
        } catch (Exception e) {
            log.error("每日统计 异常:{}", e.getMessage(), e);
            throw e;
        }
    }



    /**
     * 导出商品开票信息
     *
     * @return
     */
    @Override
    public List<ExportReceiptBillVo> exportReceiptBill(Date startTime, Date endTime, String appId) throws Exception {
        try {
            // 1. 首先查询符合条件的子订单
            List<OrderDetail> orderDetailList =
                    orderDetailDao.selectOrderDetailsByPeriod(startTime, endTime, appId, OrderDetailStatusEnum.COMPLETED.getValue());

            log.info("导出商品开票信息 查询日期条件的子订单的个数是:{}", orderDetailList.size());

            if (CollectionUtils.isEmpty(orderDetailList)) {
                log.warn("导出商品开票信息 未找到符合条件的子订单");
                return Collections.emptyList();
            }

            // x. 转map key:主订单id， value:List<OrderDetail>
            Map<Integer, List<OrderDetail>> orderDetailMap = new HashMap<>();
            for (OrderDetail orderDetail : orderDetailList) {
                Integer orderId = orderDetail.getOrderId();
                if (orderId != null) {
                    List<OrderDetail> _list = orderDetailMap.get(orderId);
                    if (_list == null) {
                        _list = new ArrayList<>();
                        orderDetailMap.put(orderId, _list);
                    }

                    _list.add(orderDetail);
                } else {
                    log.warn("导出商品开票信息 自订单id:{} 无主订单id", orderDetail.getId());
                }
            }

            // 2. 查询主订单
            // 获取主订单id集合
            List<Integer> orderIdList = orderDetailList.stream().map(od -> od.getOrderId()).collect(Collectors.toList());
            // 分区
            List<List<Integer>> orderIdListPartition = Lists.partition(orderIdList, LIST_PARTITION_SIZE_200);

            // 根据主订单id集合 查询主订单信息
            List<Orders> ordersList = new ArrayList<>(); //
            for (List<Integer> _orderIdList : orderIdListPartition) {
                List<Orders> _ordersList = ordersDao.selectOrdersListByIdList(_orderIdList);
                ordersList.addAll(_ordersList);
            }

            log.info("导出商品开票信息 找到主订单个数是:{}", ordersList.size());

            // x. 转map, key:paymentNo, value:List<Orders>
            Map<String, List<Orders>> ordersMap = new HashMap<>();
            for (Orders _orders : ordersList) {
                String paymentNo = _orders.getPaymentNo();
                if (StringUtils.isNotBlank(paymentNo)) {
                    List<Orders> _list = ordersMap.get(paymentNo);
                    if (_list == null) {
                        _list = new ArrayList<>();

                        ordersMap.put(paymentNo, _list);
                    }

                    _list.add(_orders);
                } else {
                    log.warn("导出商品开票信息 主订单id:{} 的paymentNo为空", _orders.getId());
                }
            }


            // 3. 根据payNo，查询支付信息
            // 获取payNo
            List<String> paymentNoList = ordersList.stream().map(o -> o.getPaymentNo()).collect(Collectors.toList());
            // 分区
            List<List<String>> paymentNoListPartition = Lists.partition(paymentNoList, LIST_PARTITION_SIZE_200);

            // 根据payNo集合 查询支付信息
            Map<String, List<OrderPayMethodInfoBean>> paymentMethodMap = new HashMap<>(); //
            for (List<String> _paymentNoList : paymentNoListPartition) {
                // key : paymentNo
                Map<String, List<OrderPayMethodInfoBean>> _paymentMethodMap = wsPayRpcService.queryBatchPayMethod(_paymentNoList);

                paymentMethodMap.putAll(_paymentMethodMap);
            }

            log.info("导出商品开票信息 找到用户单(支付单)个数是:{}", paymentMethodMap.size());

            // 4.开始计算，计算逻辑：
            // a.遍历支付订单(用户单)，(同时过滤掉其他的支付方式)
            // b.将支付订单(用户单)下的子订单以mpu为维度合并，同时合并的信息有: 商品数量 / 商品的总价格; 生成数据结构:Map<String, paymentInfoByMpuDimension> key: mpu value: paymentInfoByMpuDimension
            // c.将每个用户单生成的Map<String, paymentInfoByMpuDimension> 加入到一个list容器中

            List<Map<String, PaymentInfoByMpuDimension>> container = new ArrayList<>(); // 将每个处理完的用户单，放入该容器
            // a.以支付单号为维度遍历; 遍历用户单
            for (String paymentNo : paymentMethodMap.keySet()) { // 遍历支付订单(用户单)
                Map<String, PaymentInfoByMpuDimension> stringpaymentInfoByMpuDimensionMap = new HashMap<>(); ////// 数据结构 key: mpu value: paymentInfoByMpuDimension; 表示用户单中的以mpu为维度的子订单
                Integer totalAmount = 0; // 该用户单(支付订单)下，所有商品的一个总价 num * salePrice 单位分

                // 判断该支付单的支付方式 返回的是所选择的支付方式支付的总额
                Integer payAmount = judgeBanlanceCardWoaPayment(paymentMethodMap.get(paymentNo)); // "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式的支付总额
                if (payAmount >= 0) { // 说明该用户单(支付单)在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式中
                    // 遍历主订单
                    for (Orders _orders : ordersMap.get(paymentNo)) { // 遍历主订单
                        // 遍历子订单
                        for (OrderDetail _orderDetail : orderDetailMap.get(_orders.getId())) { // 遍历子订单
                            PaymentInfoByMpuDimension _paymentInfoByMpuDimension = stringpaymentInfoByMpuDimensionMap.get(_orderDetail.getMpu());
                            if (_paymentInfoByMpuDimension == null) {
                                _paymentInfoByMpuDimension = new PaymentInfoByMpuDimension();
                                _paymentInfoByMpuDimension.setMpu(_orderDetail.getMpu()); // 设置mpu

                                stringpaymentInfoByMpuDimensionMap.put(_orderDetail.getMpu(), _paymentInfoByMpuDimension);
                            }
                            // 商品数量
                            int _c = _paymentInfoByMpuDimension.getCount() == null ? 0 : _paymentInfoByMpuDimension.getCount();
                            _paymentInfoByMpuDimension.setCount(_c + _orderDetail.getNum());
                            // 商品总价 单位分
                            Integer _tp = _paymentInfoByMpuDimension.getTotalPrice() == null ? 0 : _paymentInfoByMpuDimension.getTotalPrice();
                            _paymentInfoByMpuDimension.setTotalPrice(_tp + convertYuanToFen(_orderDetail.getSalePrice().toString()) * _orderDetail.getNum());

                            totalAmount = totalAmount + _paymentInfoByMpuDimension.getTotalPrice(); // 单位 分
                        } // end 遍历子订单
                    } // end 遍历主订单

                    // 开始计算 每个mpu所占的payAmount的份额
                    BigDecimal multiplier = new BigDecimal(payAmount).divide(new BigDecimal(totalAmount));
                    // 遍历最终产生的数据结构:Map<String, paymentInfoByMpuDimension> 其实就是用户单下所有子订单 以mpu为维度的map
                    int index = 0;
                    int remainder = payAmount;
                    for (String mpu : stringpaymentInfoByMpuDimensionMap.keySet()) {
                        PaymentInfoByMpuDimension _paymentInfoByMpuDimension = stringpaymentInfoByMpuDimensionMap.get(mpu);

                        // 该mpu商品所占 指定支付方式的份额 单位分
                        if (index == stringpaymentInfoByMpuDimensionMap.size() - 1) { // 是最后一个元素
                            _paymentInfoByMpuDimension.setHoldAmount(remainder <= 0 ? 0 : remainder);
                        } else {
                            // 该mpu商品所占 指定支付方式的份额 单位分
                            Integer holdAmount = multiplier.multiply(new BigDecimal(_paymentInfoByMpuDimension.getTotalPrice())).intValue();

                            _paymentInfoByMpuDimension.setHoldAmount(holdAmount);

                            remainder = remainder - holdAmount;
                        }

                        index++;
                    }

                    container.add(stringpaymentInfoByMpuDimensionMap);
                } // end fi payAmount >= 0
            } // end 遍历支付订单

            // x. 如果没有指定支付方式的用户单数据
            if (CollectionUtils.isEmpty(container)) {
                log.info("导出商品开票信息 没有找到指定支付方式的用户单数据");
                return Collections.emptyList();
            }

            // 5. 生产需要导出的vo
            Map<String, ExportReceiptBillVo> exportReceiptBillVoMap = new HashMap<>(); // key: mpu value: ExportReceiptBillVo
            for (Map<String, PaymentInfoByMpuDimension> _map : container) { // 遍历用户单 container中每一个元素就是一个用户单
                // 遍历map(其实是在遍历每个用户单下的所有子订单(mpu))
                for (String _mpu : _map.keySet()) {
                    ExportReceiptBillVo _exportReceiptBillVo = exportReceiptBillVoMap.get(_mpu);
                    if (_exportReceiptBillVo == null) {
                        _exportReceiptBillVo = new ExportReceiptBillVo();
                        _exportReceiptBillVo.setMpu(_mpu);

                        exportReceiptBillVoMap.put(_mpu, _exportReceiptBillVo);
                    }

                    // 一个用户单下的一个mpu的商品信息
                    PaymentInfoByMpuDimension _paymentInfoByMpuDimension = _map.get(_mpu);

                    // 数量
                    int _count = _exportReceiptBillVo.getCount() == null ? 0 : _exportReceiptBillVo.getCount();
                    _exportReceiptBillVo.setCount(_count + _paymentInfoByMpuDimension.getCount()); // 数量

                    // 含税总额 单位分
                    int totalPrice = _exportReceiptBillVo.getTotalPrice() == null ? 0 : _exportReceiptBillVo.getTotalPrice();
                    _exportReceiptBillVo.setTotalPrice(totalPrice + _paymentInfoByMpuDimension.getTotalPrice()); // 含税总额 单位元

                    ////
                    _exportReceiptBillVo.setTaxPrice(0); // 税额 单位分
                    _exportReceiptBillVo.setUnitPrice(0); // 含税单价 单位分

                    _exportReceiptBillVo.setName("--"); // 商品名称
                    _exportReceiptBillVo.setCategory("--"); // 品类名称
                    _exportReceiptBillVo.setUnit("--"); // 销售单位
                    _exportReceiptBillVo.setTaxRate("--"); // 税率
                }
            }

            // x. 根据mpu查询一下商品信息
            List<ProductInfoBean> productInfoBeanList = new ArrayList<>();
            List<String> mpuList = new ArrayList<>(exportReceiptBillVoMap.keySet());
            // 分区
            List<List<String>> mpuPartition = Lists.partition(mpuList, LIST_PARTITION_SIZE_200);
            for (List<String> _mpuList : mpuPartition) {
                List<ProductInfoBean> _productInfoBeanList = productRpcService.findProductListByMpus(_mpuList);

                productInfoBeanList.addAll(_productInfoBeanList);
            }
            log.info("导出商品开票信息 获取到的商品信息个数是:{}", productInfoBeanList.size());

            // 转map, key: mpu, value: ProductInfoBean
            Map<String, ProductInfoBean> productInfoBeanMap = productInfoBeanList.stream().collect(Collectors.toMap(p -> p.getMpu(), p -> p));

            // 6. 计算税额和含税单价
            List<ExportReceiptBillVo> result = new ArrayList<>(); // 该方法的返回
            for (String _mpu : exportReceiptBillVoMap.keySet()) {
                ExportReceiptBillVo _exportReceiptBillVo = exportReceiptBillVoMap.get(_mpu);

                // 获取商品信息
                ProductInfoBean productInfoBean = productInfoBeanMap.get(_mpu);
                if (productInfoBean != null) {
                    _exportReceiptBillVo.setName(productInfoBean.getName()); // 商品名称
                    _exportReceiptBillVo.setCategory(productInfoBean.getCategoryName()); // 品类名称
                    _exportReceiptBillVo.setUnit(productInfoBean.getSaleunit()); // 销售单位

                    // 税率 TODO 看一下如果税率是空，rpc后是什么情况
                    String taxRate = StringUtils.isBlank(productInfoBean.getTaxRate()) ? null : productInfoBean.getTaxRate();
                    _exportReceiptBillVo.setTaxRate(StringUtils.isBlank(taxRate) ? "--" : taxRate);

                    // !!税额 单位分
                    if (StringUtils.isNotBlank(taxRate)) {
                        int taxPrice = new BigDecimal(_exportReceiptBillVo.getTotalPrice())
                                .divide(new BigDecimal(taxRate).add(new BigDecimal(1)))
                                .multiply(new BigDecimal(taxRate)).intValue(); // 单位分
                        _exportReceiptBillVo.setTaxPrice(taxPrice);
                    }

                    // !!含税单价 单位分
                    int unitPrice = new BigDecimal(_exportReceiptBillVo.getTotalPrice())
                            .divide(new BigDecimal(_exportReceiptBillVo.getCount())).intValue();
                    _exportReceiptBillVo.setUnitPrice(unitPrice); // 含税单价 单位分
                }

                result.add(_exportReceiptBillVo);
            }

            log.info("导出商品开票信息 组装的List<ExportReceiptBillVo>:{}", JSONUtil.toJsonString(result));

            return result;
        } catch (Exception e) {
            log.error("导出商品开票信息 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    //=============================== private =============================

    /**
     * (一个支付订单号下的) 一个用户单下 以mpu为维度, 商品数量,
     */
    @Setter
    @Getter
    private class PaymentInfoByMpuDimension {

        private String mpu;

        /**
         * (该支付订单下) 以mpu为维度的 商品数量
         */
        private Integer count;

        /**
         * (该支付订单下) 以mpu为维度的 商品价格总计
         *
         * salePrice * num
         */
        private Integer totalPrice;

        /**
         * (该支付订单下) 以mpu为维度的 分得的支付额度
         *
         * 举例:
         * 支付订单PNO下, 支付金额是payAmount(比如是woa/card/banlance); mpuA的totalPrice 为20, mpuB的totalPrice为30
         * mpuA所占的holdAmount是: (payAmount / (20 + 30) ) * 20
         */
        private Integer holdAmount;
    }

    /**
     * 元 转 分
     *
     * @param yuan
     * @return
     */
    public static Integer convertYuanToFen(String yuan) {
        if (StringUtils.isBlank(yuan)) {
            return null;
        }

        int fen = new BigDecimal(yuan).multiply(new BigDecimal(100)).intValue();

        return fen;
    }

    /**
     * 分 转 元
     *
     * @param fen
     * @return
     */
    public static String converFenToYuan(Integer fen) {
        if (fen == null) {
            return null;
        }

        String yuan = new BigDecimal(fen).divide(new BigDecimal(100)).toString();

        return yuan;
    }

    /**
     * 判断其支付方式是否是 "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户
     *
     * @param orderPayMethodInfoBeanList 某个支付单号下的支付方式的集合; 换一种表述，这是一个用户单下的支付方式
     * @return
     * -1(<0) : 说明该支付订单的支付方式不在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式中
     * 其他值(>=0) : 返回该支付单子在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式
     */
    private Integer judgeBanlanceCardWoaPayment(List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList) {
        Integer total = -1; // "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户  支付的总额 单位 分

        // 遍历支付方式
        for (OrderPayMethodInfoBean orderPayMethodInfoBean : orderPayMethodInfoBeanList) {
            String payType = orderPayMethodInfoBean.getPayType();

            if (PaymentTypeEnum.BALANCE.getName().equals(payType)
                || PaymentTypeEnum.CARD.getName().equals(payType)
                || PaymentTypeEnum.WOA.getName().equals(payType)) {
                if (total < 0) {
                    total = 0;
                }

                if (StringUtils.isNotBlank(orderPayMethodInfoBean.getActPayFee())) { // 单位分
                    total = total + Integer.valueOf(orderPayMethodInfoBean.getActPayFee());
                } else {
                    log.warn("判断其支付方式是否是 balance card woa 支付金额不合法:{}", orderPayMethodInfoBean.getActPayFee());
                }
            }
        }

        return total;
    }

    /**
     * 组装订单导出数据
     *
     * @param ordersList
     * @param orderDetailList
     * @param orderDetailRefundAmountMap 子订单的退款金额map , key: 子订单id， value: 退款金额
     * @return
     */
    private List<ExportOrdersVo> assembleExportOrderData(List<Orders> ordersList,
                                                         List<OrderDetail> orderDetailList,
                                                         Map<String, Float> orderDetailRefundAmountMap,
                                                         Map<String, List<OrderPayMethodInfoBean>> paymentMethodInfoMap,
                                                         Map<String, List<RefundMethodInfoBean>> refundMethodInfoMap) {
        // x. 获取组装结果的其他相关数据 - 获取导出需要的coupon信息列表 - 获取coupon的id集合
        Set<Integer> couponUseInfoIdSet = new HashSet<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的支付方式信息 - 获取coupon的id集合
        List<String> paymentNoList = new ArrayList<>();
        for (Orders orders : ordersList) {
            if (orders.getCouponId() != null) {
                couponUseInfoIdSet.add(orders.getCouponId());
            }

            if (StringUtils.isNotBlank(orders.getPaymentNo())) {
                paymentNoList.add(orders.getPaymentNo());
            }
        }

        // 2. 将获取到的子订单转成map key：主订单id  value：List<OrderDtailBo>
        Map<Integer, List<OrderDetailBo>> orderDetailBoMap = new HashMap<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的promotion信息列表 - 获取promotion id集合
        Set<Integer> promotionIdSet = new HashSet<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的product信息列表 - 获取mpu id集合
        Set<String> mpuIdList = new HashSet<>();
        for (OrderDetail orderDetail : orderDetailList) {
            // 转bo
            OrderDetailBo orderDetailBo = convertToOrderDetailBo(orderDetail);
            // 设置退款金额
            if (orderDetailRefundAmountMap != null) {
                Float refundAmount = orderDetailRefundAmountMap.get(orderDetail.getSubOrderId());
                if (refundAmount != null) {
                    orderDetailBo.setRefundAmount(new BigDecimal(refundAmount).toPlainString());
                }
            }

            // 放入map
            if (orderDetailBoMap.get(orderDetail.getOrderId()) == null) {
                orderDetailBoMap.put(orderDetail.getOrderId(), new ArrayList<>());
            }
            orderDetailBoMap.get(orderDetail.getOrderId()).add(orderDetailBo);

            // x. 获取组装结果的其他相关数据 - 获取导出需要的promotion信息列表 - 获取promotion id集合
            promotionIdSet.add(orderDetailBo.getPromotionId());
            // x. 获取组装结果的其他相关数据 - 获取导出需要的product信息列表 - 获取mpu id集合
            mpuIdList.add(orderDetailBo.getMpu());
        }

        // 3. 将获取的主订单列表转bo, 并加入子订单信息
        List<OrdersBo> ordersBoList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersBo ordersBo = convertToOrderBo(orders);

            // 设置子订单
            List<OrderDetailBo> orderDetailBoList = orderDetailBoMap.get(orders.getId());
            ordersBo.setOrderDetailBoList(orderDetailBoList);

            ordersBoList.add(ordersBo); //
        }



        // 4.获取组装结果的其他相关数据
        // 4.1 获取导出需要的promotion信息列表
        List<PromotionBean> promotionBeanList =
                equityRpcService.queryPromotionByIdList(new ArrayList<>(promotionIdSet));
        log.info("组装导出订单 根据id获取活动 List<PromotionBean>:{}", JSONUtil.toJsonString(promotionBeanList));
        // 转map key:promotionId, value:PromotionBean
        Map<Integer, PromotionBean> promotionBeanMap =
                promotionBeanList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        // 4.2 获取导出需要的coupon信息列表
        List<CouponUseInfoBean> couponUseInfoBeanList = equityRpcService.queryCouponUseInfoByIdList(new ArrayList<>(couponUseInfoIdSet));
        log.info("组装导出订单 根据id获取coupon List<CouponBean>:{}", JSONUtil.toJsonString(couponUseInfoBeanList));
        // 转map， key couponUseInfoId, value: CouponUseInfoBean
        Map<Integer, CouponUseInfoBean> couponUseInfoBeanMap =
                couponUseInfoBeanList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        // 4.3 获取导出需要的product信息列表
        List<ProductInfoBean> productInfoBeanList =
                productRpcService.findProductListByMpuIdList(new ArrayList<>(mpuIdList));
        log.info("组装导出订单 根据mpu获取product List<ProductInfoBean>:{}", JSONUtil.toJsonString(productInfoBeanList));
        // 转map， key:mpu  value:ProductInfoBean
        Map<String, ProductInfoBean> productInfoBeanMap =
                productInfoBeanList.stream().collect(Collectors.toMap(p -> p.getMpu(), p -> p));

        // 4.4 获取导出需要的供应商信息列表
        List<SysCompanyX> sysCompanyXList = vendorsRpcService.queryAllCompanyList();
        // 转map
        Map<Long, SysCompanyX> merchantMap = sysCompanyXList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));




        // x. ordersBoList 组装 List<ExportOrdersVo>
        List<ExportOrdersVo> exportOrdersVoList = new ArrayList<>();
        for (OrdersBo ordersBo : ordersBoList) { // 遍历主订单
            List<OrderDetailBo> orderDetailBoList = ordersBo.getOrderDetailBoList(); // 获取该订单的子订单
            if (CollectionUtils.isNotEmpty(orderDetailBoList)) { // 目前不存在子订单的这种情况是不存在的!!!!
                for (OrderDetailBo orderDetailBo : orderDetailBoList) { // 遍历子订单
                    ExportOrdersVo exportOrdersVo = new ExportOrdersVo();

                    exportOrdersVo.setMerchantId(0L);
                    exportOrdersVo.setMerchantName("未找到.");

                    if (orderDetailBo.getMerchantId() != null) {
                        exportOrdersVo.setMerchantId(Long.valueOf(orderDetailBo.getMerchantId()));
                        exportOrdersVo.setMerchantName(merchantMap.get(Long.valueOf(orderDetailBo.getMerchantId())) == null ?
                                "未找到" : merchantMap.get(Long.valueOf(orderDetailBo.getMerchantId())).getName());
                    }

                    exportOrdersVo.setOpenId(ordersBo.getOpenId()); // 用户id
                    exportOrdersVo.setTradeNo(ordersBo.getTradeNo()); // 主订单编号
                    exportOrdersVo.setPaymentNo(ordersBo.getPaymentNo());
                    exportOrdersVo.setSubOrderId(orderDetailBo.getSubOrderId()); // 子订单编号
                    exportOrdersVo.setOrderDetailStatus(orderDetailBo.getOrderDetailStatus()); // 子订单状态
                    exportOrdersVo.setPaymentTime(ordersBo.getPaymentAt()); // 订单支付时间
                    exportOrdersVo.setCreateTime(ordersBo.getCreatedAt()); // 订单生成时间
                    exportOrdersVo.setAoyiID(ordersBo.getAoyiId());

                    ProductInfoBean productInfoBean = productInfoBeanMap.get(orderDetailBo.getMpu());
                    exportOrdersVo.setCategory(productInfoBean == null ? "" : productInfoBean.getCategoryName()); // 品类
                    exportOrdersVo.setBrand(productInfoBean == null ? "" : productInfoBean.getBrand()); // 品牌
                    exportOrdersVo.setSku(orderDetailBo.getSkuId());
                    exportOrdersVo.setMpu(orderDetailBo.getMpu());
                    exportOrdersVo.setCommodityName(productInfoBean == null ? "" : productInfoBean.getName()); // 商品名称
                    exportOrdersVo.setQuantity(orderDetailBo.getNum()); // 购买数量
                    exportOrdersVo.setPromotion(
                            promotionBeanMap.get(orderDetailBo.getPromotionId()) == null ?
                                    "" : promotionBeanMap.get(orderDetailBo.getPromotionId()).getName()); // 活动
                    exportOrdersVo.setPromotionId(orderDetailBo.getPromotionId() == null ?
                            0 : orderDetailBo.getPromotionId().longValue()); // 活动id
                    // 结算类型 （0：普通类结算， 1：秒杀类结算， 2：精品类结算）
                    if (promotionBeanMap.get(orderDetailBo.getPromotionId()) != null) {
                        Integer settlement = promotionBeanMap.get(orderDetailBo.getPromotionId()) == null ?
                                null : promotionBeanMap.get(orderDetailBo.getPromotionId()).getAccountType();
                        if (settlement == null) {
                            exportOrdersVo.setSettlementType("未找到结算类型");
                        } else if (settlement == 0) {
                            exportOrdersVo.setSettlementType("普通类结算");
                        } else if (settlement == 1) {
                            exportOrdersVo.setSettlementType("秒杀类结算");
                        } else if (settlement == 2) {
                            exportOrdersVo.setSettlementType("精品类结算");
                        }
                    }else{
                        exportOrdersVo.setSettlementType("普通类结算");
                    }
                    exportOrdersVo.setCouponCode(ordersBo.getCouponCode()); // 券码
                    exportOrdersVo.setCouponId(ordersBo.getCouponId() == null ? null : ordersBo.getCouponId().longValue()); // 券码id
                    exportOrdersVo.setCouponSupplier(
                            couponUseInfoBeanMap.get(ordersBo.getCouponId()) == null ?
                                    "" : couponUseInfoBeanMap.get(ordersBo.getCouponId()).getSupplierMerchantName()); // 券来源（券商户）
//                    Integer purchasePrice = null; // 进货价格
//                    if (productInfoBean != null) {
//                        String _sprice = productInfoBean.getSprice();
//                        if (StringUtils.isNotBlank(_sprice)) {
//                            BigDecimal bigDecimal = new BigDecimal(_sprice);
//                            purchasePrice = bigDecimal.multiply(new BigDecimal(100)).intValue();
//                        }
//                    }

                    exportOrdersVo.setPurchasePrice(orderDetailBo.getSprice() == null ?
                            0 : orderDetailBo.getSprice().multiply(new BigDecimal(100)).intValue()); // 进货价格 单位分
                    exportOrdersVo.setSkuPayPrice(orderDetailBo.getSalePrice() == null ?
                            0 : orderDetailBo.getSalePrice().multiply(new BigDecimal(100)).intValue()); // sku 实际支付价格 分
                    exportOrdersVo.setUnitPrice(orderDetailBo.getUnitPrice().multiply(new BigDecimal(100)).intValue()); // 商品单价-去除 活动 的价格
                    exportOrdersVo.setCouponPrice(ordersBo.getCouponDiscount() == null ?
                            0 : new BigDecimal(ordersBo.getCouponDiscount().toString()).multiply(new BigDecimal(100)).intValue()); // 主订单 券支付金额
                    exportOrdersVo.setSkuCouponDiscount(orderDetailBo.getSkuCouponDiscount() == null ?
                            0 :orderDetailBo.getSkuCouponDiscount()); // 子订单 sku的优惠券支付金额 分

                    exportOrdersVo.setPayPrice(ordersBo.getSaleAmount() == null ?
                            0 : new BigDecimal(ordersBo.getSaleAmount().toString()).multiply(new BigDecimal(100)).intValue()); // // 主订单实际支付的价格 单位:分 // (exportOrdersVo.getTotalRealPrice() - exportOrdersVo.getCouponPrice()); // orderDetailBo.getSalePrice().multiply(new BigDecimal(100)).intValue()
                    // exportOrdersVo.setShareBenefitPercent(); // 平台分润比!!!

                    exportOrdersVo.setBuyerName(ordersBo.getReceiverName()); // 收件人名
                    exportOrdersVo.setProvinceName(ordersBo.getProvinceName()); // 省
                    exportOrdersVo.setCityName(ordersBo.getCityName()); // 市
                    exportOrdersVo.setCountyName(ordersBo.getCountyName()); // 区
                    exportOrdersVo.setExpressFee(ordersBo.getServFee().toString()); // 运费
                    exportOrdersVo.setAddress(ordersBo.getAddress() == null ? "" : ordersBo.getAddress()); // 详细地址
                    exportOrdersVo.setAoyiID(ordersBo.getAoyiId());
                    exportOrdersVo.setMobile(ordersBo.getMobile());
                    exportOrdersVo.setRemark(orderDetailBo.getRemark());
                    // 退款金额 单位元
                    if (orderDetailBo.getRefundAmount() != null) {
                        exportOrdersVo.setOrderDetailRefundAmount(orderDetailBo.getRefundAmount());
                    }

                    // 支付方式
                    if(paymentMethodInfoMap != null){
                        List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList = paymentMethodInfoMap.get(ordersBo.getPaymentNo());
                        exportOrdersVo.setBalanceFee("0"); // 余额支付金额 单位 元
                        exportOrdersVo.setHuiminCardFee("0"); // 惠民卡支付金额 单位 元
                        exportOrdersVo.setWoaFee("0"); // 联机账户支付 单位 元
                        exportOrdersVo.setQuickPayFee("0"); // 快捷支付 单位 元
                        if (CollectionUtils.isNotEmpty(orderPayMethodInfoBeanList)) {

                            boolean checkHuiminCardUnNormalPayStatus = true; // 检验是否存在有异常的支付状态

                            for (OrderPayMethodInfoBean orderPayMethodInfoBean : orderPayMethodInfoBeanList) {
                                String payType = orderPayMethodInfoBean.getPayType();
                                Integer payStatus = orderPayMethodInfoBean.getStatus();

                                if (payStatus == null || payStatus == 0) {
                                    continue;
                                }

                                // 处理显示的价格
                                String _fen = orderPayMethodInfoBean.getActPayFee(); // 花费
                                String _fee = StringUtils.isBlank(_fen) ?
                                        "0" : new BigDecimal(_fen).divide(new BigDecimal(100)).toPlainString(); // 转元

                                if (OrderPayMethodTypeEnum.BALANCE.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setBalanceFee(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setBalanceFee(exportOrdersVo.getBalanceFee() + "(异常)");
                                    }
                                } else if (OrderPayMethodTypeEnum.HUIMIN_CARD.getValue().equalsIgnoreCase(payType)) {
                                    String huiminFee = exportOrdersVo.getHuiminCardFee(); // 单位 元

                                    huiminFee = new BigDecimal(huiminFee).add(new BigDecimal(_fee)).toPlainString();

                                    exportOrdersVo.setHuiminCardFee(huiminFee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        checkHuiminCardUnNormalPayStatus = false;
                                    }
                                } else if (OrderPayMethodTypeEnum.WOA.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setWoaFee(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setWoaFee(exportOrdersVo.getWoaFee() + "(异常)");
                                    }
                                } else if (OrderPayMethodTypeEnum.BANK.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setQuickPayFee(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setQuickPayFee(exportOrdersVo.getQuickPayFee() + "(异常)");
                                    }
                                }
                            }

                            if (!checkHuiminCardUnNormalPayStatus) {
                                if (!"0".equals(exportOrdersVo.getHuiminCardFee())) {
                                    exportOrdersVo.setHuiminCardFee(exportOrdersVo.getHuiminCardFee() + "(异常)");
                                }
                            }

                        }
                    }

                    // 退款方式
                    if(refundMethodInfoMap != null){
                        List<RefundMethodInfoBean> refundMethodInfoList = refundMethodInfoMap.get(orderDetailBo.getSubOrderId());
                        exportOrdersVo.setBalanceRefund("0"); // 余额支付金额 单位 元
                        exportOrdersVo.setHuiminCardRefund("0"); // 惠民卡支付金额 单位 元
                        exportOrdersVo.setWoaRefund("0"); // 联机账户支付 单位 元
                        exportOrdersVo.setQuickPayRefund("0"); // 快捷支付 单位 元
                        if (CollectionUtils.isNotEmpty(refundMethodInfoList)) {

                            boolean checkHuiminCardUnNormalPayStatus = true; // 检验是否存在有异常的支付状态

                            for (RefundMethodInfoBean refundMethodInfoBean : refundMethodInfoList) {
                                String payType = refundMethodInfoBean.getPayType();
                                Integer payStatus = refundMethodInfoBean.getStatus();
                                exportOrdersVo.setRefundNo(refundMethodInfoBean.getOutRefundNo());
                                if (payStatus == null || payStatus == 0) {
                                    continue;
                                }

                                // 处理显示的价格
                                String _fen = refundMethodInfoBean.getRefundFee(); // 花费
                                String _fee = StringUtils.isBlank(_fen) ?
                                        "0" : new BigDecimal(_fen).divide(new BigDecimal(-100)).toPlainString(); // 转元

                                if (OrderPayMethodTypeEnum.BALANCE.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setBalanceRefund(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setBalanceRefund(exportOrdersVo.getBalanceFee() + "(超时)");
                                    }
                                } else if (OrderPayMethodTypeEnum.HUIMIN_CARD.getValue().equalsIgnoreCase(payType)) {
                                    String huiminRefund = exportOrdersVo.getHuiminCardRefund(); // 单位 元

                                    huiminRefund = new BigDecimal(huiminRefund).add(new BigDecimal(_fee)).toPlainString();

                                    exportOrdersVo.setHuiminCardRefund(huiminRefund);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        checkHuiminCardUnNormalPayStatus = false;
                                    }
                                } else if (OrderPayMethodTypeEnum.WOA.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setWoaRefund(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setWoaRefund(exportOrdersVo.getWoaRefund() + "(超时)");
                                    }
                                } else if (OrderPayMethodTypeEnum.BANK.getValue().equalsIgnoreCase(payType)) {
                                    exportOrdersVo.setQuickPayRefund(_fee);

                                    if (payStatus != 1) { // 注意，这里如果不是1， 表示支付状态不是‘成功’， 这里需要将该数据标识出来
                                        exportOrdersVo.setQuickPayRefund(exportOrdersVo.getQuickPayRefund() + "(超时)");
                                    }
                                }
                            }

                            if (!checkHuiminCardUnNormalPayStatus) {
                                if (!"0".equals(exportOrdersVo.getHuiminCardRefund())) {
                                    exportOrdersVo.setHuiminCardRefund(exportOrdersVo.getHuiminCardRefund() + "(超时)");
                                }
                            }

                        }
                    }
                    //////////
                    exportOrdersVoList.add(exportOrdersVo);
                } // 遍历子订单 end
            } // end if 子订单不为空



        } // 遍历主订单 end

        return exportOrdersVoList;
    }

    /**
     * @param orderExportReqVo
     * @return
     */
    private Orders convertToOrdersForExport(OrderExportReqVo orderExportReqVo) {
        Orders orders = new Orders();

        orders.setTradeNo(orderExportReqVo.getTradeNo());
        // orderExportReqVo.getSubOrderId();
        orders.setMobile(orderExportReqVo.getPhoneNo());
        orders.setStatus(orderExportReqVo.getOrderStatus());

        return orders;
    }

    /**
     * @param orderDetail
     * @return
     */
    private OrderDetailBo convertToOrderDetailBo(OrderDetail orderDetail) {
        OrderDetailBo orderDetailBo = new OrderDetailBo();

        orderDetailBo.setId(orderDetail.getId());
        orderDetailBo.setMerchantId(orderDetail.getMerchantId());
        orderDetailBo.setOrderId(orderDetail.getOrderId());
        orderDetailBo.setSubOrderId(orderDetail.getSubOrderId());
        orderDetailBo.setMpu(orderDetail.getMpu());
        orderDetailBo.setSkuId(orderDetail.getSkuId());
        orderDetailBo.setNum(orderDetail.getNum());
        orderDetailBo.setPromotionId(orderDetail.getPromotionId());
        orderDetailBo.setPromotionDiscount(orderDetail.getPromotionDiscount());
        orderDetailBo.setSalePrice(orderDetail.getSalePrice()); // 单位 元
        orderDetailBo.setUnitPrice(orderDetail.getUnitPrice());
        orderDetailBo.setImage(orderDetail.getImage());
        orderDetailBo.setModel(orderDetail.getModel());
        orderDetailBo.setName(orderDetail.getName());
        orderDetailBo.setStatus(orderDetail.getStatus());
        orderDetailBo.setCreatedAt(orderDetail.getCreatedAt());
        orderDetailBo.setUpdatedAt(orderDetail.getUpdatedAt());
        orderDetailBo.setLogisticsId(orderDetail.getLogisticsId());
        orderDetailBo.setLogisticsContent(orderDetail.getLogisticsContent());
        orderDetailBo.setComcode(orderDetail.getComcode());
        orderDetailBo.setSkuCouponDiscount(orderDetail.getSkuCouponDiscount());
        orderDetailBo.setRemark(orderDetail.getRemark());
        orderDetailBo.setSprice(orderDetail.getSprice());

        return orderDetailBo;
    }

    /**
     * @param orders
     * @return
     */
    private OrdersBo convertToOrderBo(Orders orders) {
        OrdersBo orderBo = new OrdersBo();

        orderBo.setId(orders.getId());
        orderBo.setOpenId(orders.getOpenId());
        orderBo.setTradeNo(orders.getTradeNo());
        orderBo.setAoyiId(orders.getAoyiId());
        orderBo.setMerchantId(orders.getMerchantId());
        orderBo.setMerchantNo(orders.getMerchantNo());
        orderBo.setCouponCode(orders.getCouponCode());
        orderBo.setCouponDiscount(orders.getCouponDiscount());
        orderBo.setCouponId(orders.getCouponId());
        orderBo.setCompanyCustNo(orders.getCompanyCustNo());
        orderBo.setReceiverName(orders.getReceiverName());
        orderBo.setTelephone(orders.getTelephone());
        orderBo.setMobile(orders.getMobile());
        orderBo.setEmail(orders.getEmail());
        orderBo.setProvinceName(orders.getProvinceName());
        orderBo.setProvinceId(orders.getProvinceId());
        orderBo.setCityName(orders.getCityName());
        orderBo.setCityId(orders.getCityId());
        orderBo.setCountyName(orders.getCountyName());
        orderBo.setCountyId(orders.getCountyId());
        orderBo.setTownId(orders.getTownId());
        orderBo.setAddress(orders.getAddress());
        orderBo.setZip(orders.getZip());
        orderBo.setRemark(orders.getRemark());
        orderBo.setInvoiceState(orders.getInvoiceState());
        orderBo.setInvoiceType(orders.getInvoiceType());
        orderBo.setInvoiceTitle(orders.getInvoiceTitle());
        orderBo.setInvoiceContent(orders.getInvoiceContent());
        orderBo.setPayment(orders.getPayment());
        orderBo.setServFee(orders.getServFee());
        orderBo.setSaleAmount(orders.getSaleAmount());
        orderBo.setAmount(orders.getAmount());
        orderBo.setStatus(orders.getStatus());
        orderBo.setType(orders.getType());
        orderBo.setOutTradeNo(orders.getOutTradeNo());
        orderBo.setPaymentNo(orders.getPaymentNo());
        orderBo.setPaymentAt(orders.getPaymentAt());
        orderBo.setPaymentAmount(orders.getPaymentAmount());
        orderBo.setPayee(orders.getPayee());
        orderBo.setPayType(orders.getPayType());
        orderBo.setPaymentTotalFee(orders.getPaymentTotalFee());
        orderBo.setPayer(orders.getPayer());
        orderBo.setPayStatus(orders.getPayStatus());
        orderBo.setPayOrderCategory(orders.getPayOrderCategory());
        orderBo.setRefundFee(orders.getRefundFee());
        orderBo.setCreatedAt(orders.getCreatedAt());
        orderBo.setUpdatedAt(orders.getUpdatedAt());

        return orderBo;
    }
}
