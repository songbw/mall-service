package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.bo.UserOrderBo;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.WsPayRpcService;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.service.SettlementAssistService;
import com.fengchao.order.utils.AlarmUtil;
import com.fengchao.order.utils.JSONUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SettlementAssistServiceImpl implements SettlementAssistService {

    private static final Integer LIST_PARTITION_SIZE_50 = 50;

    private OrdersDao ordersDao;

    private OrderDetailDao orderDetailDao;

    private WsPayRpcService wsPayRpcService;

    @Autowired
    public SettlementAssistServiceImpl(OrdersDao ordersDao,
                                       OrderDetailDao orderDetailDao,
                                       WsPayRpcService wsPayRpcService) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
        this.wsPayRpcService = wsPayRpcService;
    }

    @Override
    public List<UserOrderBo> queryIncomeUserOrderBoList(Date startTime, Date endTime, String appId) {
        // 1. 首先查询符合条件的主订单
        List<Orders> payedOrdersList = ordersDao.selectPayedOrdersListByPaymentTime(startTime, endTime, appId); // 已经支付的主订单集合

        log.info("结算辅助服务-查询进账的用户单 查询已经支付的主订单集合个数:{}", payedOrdersList.size());

        if (CollectionUtils.isEmpty(payedOrdersList)) {
            log.warn("结算辅助服务-查询进账的用户单 未找到符合条件的主订单");
            return Collections.emptyList(); //
        }

        // x. 获取主订单集合中的支付订单号集合
        List<String> paymentNoList = payedOrdersList.stream().map(o -> o.getPaymentNo()).collect(Collectors.toList());
        // 获取主订单id集合
        List<Integer> orderIdList = payedOrdersList.stream().map(o -> o.getId()).collect(Collectors.toList());

        // 2. 查询子订单
        // 根据主订单id集合 查询子订单信息
        List<OrderDetail> orderDetailList = new ArrayList<>(); //

        // 分区
        List<List<Integer>> orderIdListPartition = Lists.partition(orderIdList, LIST_PARTITION_SIZE_50);
        for (List<Integer> _orderIdList : orderIdListPartition) {
            List<OrderDetail> _orderDetailList = orderDetailDao.selectOrderDetailsByOrdersIdList(_orderIdList);
            orderDetailList.addAll(_orderDetailList);
        }

        log.info("结算辅助服务-查询进账的用户单 找到子订单个数是:{}", orderDetailList.size());
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.warn("结算辅助服务-查询进账的用户单 未找到符合条件的主订单");

            return Collections.emptyList(); //
        }

        // x. 子订单转map, key: orderId, value:List<OrderDetail>
        Map<Integer, List<OrderDetail>> orderDetailMap = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            List<OrderDetail> _list = orderDetailMap.get(orderDetail.getOrderId());
            if (_list == null) {
                _list = new ArrayList<>();

                orderDetailMap.put(orderDetail.getOrderId(), _list);
            }

            _list.add(orderDetail);
        }

        // 3. 组装主订单和子订单
        for (Orders orders : payedOrdersList) {
            // Orders转bo
            OrdersBo ordersBo = convertToOrdersBo(orders);

            List<OrderDetail> _list = orderDetailMap.get(orders.getId());

            // orders.set
        }

        // 3. 根据paymentNo集合，查询支付信息
        // 获取payNo
        log.info("结算辅助服务-查询进账的用户单 需要查询用户单(支付单)的个数是:{}", paymentNoList.size());
        // 分区
        List<List<String>> paymentNoListPartition = Lists.partition(paymentNoList, LIST_PARTITION_SIZE_50);

        // 根据payNo集合 查询支付信息
        Map<String, List<OrderPayMethodInfoBean>> paymentMethodMap = new HashMap<>(); //
        for (List<String> _paymentNoList : paymentNoListPartition) {
            // key : paymentNo
            Map<String, List<OrderPayMethodInfoBean>> _paymentMethodMap = wsPayRpcService.queryBatchPayMethod(_paymentNoList);

            paymentMethodMap.putAll(_paymentMethodMap);
        }

        log.info("结算辅助服务-查询进账的用户单 找到用户单(支付单)信息是:{}", JSONUtil.toJsonString(paymentMethodMap));


        // 4. 组装用户单数据
        List<UserOrderBo> userOrderBoList = new ArrayList<>();



        return null;
    }

    //====================================== private ======================================

    private OrdersBo convertToOrdersBo(Orders orders) {
        OrdersBo ordersBo = new OrdersBo();

        ordersBo.setId(orders.getId());
        ordersBo.setOpenId(orders.getOpenId());
        ordersBo.setAppId(orders.getAppId());
        ordersBo.setTradeNo(orders.getTradeNo());
        // private String aoyiId;
        // private Integer merchantId;
        // private String merchantNo;
        // private String couponCode;
        // private Float couponDiscount;
        // private Integer couponId;
        // private String companyCustNo;
        // private String receiverName;
        // private String telephone;
        // private String mobile;
        // private String email;
        // private String remark;
        // private String payment;
        // private Float servFee;
        ordersBo.setSaleAmount(orders.getSaleAmount());
        ordersBo.setAmount(orders.getAmount());
        ordersBo.setStatus(orders.getStatus());
        // private Integer type;
        ordersBo.setOutTradeNo(orders.getOutTradeNo());
        ordersBo.setPaymentNo(orders.getPaymentNo());
        ordersBo.setPaymentAt(orders.getPaymentAt());
        ordersBo.setPaymentAmount(orders.getPaymentAmount()); // 单位 分
        // private String payee;
        // private String payType;
        ordersBo.setPaymentTotalFee(orders.getPaymentTotalFee()); // 单位 分
        // private String payer;
        ordersBo.setPayStatus(orders.getPayStatus());
        // private Integer payOrderCategory;
        ordersBo.setRefundFee(orders.getRefundFee());
        ordersBo.setCreatedAt(orders.getCreatedAt());
        ordersBo.setUpdatedAt(orders.getUpdatedAt());

        return ordersBo;
    }
}
