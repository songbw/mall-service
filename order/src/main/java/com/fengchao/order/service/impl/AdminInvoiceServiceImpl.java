package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.constants.ReceiptTypeEnum;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.WsPayRpcService;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.rpc.extmodel.ProductInfoBean;
import com.fengchao.order.rpc.extmodel.WorkOrder;
import com.fengchao.order.service.AdminInvoiceService;
import com.fengchao.order.utils.AlarmUtil;
import com.fengchao.order.utils.CalculateUtil;
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

@Service
@Slf4j
public class AdminInvoiceServiceImpl implements AdminInvoiceService {

    private static final Integer LIST_PARTITION_SIZE_200 = 200;
    private static final Integer LIST_PARTITION_SIZE_50 = 50;

    private OrdersDao ordersDao;

    private OrderDetailDao orderDetailDao;

    private WsPayRpcService wsPayRpcService;

    private ProductRpcService productRpcService;

    private WorkOrderRpcService workOrderRpcService;

    @Autowired
    public AdminInvoiceServiceImpl(OrdersDao ordersDao,
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

    /**
     * 导出商品开票信息
     *
     * @return
     */
    @Override
    public List<ExportReceiptBillVo> exportInvoice(Date startTime, Date endTime,
                                                       String appId, ReceiptTypeEnum receiptTypeEnum) throws Exception {
        try {
            // 1.获取入账的'发票导出'信息 key: mpu
            Map<String, ExportReceiptBillVo> inComeExportReceiptBillVoMap = handleIncomeInvoiceInfo(startTime, endTime, appId, receiptTypeEnum);
            log.info("导出商品开票信息 获取入账的信息个数:{}", inComeExportReceiptBillVoMap.size());

            // 2.获取出账的'发票导出'信息
            Map<String, ExportReceiptBillVo> outExportReceiptBillVoMap = handleOutInvoiceInfo(startTime, endTime, appId, receiptTypeEnum);
            log.info("导出商品开票信息 获取出账的信息个数:{}", outExportReceiptBillVoMap.size());

            // 3. 合并上2步的信息
            Map<String, ExportReceiptBillVo> exportReceiptBillVoMap = new HashMap<>(); // 合并后的结果
            for (String mpu : inComeExportReceiptBillVoMap.keySet()) { // 遍历入账的数据
                ExportReceiptBillVo exportReceiptBillVo = new ExportReceiptBillVo(); //

                ExportReceiptBillVo incomeExportReceiptBillVo = inComeExportReceiptBillVoMap.get(mpu);
                ExportReceiptBillVo outExportReceiptBillVo = outExportReceiptBillVoMap.get(mpu);

                exportReceiptBillVo.setMpu(mpu);
                if (outExportReceiptBillVo != null) { // 如果存在退款信息
                    exportReceiptBillVo.setTotalPrice(incomeExportReceiptBillVo.getTotalPrice() - outExportReceiptBillVo.getTotalPrice());
                    exportReceiptBillVo.setCount(incomeExportReceiptBillVo.getCount() - incomeExportReceiptBillVo.getCount());

                    outExportReceiptBillVoMap.remove(mpu); //
                } else {
                    exportReceiptBillVo.setTotalPrice(incomeExportReceiptBillVo.getTotalPrice());
                    exportReceiptBillVo.setCount(incomeExportReceiptBillVo.getCount());
                }


                //
                exportReceiptBillVoMap.put(mpu, exportReceiptBillVo); //
            }

            if (outExportReceiptBillVoMap.size() > 0) { // 如果还存在退款信息
                for (String mpu : outExportReceiptBillVoMap.keySet()) {
                    ExportReceiptBillVo exportReceiptBillVo = new ExportReceiptBillVo();

                    ExportReceiptBillVo _erbiv = outExportReceiptBillVoMap.get(mpu);
                    exportReceiptBillVo.setTotalPrice(0 - _erbiv.getTotalPrice());
                    exportReceiptBillVo.setCount(0 - _erbiv.getCount());

                    exportReceiptBillVo.setMpu(mpu);
                    exportReceiptBillVoMap.put(mpu, exportReceiptBillVo);
                }
            }

            log.info("导出商品开票信息 合并出账和入账的信息 结果是:{}", JSONUtil.toJsonString(exportReceiptBillVoMap));

            /////
            // x. 根据mpu查询一下商品信息
            List<ProductInfoBean> productInfoBeanList = new ArrayList<>();
            List<String> mpuList = new ArrayList<>(exportReceiptBillVoMap.keySet());
            // 分区
            List<List<String>> mpuPartition = Lists.partition(mpuList, LIST_PARTITION_SIZE_50);
            for (List<String> _mpuList : mpuPartition) {
                log.info("导出商品开票信息 查询商品信息rpc的入参:{}", JSONUtil.toJsonString(_mpuList));
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

                    // !!含税单价 单位分
                    int unitPrice = 0;
                    if (_exportReceiptBillVo.getCount() == 0) {
                        log.warn("导出商品开票信息 出账和入账后的商品个数为0, mpu:{}, 抵消后的金额是:{}",
                                _exportReceiptBillVo.getMpu(), _exportReceiptBillVo.getTotalPrice());
//                        AlarmUtil.alarmAsync("导出商品开票信息",
//                                "出账和入账后的商品个数为0, mpu:" + _exportReceiptBillVo.getMpu() + " 抵消后的金额是:" + _exportReceiptBillVo.getTotalPrice());
                    } else if (_exportReceiptBillVo.getCount() > 0) {
                        unitPrice = new BigDecimal(_exportReceiptBillVo.getTotalPrice())
                                .divide(new BigDecimal(_exportReceiptBillVo.getCount()), 2, BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                    } else if (_exportReceiptBillVo.getCount() < 0) {
                        log.warn("导出商品开票信息 出账和入账后的商品个数为:{}, mpu:{}, 抵消后的金额是:{}",
                                _exportReceiptBillVo.getCount(), _exportReceiptBillVo.getMpu(), _exportReceiptBillVo.getTotalPrice());

                        unitPrice = new BigDecimal(_exportReceiptBillVo.getTotalPrice())
                                .divide(new BigDecimal(0 - _exportReceiptBillVo.getCount()), 2, BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                    }
                    _exportReceiptBillVo.setUnitPrice(unitPrice); // 含税单价 单位分

                    // 税率
                    String taxRate = StringUtils.isBlank(productInfoBean.getTaxRate()) ? null : productInfoBean.getTaxRate();
                    _exportReceiptBillVo.setTaxRate(taxRate);

                    // !!税额 单位分
                    if (StringUtils.isNotBlank(taxRate)) {
                        int taxPrice = new BigDecimal(_exportReceiptBillVo.getTotalPrice())
                                .divide(new BigDecimal(taxRate).add(new BigDecimal(1)),2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(taxRate))
                                .setScale(0, BigDecimal.ROUND_HALF_UP)
                                .intValue(); // 单位分
                        _exportReceiptBillVo.setTaxPrice(taxPrice);
                    }
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

    //==================================== private ======================================

    /**
     * 处理出账的发票信息
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @param receiptTypeEnum
     * @return
     */
    private Map<String, ExportReceiptBillVo> handleOutInvoiceInfo(Date startTime, Date endTime, String appId, ReceiptTypeEnum receiptTypeEnum) {
        try {
            // 1. 查询退款的信息
            List<WorkOrder> workOrderList = workOrderRpcService.queryRefundedOrderDetailList(null, startTime, endTime);
            if (CollectionUtils.isEmpty(workOrderList)) {
                log.info("导出商品开票信息 未获取到退款记录");

                return Collections.emptyMap();
            }

            // 2. 获取退款信息中的子订单信息
            // 子订单号集合
            List<String> orderDetailNoList = workOrderList.stream().map(w -> w.getOrderId()).collect(Collectors.toList());
            List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailListBySubOrderIds(orderDetailNoList);

            log.info("导出商品开票信息 获取所有退款子订单个数:{}", orderDetailList.size());

            // x. 转map key: subOrderId, value: OrderDetail
            Map<String, OrderDetail> orderDetailMap =
                    orderDetailList.stream().collect(Collectors.toMap(od -> od.getSubOrderId(), od -> od));

            // 3. 将退款信息List<WorkOrder> 以mpu的维度聚合
            Map<String, ExportReceiptBillVo> exportReceiptBillVoMap = new HashMap<>(); //
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
                String refundInfoJson = workOrder.getComments();
                if (StringUtils.isBlank(refundInfoJson)) {
                    log.warn("导出商品开票信息 退款信息内容为空tradeNo:{}", workOrder.getTradeNo());
                    AlarmUtil.alarmAsync("导出商品开票信息", "退款信息内容为空tradeNo:" + workOrder.getTradeNo());
                    continue;
                }
                List<Map<String, Object>> refundInfoMapList = (List) JSONObject.parse(refundInfoJson);
                if (CollectionUtils.isEmpty(refundInfoMapList)) {
                    log.warn("导出商品开票信息 退款信息为空tradeNo:{}", workOrder.getTradeNo());
                    AlarmUtil.alarmAsync("导出商品开票信息", "退款信息为空tradeNo:" + workOrder.getTradeNo());
                    continue;
                }

                // 获取指定退款方式下的退款金额
                Integer refundAmount =calcRefundAmount(receiptTypeEnum, refundInfoMapList);
                if (refundAmount >= 0) { // 说明该退款信息中 含有指定方式下的退款金额
                    OrderDetail _orderDetail = orderDetailMap.get(workOrder.getOrderId());
                    if (_orderDetail == null) {
                        log.warn("导出商品开票信息 从退款信息中没有获取到订单详情记录,surOrderId:{}", workOrder.getOrderId());
                        AlarmUtil.alarmAsync("导出商品开票信息" , "从退款信息中没有获取到订单详情记录,surOrderId:" + workOrder.getOrderId());
                        continue;
                    }

                    // mpu
                    String mpu = _orderDetail.getMpu();

                    ExportReceiptBillVo exportReceiptBillVo = exportReceiptBillVoMap.get(mpu);
                    if (exportReceiptBillVo == null) {
                        exportReceiptBillVo = new ExportReceiptBillVo();
                        exportReceiptBillVo.setMpu(mpu);

                        exportReceiptBillVoMap.put(mpu, exportReceiptBillVo);
                    }


                    // 总计退款金额
                    Integer _ra = exportReceiptBillVo.getTotalPrice() == null ? 0 : exportReceiptBillVo.getTotalPrice();
                    exportReceiptBillVo.setTotalPrice(_ra + refundAmount);

                    // 总计数量
                    Integer _c = exportReceiptBillVo.getCount() == null ? 0 : exportReceiptBillVo.getCount();
                    exportReceiptBillVo.setCount(_c + _orderDetail.getNum());
                }
            }

            return exportReceiptBillVoMap;
        } catch (Exception e) {
            log.error("导出商品开票信息 处理退款记录异常:{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 处理入账的发票信息
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @param receiptTypeEnum
     * @return
     */
    private Map<String, ExportReceiptBillVo> handleIncomeInvoiceInfo(Date startTime, Date endTime, String appId, ReceiptTypeEnum receiptTypeEnum) {
        try {
            // 1. 首先查询符合条件的主订单
            List<Orders> payedOrdersList = ordersDao.selectPayedOrdersListByPaymentTime(startTime, endTime, appId); // 已经支付的主订单集合

            log.info("导出商品开票信息 查询已经支付的主订单集合个数:{}", payedOrdersList.size());

            if (CollectionUtils.isEmpty(payedOrdersList)) {
                log.warn("导出商品开票信息 未找到符合条件的子订单");
                return Collections.emptyMap(); //
            }

            // 2. 查询子订单
            // 获取主订单id集合
            List<Integer> orderIdList = payedOrdersList.stream().map(o -> o.getId()).collect(Collectors.toList());
            // 分区
            List<List<Integer>> orderIdListPartition = Lists.partition(orderIdList, LIST_PARTITION_SIZE_200);

            // 根据主订单id集合 查询子订单信息
            List<OrderDetail> orderDetailList = new ArrayList<>(); //
            for (List<Integer> _orderIdList : orderIdListPartition) {
                List<OrderDetail> _orderDetailList = orderDetailDao.selectOrderDetailsByOrdersIdList(_orderIdList);
                orderDetailList.addAll(_orderDetailList);
            }

            log.info("导出商品开票信息 找到子订单个数是:{}", orderDetailList.size());
            if (CollectionUtils.isEmpty(orderDetailList)) {
                log.warn("导出商品开票信息 未找到符合条件的主订单");

                return Collections.emptyMap(); //
            }

            // x1. 主订单转map, 这里转成两个map
            // 第一个, key:paymentNo, value:List<Orders>
            // 第二个, key:id, value:Order
            Map<String, List<Orders>> ordersPaymentNoMap = new HashMap<>(); //
            Map<Integer, Orders> ordersIdMap = new HashMap<>(); //
            for (Orders _orders : payedOrdersList) {
                String paymentNo = _orders.getPaymentNo();

                ordersIdMap.put(_orders.getId(), _orders);

                if (StringUtils.isNotBlank(paymentNo)) {
                    List<Orders> _list = ordersPaymentNoMap.get(paymentNo);
                    if (_list == null) {
                        _list = new ArrayList<>();

                        ordersPaymentNoMap.put(paymentNo, _list);
                    }

                    _list.add(_orders);
                } else {
                    log.warn("导出商品开票信息 主订单id:{} 的paymentNo为空", _orders.getId());
                    AlarmUtil.alarmAsync("导出商品开票信息", "主订单id:" + _orders.getId() + "的paymentNo为空");
                }
            }

            // x2. 子订单转map key:主订单id， value:List<OrderDetail>
            Map<Integer, List<OrderDetail>> orderDetailMap = new HashMap<>(); //
            for (OrderDetail orderDetail : orderDetailList) {
                Integer orderId = orderDetail.getOrderId(); // 主订单id
                if (orderId != null) {
                    List<OrderDetail> _list = orderDetailMap.get(orderId);
                    if (_list == null) {
                        _list = new ArrayList<>();
                        orderDetailMap.put(orderId, _list);
                    }

                    _list.add(orderDetail);
                } else {
                    log.warn("导出商品开票信息 子订单id:{} 无主订单id", orderDetail.getId());
                    AlarmUtil.alarmAsync("导出商品开票信息", "子订单id:" + orderDetail.getId() + "无主订单id");
                }
            }


            // 3. 根据payNo，查询支付信息
            // 获取payNo
            List<String> paymentNoList = payedOrdersList.stream().map(o -> o.getPaymentNo()).collect(Collectors.toList());
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
                Integer payAmount = judgePaymentType(receiptTypeEnum, paymentMethodMap.get(paymentNo)); // judgeBanlanceCardWoaPayment(paymentMethodMap.get(paymentNo)); // "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式的支付总额
                if (payAmount > 0) { // 说明该用户单(支付单)在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种支付方式中 (20191227 这里排除等于0的情况,因为等于0不用开发票)
                    // 遍历主订单
                    for (Orders _orders : ordersPaymentNoMap.get(paymentNo)) { // 遍历主订单
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
                            _paymentInfoByMpuDimension.setTotalPrice(_tp + CalculateUtil.convertYuanToFen(_orderDetail.getSalePrice().toString()) * _orderDetail.getNum());

                            totalAmount = totalAmount + CalculateUtil.convertYuanToFen(_orderDetail.getSalePrice().toString()) * _orderDetail.getNum(); // 单位 分
                        } // end 遍历子订单
                    } // end 遍历主订单

                    // 开始计算 该用户单下 每个mpu所占的payAmount的份额
                    if (totalAmount == 0) { // 目前测试时, 出现这种不合法数据, 如遇到则报警
                        log.warn("导出商品开票信息 根据订单详情信息计算出的总价为0(需要关注)");

                        AlarmUtil.alarmAsync("导出商品开票信息", "该用户单根据子订单信息计算出的总价为0(需要关注) 用户单号(支付单号):" + paymentNo);
                        continue;
                    }

                    // 开始分配该用户单下的各个mpu分得的payAmount
                    BigDecimal multiplier = new BigDecimal(payAmount).divide(new BigDecimal(totalAmount), 2, BigDecimal.ROUND_HALF_UP);
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
                return Collections.emptyMap();
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
                    _exportReceiptBillVo.setTotalPrice(
                            totalPrice + (_paymentInfoByMpuDimension.getHoldAmount() == null ? 0 : _paymentInfoByMpuDimension.getHoldAmount())); // 含税总额 单位分

                }
            }

            return exportReceiptBillVoMap;
        } catch (Exception e) {
            log.error("导出商品开票信息 异常:{}", e.getMessage(), e);
            throw e;
        }
    }


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
     * 根据指定的开票类型，获取用户单该在 该'开票类型' 下的支付方式的支付总额
     * (判断发票的类型, 进而获取该类型的支付总额)
     *
     * @param receiptTypeEnum 指定的开票类型
     * @param orderPayMethodInfoBeanList 某个支付单号下的支付方式的集合; 换一种表述，这是一个用户单下的支付方式集合
     * @return
     */
    private Integer judgePaymentType(ReceiptTypeEnum receiptTypeEnum, List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList) {
        switch (receiptTypeEnum) {
            case BALANCE_CARD_WOA:
                return judgeBanlanceCardWoaPayment(orderPayMethodInfoBeanList);
            case BANK:
                return judgeBankPayment(orderPayMethodInfoBeanList);

            default:
                log.warn("获取用户单该在 该'开票类型' 下的支付方式的支付总额 未找到有效的发票类型!");
                return -1;
        }
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
     * 判断其支付方式是否是 "bank" 中投快捷支付
     *
     * @param orderPayMethodInfoBeanList 某个支付单号下的支付方式的集合; 换一种表述，这是一个用户单下的支付方式集合
     * @return
     * -1(<0) : 说明该支付订单的支付方式不在 ("bank" 中投快捷支付) 这种支付方式中
     * 其他值(>=0) : 返回该支付单子在（"bank" 中投快捷支付）这种支付方式
     */
    private Integer judgeBankPayment(List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList) {
        Integer total = -1; // 单位分

        // 遍历支付方式
        for (OrderPayMethodInfoBean orderPayMethodInfoBean : orderPayMethodInfoBeanList) {
            String payType = orderPayMethodInfoBean.getPayType();

            if (PaymentTypeEnum.BANK.getName().equals(payType)) {
                if (total < 0) {
                    total = 0;
                }

                if (StringUtils.isNotBlank(orderPayMethodInfoBean.getActPayFee())) { // 单位分
                    total = total + Integer.valueOf(orderPayMethodInfoBean.getActPayFee());
                } else {
                    log.warn("判断其支付方式是否是 bank 支付金额不合法:{}", orderPayMethodInfoBean.getActPayFee());
                }
            }
        }

        return total;
    }

    /////

    /**
     * 根据指定的开票类型，获取某个sku在该'开票类型'下的退款金额
     *
     * @param receiptTypeEnum
     * @param refundInfoList
     * @return 单位分
     */
    private Integer calcRefundAmount(ReceiptTypeEnum receiptTypeEnum, List<Map<String, Object>> refundInfoList) {
        switch (receiptTypeEnum) {
            case BALANCE_CARD_WOA:
                return calcBanlanceCardWoaRefundAmount(refundInfoList);
            case BANK:
                return calcBankRefundAmount(refundInfoList);

            default:
                log.warn("获取用户单该在 该'开票类型' 下的支付方式的支付总额 未找到有效的发票类型!");
                return -1;
        }
    }

    /**
     * 判断其退款方式是否是 "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户
     *
     * @param refundInfoList 某个sku的退款详情
     *
     * [
     *   {
     *     "createDate": "2019-12-20T16:11:47",
     *     "merchantCode": "32",
     *     "orderNo": "e2f2cf41ca674b51aeaeaac8a584fa79",
     *     "outRefundNo": "111576829506646",
     *     "payType": "balance",
     *     "refundFee": "10",
     *     "refundNo": "",
     *     "sourceOutTradeNo": "118111a3bf2248b9e6404c87dff0892572ebb953283501",
     *     "status": 1,
     *     "statusMsg": "退款成功",
     *     "totalFee": "0",
     *     "tradeDate": "20191220161146"
     *   }
     * ]
     *
     * @return
     * -1(<0) : 说明该退款不在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）这几种退款方式中
     * 其他值(>=0) : 返回该退款在（"balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户）
     */
    private Integer calcBanlanceCardWoaRefundAmount(List<Map<String, Object>> refundInfoList) {
        Integer total = -1; // "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户  支付的总额 单位 分

        // 遍历支付方式
        for (Map<String, Object> refundInfo : refundInfoList) {
            String payType = refundInfo.get("payType") == null ? "" : (String) refundInfo.get("payType");
            int payStatus = refundInfo.get("status") == null ? -1 : (Integer) refundInfo.get("status");

            if (payStatus != 1 && payStatus != 3) { // 如果退款没有成功
                log.warn("判断其退款方式是否是 balance card woa 退款详情中含有退款失败的记录outRefundNo:", refundInfo.get("outRefundNo"));
                AlarmUtil.alarmAsync("导出商品开票信息-判断其退款方式", "判断其退款方式是否是 balance card woa 退款详情中含有退款失败的记录outRefundNo:" + refundInfo.get("outRefundNo"));
                continue;
            }

            if (PaymentTypeEnum.BALANCE.getName().equals(payType)
                    || PaymentTypeEnum.CARD.getName().equals(payType)
                    || PaymentTypeEnum.WOA.getName().equals(payType)) {
                if (total < 0) {
                    total = 0;
                }

                // 退款金额
                String refudnAmount = (String) refundInfo.get("refundFee");
                if (StringUtils.isNotBlank(refudnAmount)) { // 单位分
                    total = total + Integer.valueOf(refudnAmount);
                } else {
                    log.warn("判断其退款方式是否是 balance card woa 退款金额不合法:{}", refudnAmount);
                }
            }
        }

        return total;
    }

    /**
     * 判断其退款方式是否是 "bank" 中投快捷支付
     *
     * @param refundInfoList 某个sku下的退款方式的集合;
     *
     * [
     *   {
     *     "createDate": "2019-12-20T16:11:47",
     *     "merchantCode": "32",
     *     "orderNo": "e2f2cf41ca674b51aeaeaac8a584fa79",
     *     "outRefundNo": "111576829506646",
     *     "payType": "balance",
     *     "refundFee": "10",
     *     "refundNo": "",
     *     "sourceOutTradeNo": "118111a3bf2248b9e6404c87dff0892572ebb953283501",
     *     "status": 1,
     *     "statusMsg": "退款成功",
     *     "totalFee": "0",
     *     "tradeDate": "20191220161146"
     *   }
     * ]
     * @return
     * -1(<0) : 说明该退款方式不在 ("bank" 中投快捷支付)
     * 其他值(>=0) : 返回该退款在（"bank" 中投快捷支付）
     */
    private Integer calcBankRefundAmount(List<Map<String, Object>> refundInfoList) {
        Integer total = -1; // 单位分

        // 遍历支付方式
        for (Map<String, Object> refundInfo  : refundInfoList) {
            String payType = refundInfo.get("payType") == null ? "" : (String) refundInfo.get("payType");
            int payStatus = refundInfo.get("status") == null ? -1 : (Integer) refundInfo.get("status");
            if (payStatus != 1) { // 如果退款没有成功
                log.warn("判断其退款方式是否是 balance card woa 退款详情中含有退款失败的记录outRefundNo:", refundInfo.get("outRefundNo"));
                AlarmUtil.alarmAsync("导出商品开票信息-判断其退款方式", "判断其退款方式是否是 bank 退款详情中含有退款失败的记录outRefundNo:" + refundInfo.get("outRefundNo"));
                continue;
            }

            if (PaymentTypeEnum.BANK.getName().equals(payType)) {
                if (total < 0) {
                    total = 0;
                }

                // 退款金额
                String refudnAmount = (String) refundInfo.get("refundFee");
                if (StringUtils.isNotBlank(refudnAmount)) { // 单位分
                    total = total + Integer.valueOf(refudnAmount);
                } else {
                    log.warn("判断其退款方式是否是 bank 支付金额不合法:{}", refudnAmount);
                }
            }
        }

        return total;
    }
}
