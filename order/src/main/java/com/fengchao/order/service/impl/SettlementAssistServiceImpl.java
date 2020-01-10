package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.bo.UserOrderBo;
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
import com.fengchao.order.utils.CalculateUtil;
import com.fengchao.order.utils.JSONUtil;
import com.google.common.collect.Lists;
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

        // x. 获取子订单集合相关的结算类型信息 key:promotionId, value: 结算类型
        Map<Integer, Integer> promotionMap = queryOrderDetailSettlementType(orderDetailList);
        log.info("结算辅助服务-查询进账的用户单 获取子订单集合相关的结算类型信息(入账):{}", JSONUtil.toJsonString(promotionMap));

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

            // 计算该主订单下所有子订单的salePrice*num之和
            Integer totalPriceForOrder = 0; // 单位分
            for (OrderDetailBo _orderDetailBo : _orderDetailBoList) {
                totalPriceForOrder = totalPriceForOrder +
                        CalculateUtil.convertYuanToFen(_orderDetailBo.getSalePrice().multiply(new BigDecimal(_orderDetailBo.getNum())).toString());
            }
            ordersBo.setTotalSalePrice(totalPriceForOrder);

            // 输出结果map key: paymentNo, value:List<OrdersBo>
            List<OrdersBo> _ordersBoList = ordersBoMap.get(orders.getPaymentNo());
            if (_ordersBoList == null) {
                _ordersBoList = new ArrayList<>();
                ordersBoMap.put(orders.getPaymentNo(), _ordersBoList);
            }

            _ordersBoList.add(ordersBo);
        }

        // 5. 根据paymentNo集合，查询支付信息
        // 获取payNo
        log.info("结算辅助服务-查询进账的用户单 需要查询用户单支付方式信息的个数是:{}", paymentNoList.size());
        // 分区
        List<List<String>> paymentNoListPartition = Lists.partition(paymentNoList, LIST_PARTITION_SIZE_50);

        // 根据payNo集合 查询支付信息
        Map<String, List<OrderPayMethodInfoBean>> paymentMethodMap = new HashMap<>(); //
        for (List<String> _paymentNoList : paymentNoListPartition) {
            // key : paymentNo
            Map<String, List<OrderPayMethodInfoBean>> _paymentMethodMap = wsPayRpcService.queryBatchPayMethod(_paymentNoList);

            paymentMethodMap.putAll(_paymentMethodMap);
        }

        log.info("结算辅助服务-查询进账的用户单 找到所有入账用户单支付方式信息是:{}", JSONUtil.toJsonString(paymentMethodMap));

        // 6. 组装用户单数据
        List<UserOrderBo> userOrderBoList = new ArrayList<>();
        for (String paymentNo : paymentMethodMap.keySet()) {
            UserOrderBo userOrderBo = new UserOrderBo();

            userOrderBo.setPaymentNo(paymentNo); // 支付单号
            userOrderBo.setUserOrderNo(paymentNo); // 用户单号
            userOrderBo.setMerchantOrderList(ordersBoMap.get(paymentNo)); // 商户单
            userOrderBo.setPayMethodInfoBeanList(paymentMethodMap.get(paymentNo)); // 支付方式

            // 计算主订单中的totalSalePrice之和
            Integer totalSalePrice = 0;
            for (OrdersBo _ordersBo : ordersBoMap.get(paymentNo)) {
                totalSalePrice = totalSalePrice + _ordersBo.getTotalSalePrice();
            }
            userOrderBo.setTotalSalePrice(totalSalePrice);

            userOrderBoList.add(userOrderBo);
        }

        log.info("结算辅助服务-查询进账的用户单 获取的入账用户单信息:{}", JSONUtil.toJsonStringWithoutNull(userOrderBoList));

        return userOrderBoList;
    }

    @Override
    public List<UserOrderBo> queryRefundUserOrderBoList(Date startTime, Date endTime, String appId) {
        try {
            // 1. 查询退款的信息
            List<WorkOrder> workOrderList = workOrderRpcService.queryRefundedOrderDetailList(null, startTime, endTime);
            if (CollectionUtils.isEmpty(workOrderList)) {
                log.info("结算辅助服务-查询退款工单 未获取到退款记录");

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

            log.info("结算辅助服务-查询出账的用户单 获取所有退款主订单个数:{}", ordersList.size());

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

            // x. 获取子订单集合相关的结算类型信息 key:promotionId, value: 结算类型
            Map<Integer, Integer> promotionMap = queryOrderDetailSettlementType(orderDetailList);
            log.info("结算辅助服务-查询出账的用户单 获取子订单集合相关的结算类型信息(出账):{}", JSONUtil.toJsonString(promotionMap));

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

                // 6.2 计算该主订单下所有子订单的salePrice*num之和
                Integer totalPriceForOrder = 0; // 单位分
                for (OrderDetailBo _orderDetailBo : _orderDetailBoList) {
                    totalPriceForOrder = totalPriceForOrder +
                            CalculateUtil.convertYuanToFen(_orderDetailBo.getSalePrice().multiply(new BigDecimal(_orderDetailBo.getNum())).toString());
                }
                ordersBo.setTotalSalePrice(totalPriceForOrder);

                // 6.3 转map key: paymentNo， value:List<OrdersBo>
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

                Integer totalSalePrice = 0; // 准备计算该用户单下所有子订单salePrice*num之和
                for (OrdersBo _ordersBo : _ordresBoList) { // 遍历商户单
                    totalSalePrice = totalSalePrice + _ordersBo.getTotalSalePrice(); // 该用户单下所有子订单salePrice*num之和

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
                userOrderBo.setTotalSalePrice(totalSalePrice); // 该用户单下所有子订单salePrice*num之和

                // 退款信息
                List<OrderPayMethodInfoBean> refundMethodInfoBeanList = assembleRefundInfo(cardRefundAmount,
                        balanceRefundAmount, bankRefundAmount, woaRefundAmount);
                userOrderBo.setRefundMethodInfoBeanList(refundMethodInfoBeanList);

                //
                userOrderBoList.add(userOrderBo);
            } // end 组装用户单

            log.info("结算辅助服务-查询出账的用户单 获取出账用户单信息:{}", JSONUtil.toJsonStringWithoutNull(userOrderBoList));
            return userOrderBoList;
        } catch (Exception e) {
            log.error("结算辅助服务-查询出账的用户单 处理退款记录异常:{}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<UserOrderBo> mergeIncomeAndRefundUserOrder(Date startTime, Date endTime, String appId) {
        // 1. 查询入账和出账的用户单
        // 获取入账的用户单
        List<UserOrderBo> incomeUserOrderBoList = queryIncomeUserOrderBoList(startTime, endTime, appId);
        // 获取出账的用户单
        List<UserOrderBo> refundUserOrderBoList = queryRefundUserOrderBoList(startTime, endTime, appId);

        // x. 将出账的用户单转map key: paymentNo, value:UserOrderBo
        Map<String, UserOrderBo> refundUserOrderBoMap = refundUserOrderBoList.stream().collect(Collectors.toMap(r -> r.getPaymentNo(), r -> r));

        // 2. 合并入账和出账的用户单，合并内容有: 金额信息、sku的数量
        List<UserOrderBo> mergedUserOrderBoList = new ArrayList<>();

        //
        for (UserOrderBo incomeUserOrderBo : incomeUserOrderBoList) { // 遍历入账用户单
            UserOrderBo mergedUserOrderBo = incomeUserOrderBo; // 合并后的

            // 2.1 获取该入账用户单关联的出账用户单
            UserOrderBo refundUserOrderBo = refundUserOrderBoMap.get(incomeUserOrderBo.getPaymentNo());
            // 设置该用户单的退款信息
            mergedUserOrderBo.setRefundMethodInfoBeanList(refundUserOrderBo == null ? null : refundUserOrderBo.getRefundMethodInfoBeanList());

            // 2.2 合并入账和退款子订单的个数
            if (refundUserOrderBo != null) { // 如果该入账用户单存在退款用户单
                mergeIncomeAndRefundOrderDetailNum(mergedUserOrderBo, refundUserOrderBo);
            }

            // 2.3 合并入账和出账用户单中的totalSalePrice属性
            if (refundUserOrderBo != null) {
                mergedUserOrderBo.setTotalSalePrice(mergedUserOrderBo.getTotalSalePrice() - refundUserOrderBo.getTotalSalePrice());
            }

            // 2.4 合并支付和退款信息, 注意: 这个合并逻辑并没有改变原有的数据，而是新增了合并后的属性(支付方式)MergedMethodInfoBeanList
            List<OrderPayMethodInfoBean> mergedPayMethodInfoList =
                    mergePayMethodInfoBean(mergedUserOrderBo.getPayMethodInfoBeanList(), mergedUserOrderBo.getRefundMethodInfoBeanList());
            // 设置支付(出账，入账)合并后的信息
            mergedUserOrderBo.setMergedMethodInfoBeanList(mergedPayMethodInfoList);

            // 移除退款的用户单
            if (refundUserOrderBo != null) {
                refundUserOrderBoMap.remove(incomeUserOrderBo.getPaymentNo());
            }

            //
            mergedUserOrderBoList.add(mergedUserOrderBo);
        }

        // 3. 如果还有退款用户单的信息, 那么将这些用户单加入mergedUserOrderBoList
        if (CollectionUtils.isNotEmpty(refundUserOrderBoList)) { // 如果还有出账的用户单
            for (UserOrderBo refundUserOrderBo : refundUserOrderBoMap.values()) {
                UserOrderBo mergedUserOrderBo = refundUserOrderBo; // 合并后的

                List<OrderPayMethodInfoBean> mergedPayMethodInfoList = mergePayMethodInfoBean(Collections.emptyList(), refundUserOrderBo.getRefundMethodInfoBeanList());
                mergedUserOrderBo.setMergedMethodInfoBeanList(mergedPayMethodInfoList);

                mergedUserOrderBoList.add(mergedUserOrderBo);
            }
        }

        log.info("结算辅助服务 合并出账和入账用户单信息为:{}", JSONUtil.toJsonStringWithoutNull(mergedUserOrderBoList));

        //
        return mergedUserOrderBoList;
    }

    @Override
    public List<UserOrderBo> assignPaymentAmout(List<UserOrderBo> userOrderBoList) {
        // 分配金额
        for (UserOrderBo userOrderBo : userOrderBoList) { // 遍历用户单
            // 1. 获取该用户单的基础数据
            // 该用户单下所有子订单的salePrice*num之和 单位分
            Integer totalSalePrice = userOrderBo.getTotalSalePrice();

            // 该用户单的支付信息
            List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList = userOrderBo.getMergedMethodInfoBeanList();
            // 转map key: paymentType value: 实际金额 单位分
            Map<String, Integer> payMethodMap = orderPayMethodInfoBeanList.stream().collect(Collectors.toMap(pm -> pm.getPayType(), pm -> Integer.valueOf(pm.getActPayFee())));

            // 2. 列出该用户单所有支付方式的金额(单位 分):
            Integer balanceAmount = payMethodMap.get(PaymentTypeEnum.BALANCE.getName()) == null ?
                    0 : payMethodMap.get(PaymentTypeEnum.BALANCE.getName()); // 余额
            Integer cardAmount = payMethodMap.get(PaymentTypeEnum.CARD.getName()) == null ?
                    0 : payMethodMap.get(PaymentTypeEnum.CARD.getName()); // 惠民卡
            Integer woaAmount = payMethodMap.get(PaymentTypeEnum.WOA.getName()) == null ?
                    0 : payMethodMap.get(PaymentTypeEnum.WOA.getName()); // 联机账户
            Integer bankAmount = payMethodMap.get(PaymentTypeEnum.BANK.getName()) == null ?
                    0 : payMethodMap.get(PaymentTypeEnum.BANK.getName()); // 快捷支付

            // 3. 开始分摊
            Integer remainBalanceAmount = balanceAmount;
            Integer remainCardAmount = cardAmount;
            Integer remainWoaAmount = woaAmount;
            Integer remainBankAmount = bankAmount;

            for (int i = 0; i < userOrderBo.getMerchantOrderList().size(); i++) { // 遍历(遍历该用户单下的所有子订单)
                OrdersBo ordersBo = userOrderBo.getMerchantOrderList().get(i);

                for (int j = 0; j < ordersBo.getOrderDetailBoList().size(); j++) {
                    OrderDetailBo orderDetailBo = ordersBo.getOrderDetailBoList().get(j);

                    if (i == (userOrderBo.getMerchantOrderList().size() - 1)
                            && j == (ordersBo.getOrderDetailBoList().size() - 1)) { // 如果是最后一条记录，那么使用减法
                        orderDetailBo.setShareBalanceAmount(remainBalanceAmount);
                        orderDetailBo.setShareCardAmount(remainCardAmount);
                        orderDetailBo.setShareWoaAmount(remainWoaAmount);
                        orderDetailBo.setShareBankAmount(remainBankAmount);
                    } else {
                        Integer multiplier = CalculateUtil.convertYuanToFen(orderDetailBo.getSalePrice().toString()) * orderDetailBo.getNum(); // 可能为0

                        // 余额分摊
                        Integer shareBalanceAmount =
                                new BigDecimal(balanceAmount).multiply(new BigDecimal(multiplier)).divide(new BigDecimal(totalSalePrice), 2, BigDecimal.ROUND_HALF_UP).intValue();
                        orderDetailBo.setShareBalanceAmount(shareBalanceAmount);
                        remainBalanceAmount = remainBalanceAmount - shareBalanceAmount;

                        // 惠民卡分摊
                        Integer shareCardAmount =
                                new BigDecimal(cardAmount).multiply(new BigDecimal(multiplier)).divide(new BigDecimal(totalSalePrice), 2, BigDecimal.ROUND_HALF_UP).intValue();
                        orderDetailBo.setShareCardAmount(shareCardAmount);
                        remainCardAmount = remainCardAmount - shareCardAmount;

                        // 联机账户分摊
                        Integer shareWoaAmount =
                                new BigDecimal(woaAmount).multiply(new BigDecimal(multiplier)).divide(new BigDecimal(totalSalePrice), 2, BigDecimal.ROUND_HALF_UP).intValue();
                        orderDetailBo.setShareWoaAmount(shareWoaAmount);
                        remainWoaAmount = remainWoaAmount - shareWoaAmount;

                        // 快捷支付分摊
                        Integer shareBankAmount =
                                new BigDecimal(bankAmount).multiply(new BigDecimal(multiplier)).divide(new BigDecimal(totalSalePrice), 2, BigDecimal.ROUND_HALF_UP).intValue();
                        orderDetailBo.setShareBankAmount(shareBankAmount);
                        remainBankAmount = remainBankAmount - shareBankAmount;
                    }
                }
            }
        }

        return userOrderBoList;
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

        // 转map key:promotionId, value: 结算类型（0：普通类结算， 1：秒杀类结算， 2：精品类结算）
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

    /**
     * 合并某个用户单的入账和出账的金额
     * <p>
     * 注意：这个合并逻辑并没有改变原有的数据，而是新增了合并后的属性(支付方式)MergedMethodInfoBeanList
     *
     * @param incomePayMethodList 某个用户单的入账信息 一定不为null
     * @param refundPayMehtodList 某个用户单的退款信息  可能为空(null || size:0)
     * @return
     */
    private List<OrderPayMethodInfoBean> mergePayMethodInfoBean(List<OrderPayMethodInfoBean> incomePayMethodList, List<OrderPayMethodInfoBean> refundPayMehtodList) {
        // 返回值
        List<OrderPayMethodInfoBean> mergedPayMethodInfoBeanList = new ArrayList<>();

        // 将出账信息转map key:payType(参考PaymentTypeEnum) value: OrderPayMethodInfoBean
        Map<String, OrderPayMethodInfoBean> refundPayMethodMap = CollectionUtils.isEmpty(refundPayMehtodList) ?
                Collections.emptyMap() : refundPayMehtodList.stream().collect(Collectors.toMap(r -> r.getPayType(), r -> r));

        // 合并
        for (OrderPayMethodInfoBean incomePayMehtod : incomePayMethodList) {
            OrderPayMethodInfoBean mergedOrderPayMethodInfoBean = copyOrderPayMethodInfoBean(incomePayMehtod); // incomePayMehtod;

            // 获取退款的方式信息
            OrderPayMethodInfoBean _refundPayMethodBean = refundPayMethodMap.get(incomePayMehtod.getPayType());

            // 如果存在相应的退款方式, 则从入账中减去退款
            if (_refundPayMethodBean != null) { //
                mergedOrderPayMethodInfoBean.setActPayFee(String.valueOf(Integer.valueOf(incomePayMehtod.getActPayFee()) - Integer.valueOf(_refundPayMethodBean.getActPayFee())));

                // 移除
                refundPayMethodMap.remove(incomePayMehtod.getPayType());
            }

            mergedPayMethodInfoBeanList.add(mergedOrderPayMethodInfoBean);
        }

        if (refundPayMethodMap.size() > 0) { // 如果还有退款方式， 则把这种方式放入到mergedPayMethodInfoBeanList
            for (String payType : refundPayMethodMap.keySet()) {
                OrderPayMethodInfoBean _refundPayMethodInfoBean = refundPayMethodMap.get(payType);

                OrderPayMethodInfoBean mergedOrderPayMethodInfoBean = copyOrderPayMethodInfoBean(_refundPayMethodInfoBean); // incomePayMehtod;
                mergedOrderPayMethodInfoBean.setActPayFee(String.valueOf(0 - Integer.valueOf(mergedOrderPayMethodInfoBean.getActPayFee())));

                //
                mergedPayMethodInfoBeanList.add(mergedOrderPayMethodInfoBean);
            }
        }

        return mergedPayMethodInfoBeanList;
    }

    /**
     * 合并入账和退款子订单的个数
     *
     * @param incomeUserOrderBo 入账用户单 一定不为空
     * @param refundUserOrderBo 与入账用户单关联的出账(退款)用户单 可能为空
     */
    private void mergeIncomeAndRefundOrderDetailNum(UserOrderBo incomeUserOrderBo, UserOrderBo refundUserOrderBo) {


        // 1. 将退款的用户单以mpu为维度转为mpa key: mpu, value: OrderDetailBo
        Map<String, OrderDetailBo> refundOrderDetailBoMap = new HashMap<>();

        // 获取该退款的用户单的商户单
        List<OrdersBo> refundMerchantOrdersBoList = refundUserOrderBo.getMerchantOrderList();
        for (OrdersBo refundOrdersBo : refundMerchantOrdersBoList) { // 遍历商户单
            List<OrderDetailBo> refundOrderDetailBoList = refundOrdersBo.getOrderDetailBoList();
            for (OrderDetailBo orderDetailBo : refundOrderDetailBoList) { // 遍历订单详情
                refundOrderDetailBoMap.put(orderDetailBo.getMpu(), orderDetailBo);
            }
        }

        // 2. 合并数量， 查看入账的子订单，是否存在在退款的子订单中，如果存在，则将数量设置为0
        for (OrdersBo incomeOrdersBo : incomeUserOrderBo.getMerchantOrderList()) { // 遍历
            for (OrderDetailBo incomeOrderDetailBo : incomeOrdersBo.getOrderDetailBoList()) {
                OrderDetailBo _orderDetailBo = refundOrderDetailBoMap.get(incomeOrderDetailBo.getMpu());
                if (_orderDetailBo != null) { // 如果有退款子订单，那么将该mpu的数量设置为0(因为退款是以mpu为维度退的)
                    incomeOrderDetailBo.setNum(0);
                }
            }
        }
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

    /**
     * @param old
     * @return
     */
    private OrderPayMethodInfoBean copyOrderPayMethodInfoBean(OrderPayMethodInfoBean old) {
        OrderPayMethodInfoBean newobj = new OrderPayMethodInfoBean(); // incomePayMehtod;

        newobj.setPayType(old.getPayType());
        newobj.setOrderNo(old.getOrderNo()); // 支付订单号
        newobj.setOutTradeNo(old.getOutTradeNo()); // 订单号
        newobj.setTradeNo(old.getTradeNo()); // 联机账户订单号
        newobj.setBody(old.getBody()); // 商品描述
        newobj.setRemark(old.getRemark()); // 用户自定义
        newobj.setTotalFee(old.getTotalFee()); // 交易总金额
        newobj.setActPayFee(old.getActPayFee()); // 交易实际金额 单位 分
        newobj.setStatus(old.getStatus()); // 交易状态: 1: 成功, 2: 失败, 0: 新创建
        newobj.setTradeDate(old.getTradeDate()); // 交易时间
        newobj.setCreateDate(old.getCreateDate()); // 创建时间
        newobj.setLimitPay(old.getLimitPay()); // 支付限制
        newobj.setCardNo(old.getCardNo()); // 卡号或OpenID
        newobj.setPayer(old.getPayer()); // 手机号
        newobj.setTradeType(old.getTradeType()); // tradeType
        newobj.setAppId(old.getAppId()); // appId

        return newobj;
    }
}
