package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.bo.UserOrderBo;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.EquityRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.WsPayRpcService;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import com.fengchao.order.rpc.extmodel.RefundDetailBean;
import com.fengchao.order.rpc.extmodel.WorkOrder;
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

    private static final Integer LIST_PARTITION_SIZE_200 = 200;

    private OrdersDao ordersDao;

    private OrderDetailDao orderDetailDao;

    private WsPayRpcService wsPayRpcService;

    private WorkOrderRpcService workOrderRpcService;

    private EquityRpcService equityRpcService;

    @Autowired
    public SettlementAssistServiceImpl(OrdersDao ordersDao,
                                       OrderDetailDao orderDetailDao,
                                       WsPayRpcService wsPayRpcService,
                                       WorkOrderRpcService workOrderRpcService,
                                       EquityRpcService equityRpcService) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
        this.wsPayRpcService = wsPayRpcService;
        this.workOrderRpcService = workOrderRpcService;
        this.equityRpcService = equityRpcService;
    }

    @Override
    public List<UserOrderBo> queryIncomeUserOrderBoList(Date startTime, Date endTime, String appId) {
        // 1. 首先查询符合条件的主订单(进账)
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
        List<List<Integer>> orderIdListPartition = Lists.partition(orderIdList, LIST_PARTITION_SIZE_200);
        for (List<Integer> _orderIdList : orderIdListPartition) {
            List<OrderDetail> _orderDetailList = orderDetailDao.selectOrderDetailsByOrdersIdList(_orderIdList);
            orderDetailList.addAll(_orderDetailList);
        }

        log.info("结算辅助服务-查询进账的用户单 找到子订单个数是:{}", orderDetailList.size());
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.warn("结算辅助服务-查询进账的用户单 未找到符合条件的子订单");

            return Collections.emptyList(); //
        }

        // x. 获取子订单集合相关的结算类型信息
        Map<Integer, Integer> promotionMap = queryOrderDetailSettlementType(orderDetailList);

        // x. 子订单转map, key: orderId, value:List<OrderDetailBo>
        Map<Integer, List<OrderDetailBo>> orderDetailBoMap = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            List<OrderDetailBo> _list = orderDetailBoMap.get(orderDetail.getOrderId());
            if (_list == null) {
                _list = new ArrayList<>();

                orderDetailBoMap.put(orderDetail.getOrderId(), _list);
            }

            //
            OrderDetailBo orderDetailBo = convertToOrderDetailBo(orderDetail);
            orderDetailBo.setSettlementType(promotionMap.get(orderDetailBo.getPromotionId()) == null ?
                    0 : promotionMap.get(orderDetailBo.getPromotionId()));
            _list.add(orderDetailBo);
        }

        // 4. 组装主订单和子订单 - 结果以map输出, key: paymentNo, value:List<OrdersBo>
        Map<String, List<OrdersBo>> ordersBoMap = new HashMap<>();
        for (Orders orders : payedOrdersList) { // 遍历主订单
            // Orders转bo
            OrdersBo ordersBo = convertToOrdersBo(orders);

            // 设置子订单
            List<OrderDetailBo> _orderDetailBoList = orderDetailBoMap.get(orders.getId());
            ordersBo.setOrderDetailBoList(_orderDetailBoList);

            List<OrdersBo> _ordersBoList = ordersBoMap.get(orders.getPaymentNo());
            if (_ordersBoList == null) {
                _ordersBoList = new ArrayList<>();
                ordersBoMap.put(orders.getPaymentNo(), _ordersBoList);
            }

            _ordersBoList.add(ordersBo);
        }

        // 5. 根据paymentNo集合，查询支付信息
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

        // 6. 组装用户单数据
        List<UserOrderBo> userOrderBoList = new ArrayList<>();
        for (String paymentNo : paymentMethodMap.keySet()) {
            UserOrderBo userOrderBo = new UserOrderBo();

            userOrderBo.setPaymentNo(paymentNo); // 支付单号
            userOrderBo.setUserOrderNo(paymentNo); // 用户单号
            userOrderBo.setMerchantOrderList(ordersBoMap.get(paymentNo)); // 商户单
            userOrderBo.setOrderPayMethodInfoBeanList(paymentMethodMap.get(paymentNo)); // 支付方式

            userOrderBoList.add(userOrderBo);
        }

        log.info("结算辅助服务-查询进账的用户单 获取的用户单信息:{}", JSONUtil.toJsonString(userOrderBoList));

        return userOrderBoList;
    }

    @Override
    public List<UserOrderBo> queryRefundUserOrderBoList(Date startTime, Date endTime, String appId) {
        try {
            // 1. 查询退款的信息
            List<WorkOrder> workOrderList = workOrderRpcService.queryRefundedOrderDetailList(null, startTime, endTime);
            if (CollectionUtils.isEmpty(workOrderList)) {
                log.info("结算辅助服务-查询出账的用户单 未获取到退款记录");

                return Collections.emptyList();
            }

            // 2. 获取退款信息中的子订单信息
            // 子订单号集合
            List<String> orderDetailNoList = workOrderList.stream().map(w -> w.getOrderId()).collect(Collectors.toList());
            List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailListBySubOrderIds(orderDetailNoList);

            log.info("结算辅助服务-查询出账的用户单 获取所有退款子订单个数:{}", orderDetailList.size());

            // 3. 根据子订单查询主订单
            List<Integer> orderIdList = orderDetailList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
            List<Orders> ordersList = ordersDao.selectOrdersListByIdList(orderIdList, appId);

            // 4. 解析退款信息 输出map结果 key:子订单号, value:RefundDetailBean
            Map<String, List<RefundDetailBean>> refundDetailBeanMap = new HashMap<>(); //
            for (WorkOrder workOrder : workOrderList) { // 遍历退款信息
                // 退款详情
                // [
                //  {
                //    "createDate": "2019-12-20T16:11:47",
                //    "merchantCode": "32",
                //    "orderNo": "e2f2cf41ca674b51aeaeaac8a584fa79",
                //    "outRefundNo": "111576829506646",
                //    "payType": "balance",
                //    "refundFee": "10",
                //    "refundNo": "",
                //    "sourceOutTradeNo": "118111a3bf2248b9e6404c87dff0892572ebb953283501",
                //    "status": 1,
                //    "statusMsg": "退款成功",
                //    "totalFee": "0",
                //    "tradeDate": "20191220161146"
                //  }
                //]
                // 获取退款详情
                String refundInfoJson = workOrder.getComments();
                if (StringUtils.isBlank(refundInfoJson)) {
                    log.warn("结算辅助服务-查询出账的用户单 退款信息内容为空tradeNo:{}", workOrder.getTradeNo());
                    AlarmUtil.alarmAsync("结算辅助服务-查询出账的用户单", "退款信息内容为空tradeNo:" + workOrder.getTradeNo());
                    continue;
                }

                List<RefundDetailBean> refundDetailBeanList = JSONObject.parseArray(refundInfoJson, RefundDetailBean.class);
                if (CollectionUtils.isEmpty(refundDetailBeanList)) {
                    log.warn("结算辅助服务-查询出账的用户单 退款信息为空tradeNo:{}", workOrder.getTradeNo());
                    AlarmUtil.alarmAsync("结算辅助服务-查询出账的用户单", "退款信息为空tradeNo:" + workOrder.getTradeNo());
                    continue;
                }

                refundDetailBeanMap.put(workOrder.getOrderId(), refundDetailBeanList);
            }

            // x. 获取子订单集合相关的结算类型信息
            Map<Integer, Integer> promotionMap = queryOrderDetailSettlementType(orderDetailList);

            // 5. 将子订单设置退款详情  并输出map结果 key: 主订单id， value:List<OrderDetail>
            Map<Integer, List<OrderDetailBo>> orderDetailBoMap = new HashMap<>();
            for (OrderDetail _orderDetail : orderDetailList) {
                OrderDetailBo orderDetailBo = convertToOrderDetailBo(_orderDetail);
                orderDetailBo.setSettlementType(promotionMap.get(orderDetailBo.getPromotionId()) == null ?
                        0 : promotionMap.get(orderDetailBo.getPromotionId()));

                // 5.1 设置退款信息
                // 获取退款信息
                List<RefundDetailBean> _refundDetailBeanList = refundDetailBeanMap.get(_orderDetail.getSubOrderId());
                // 设置退款信息
                orderDetailBo.setRefundDetailBeanList(_refundDetailBeanList); //

                // 5.2 转map
                List<OrderDetailBo> _orderDetailBoList = orderDetailBoMap.get(_orderDetail.getOrderId());
                if (_orderDetailBoList == null) {
                    _orderDetailBoList = new ArrayList<>();

                    orderDetailBoMap.put(_orderDetail.getOrderId(), _orderDetailBoList);
                }

                _orderDetailBoList.add(orderDetailBo);
            }

            // 6. 将主订单设置子订单信息 并输出map结果 key: paymentNo， value:List<OrdersBo>
            Map<String, List<OrdersBo>> ordersBoMap = new HashMap<>();
            for (Orders orders : ordersList) {
                OrdersBo ordersBo = convertToOrdersBo(orders);

                // 6.1 设置子订单信息
                List<OrderDetailBo> _orderDetailBoList = orderDetailBoMap.get(ordersBo.getId());
                ordersBo.setOrderDetailBoList(_orderDetailBoList);

                // 6.2 转map
                List<OrdersBo> _ordersBoList = ordersBoMap.get(orders.getPaymentNo());
                if (_ordersBoList == null) {
                    _ordersBoList = new ArrayList<>();

                    ordersBoMap.put(orders.getPaymentNo(), _ordersBoList);

                    _ordersBoList.add(ordersBo);
                }
            }

            // 7. 组装用户单
            List<UserOrderBo> userOrderBoList = new ArrayList<>();
            for (String paymentNo : ordersBoMap.keySet()) {
                // 7.1 处理用户单退款总额信息
                int cardRefundAmount = 0;
                int balanceRefundAmount = 0;
                int bankRefundAmount = 0;
                int woaRefundAmount = 0;

                // 商户单信息
                List<OrdersBo> _ordresBoList = ordersBoMap.get(paymentNo);
                for (OrdersBo _ordersBo : _ordresBoList) { // 遍历商户单
                    List<OrderDetailBo> _orderDetailBoList = _ordersBo.getOrderDetailBoList(); // 子订单信息
                    for (OrderDetailBo _orderDetailBo : _orderDetailBoList) { // 遍历子订单
                        List<RefundDetailBean> refundDetailBeanList = _orderDetailBo.getRefundDetailBeanList(); // 该子订单的退款详情
                        for (RefundDetailBean refundDetailBean : refundDetailBeanList) { // 遍历该子订单的退款方式
                            Integer refundFee = Integer.valueOf(refundDetailBean.getRefundFee());

                            if (PaymentTypeEnum.CARD.getName().equals(refundDetailBean.getPayType())) {
                                cardRefundAmount = cardRefundAmount + refundFee;
                            } else if (PaymentTypeEnum.BALANCE.getName().equals(refundDetailBean.getPayType())) {
                                balanceRefundAmount = balanceRefundAmount + refundFee;
                            } else if (PaymentTypeEnum.BANK.getName().equals(refundDetailBean.getPayType())) {
                                bankRefundAmount = bankRefundAmount + refundFee;
                            } else if (PaymentTypeEnum.WOA.getName().equals(refundDetailBean.getPayType())) {
                                woaRefundAmount = woaRefundAmount + refundFee;
                            } else {
                                log.warn("结算辅助服务-查询出账的用户单 未找到相应的退款类型 outRefundNo:{}", refundDetailBean.getOutRefundNo());
                            }
                        }
                    }
                }

                // 7.2 组装用户单
                UserOrderBo userOrderBo = new UserOrderBo();

                userOrderBo.setPaymentNo(paymentNo);
                userOrderBo.setUserOrderNo(paymentNo);
                userOrderBo.setMerchantOrderList(ordersBoMap.get(paymentNo));

                // 退款信息
                List<OrderPayMethodInfoBean> refundMethodInfoBeanList = assembleRefundInfo(cardRefundAmount,
                        balanceRefundAmount, bankRefundAmount, woaRefundAmount);
                userOrderBo.setRefundMethodInfoBeanList(refundMethodInfoBeanList);

                //
                userOrderBoList.add(userOrderBo);
            } // end 组装用户单

            log.info("结算辅助服务-查询出账的用户单 获取用户单信息:{}", JSONUtil.toJsonString(userOrderBoList));
            return userOrderBoList;
        } catch (Exception e) {
            log.error("结算辅助服务-查询出账的用户单 处理退款记录异常:{}", e.getMessage(), e);
            throw e;
        }
    }


    //====================================== private ======================================

    /**
     * 获取子订单集合的结算类型信息
     *
     * @param orderDetailList
     */
    private Map<Integer, Integer> queryOrderDetailSettlementType(List<OrderDetail> orderDetailList) {
        List<Integer> promotionIdList = orderDetailList.stream().map(od -> od.getPromotionId()).collect(Collectors.toList());

        // 分区
        List<List<Integer>> promotionIdListPartition = Lists.partition(promotionIdList, LIST_PARTITION_SIZE_50);

        //
        List<PromotionBean> promotionBeanList = new ArrayList<>();
        for (List<Integer> _itemIdList : promotionIdListPartition) {
            List<PromotionBean> _list = equityRpcService.queryPromotionByIdList(_itemIdList);
            promotionBeanList.addAll(_list);
        }

        // 转map key:promotionId, value: 结算类型
        Map<Integer, Integer> promotionMap = promotionBeanList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p.getAccountType()));

        return promotionMap;
    }

    /**
     * 组装退款信息
     *
     * @param cardRefundAmount
     * @param balanceRefundAmount
     * @param bankRefundAmount
     * @param woaRefundAmount
     * @return
     */
    private List<OrderPayMethodInfoBean> assembleRefundInfo(int cardRefundAmount,
                                                            int balanceRefundAmount,
                                                            int bankRefundAmount,
                                                            int woaRefundAmount) {
        List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList = new ArrayList<>();

        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            OrderPayMethodInfoBean orderPayMethodInfoBean = new OrderPayMethodInfoBean();
            if ("card".equals(paymentTypeEnum.getName())) {
                orderPayMethodInfoBean.setPayType("card");
                orderPayMethodInfoBean.setActPayFee(String.valueOf(cardRefundAmount));
                orderPayMethodInfoBeanList.add(orderPayMethodInfoBean);
            } else if ("balance".equals(paymentTypeEnum.getName())) {
                orderPayMethodInfoBean.setPayType("balance");
                orderPayMethodInfoBean.setActPayFee(String.valueOf(balanceRefundAmount));
                orderPayMethodInfoBeanList.add(orderPayMethodInfoBean);
            } else if ("bank".equals(paymentTypeEnum.getName())) {
                orderPayMethodInfoBean.setPayType("bank");
                orderPayMethodInfoBean.setActPayFee(String.valueOf(bankRefundAmount));
                orderPayMethodInfoBeanList.add(orderPayMethodInfoBean);
            } else if ("woa".equals(paymentTypeEnum.getName())) {
                orderPayMethodInfoBean.setPayType("woa");
                orderPayMethodInfoBean.setActPayFee(String.valueOf(woaRefundAmount));
                orderPayMethodInfoBeanList.add(orderPayMethodInfoBean);
            }
        }

        return orderPayMethodInfoBeanList;
    }

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
        orderDetailBo.setSalePrice(orderDetail.getSalePrice());
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
        // orderDetailBo.setRefundAmount(orderDetail.ge); // 退款金额 单位元
        orderDetailBo.setRemark(orderDetail.getRemark());
        orderDetailBo.setSprice(orderDetail.getSprice());

        return orderDetailBo;
    }
}
