package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.ExportExpressFeeVo;
import com.fengchao.order.bean.vo.ExportLoanSettlementVo;
import com.fengchao.order.bean.vo.ExportMerchantReceiptVo;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.FreightsRpcService;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.VendorsRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.extmodel.*;
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

    private ProductRpcService productRpcService;

    private VendorsRpcService vendorsRpcService;

    @Autowired
    public ExportStatisticServiceImpl(OrdersDao ordersDao,
                                      OrderDetailDao orderDetailDao,
                                      FreightsRpcService freightsRpcService,
                                      WorkOrderRpcService workOrderRpcService,
                                      ProductRpcService productRpcService,
                                      VendorsRpcService vendorsRpcService) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
        this.freightsRpcService = freightsRpcService;
        this.workOrderRpcService = workOrderRpcService;
        this.productRpcService = productRpcService;
        this.vendorsRpcService = vendorsRpcService;
    }

    @Override
    public ExportLoanSettlementVo exportSettlement(Date startTime, Date endTime,
                                                      List<String> appIdList, Integer merchantId) throws Exception {

        // 1. 查询入账子订单
        // 1.1 查询
        List<OrderDetail> incomeOrderDetailList = queryIncomeOrderDetails(startTime, endTime, appIdList, merchantId);

        log.info("导出商户货款结算表 获取入账子订单:{}", JSONUtil.toJsonString(incomeOrderDetailList));

        if (CollectionUtils.isEmpty(incomeOrderDetailList)) {
            log.warn("导出商户货款结算表 数据为空!!");
            return null;
        }

        // 2. 查询出账的订单
        // 获取已退款的信息集合
        List<WorkOrder> workOrderList = new ArrayList<>();
        if (CollectionUtils.isEmpty(appIdList)) {
            workOrderList = workOrderRpcService.queryRefundedOrderDetailList(merchantId, null, startTime, endTime);
        } else {
            for (String appId : appIdList) {
                List<WorkOrder> _tmpList = workOrderRpcService.queryRefundedOrderDetailList(merchantId, appId, startTime, endTime);

                workOrderList.addAll(_tmpList);
            }
        }

        log.info("导出商户货款结算表 获取退款信息:{}", JSONUtil.toJsonString(workOrderList));

        // 3. 查询运费模版
        List<Integer> merchantIdList = incomeOrderDetailList.stream()
                .map(f -> f.getMerchantId()).collect(Collectors.toList());
        List<ShipTemplateBean> shipTemplateBeanList = freightsRpcService.queryMerchantExceptionFee(merchantIdList);
        // 转map  key: merchantId, value:ShipTemplateBean对象
        Map<Integer, ShipTemplateBean> shipTemplateBeanMap =
                shipTemplateBeanList.stream().collect(Collectors.toMap(s -> s.getMerchantId(), s -> s));

        log.info("导出商户货款结算表 获取运费模版map:{}", JSONUtil.toJsonString(shipTemplateBeanMap));

        // x. 查询一下供应商名称
        List<SysCompanyX> sysCompanyXList = vendorsRpcService.queryAllCompanyList();
        // 转map key:merchantId
        Map<Integer, SysCompanyX> merchantMap = sysCompanyXList.stream().collect(Collectors.toMap(s -> s.getId().intValue(), s -> s));
        log.info("导出商户货款结算表 获取供应商map:{}", JSONUtil.toJsonString(merchantMap));

        // 5. 计算导出数据遍历入账的子订单
        String startTimeStr = DateUtil.dateTimeFormat(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        String endTimeStr = DateUtil.dateTimeFormat(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        // 5.1 处理完成订单金额
        Integer completeOrderAmount = 0; // 总计完成订单金额 单位分

        // 将入账子订单以主订单的纬度 转map
        Map<Integer, List<OrderDetail>> incomeOrderMap = new HashMap<>();
        for (OrderDetail orderDetail : incomeOrderDetailList) {
            // expressAmount = expressAmount + calcExpressFee(orderDetail, shipTemplateBeanMap);

            if (orderDetail.getSprice() != null) {
                completeOrderAmount = completeOrderAmount +
                        CalculateUtil.convertYuanToFen(orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).toString());
            }

            // 转主订单
            List<OrderDetail> _orderDetailList = incomeOrderMap.get(orderDetail.getOrderId());
            if (_orderDetailList == null) {
                _orderDetailList = new ArrayList<>();
                incomeOrderMap.put(orderDetail.getOrderId(), _orderDetailList);
            }
            _orderDetailList.add(orderDetail);
        }

        log.info("导出商户货款结算表 将入账子订单转以主订单的纬度Map<Integer, List<OrderDetail>>:{}",
                JSONUtil.toJsonString(incomeOrderMap));

        // 5.2 处理运费
        Integer expressAmount = 0; // 总计快递费 单位分
        StringBuilder logBuilder = new StringBuilder();
        for (Integer orderId : incomeOrderMap.keySet()) {
            List<OrderDetail> _orderDetailList = incomeOrderMap.get(orderId);
            //
            Integer merchantExpressFee = calcExpressFee(_orderDetailList, shipTemplateBeanMap.get(orderId));
            logBuilder.append("商户:" + merchantId + " 主订单:" + orderId + " 运费:" + merchantExpressFee + "\r\n");
            expressAmount = expressAmount + merchantExpressFee;
        }

        log.info("导出商户货款结算表 商户运费计算明细:{}", logBuilder.toString());

        // 5.2 处理出账
        // 查询子订单
        List<String> subOrderIdList = workOrderList.stream().map(w -> w.getOrderId()).collect(Collectors.toList());
        log.info("导出商户货款结算表 数据库入参: {}", JSONUtil.toJsonString(subOrderIdList));

        List<OrderDetail> outOrderDetailList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subOrderIdList)) {
            outOrderDetailList = orderDetailDao.selectOrderDetailListBySubOrderIds(subOrderIdList);
        }
        log.info("导出商户货款结算表 出账子订单是:{}", JSONUtil.toJsonString(outOrderDetailList));
        // 计算退款总价
        Integer refundOrderAmount = 0; // 单位 分
        for (OrderDetail orderDetail : outOrderDetailList) {
            if (orderDetail.getSprice() != null) {
                refundOrderAmount = refundOrderAmount +
                        CalculateUtil.convertYuanToFen(orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).toString());
            }
        }

        log.info("导出商户货款结算表 completeOrderAmount = {}", completeOrderAmount);
        log.info("导出商户货款结算表 refundOrderAmount = {}", refundOrderAmount);
        log.info("导出商户货款结算表 expressAmount = {}", expressAmount);

        // 5.3 组装导出数据
        ExportLoanSettlementVo exportLoanSettlementVo = new ExportLoanSettlementVo();
        exportLoanSettlementVo.setMerchantName(merchantMap.get(merchantId) == null ? "--" : merchantMap.get(merchantId).getName()); // 商户名称
        exportLoanSettlementVo.setSettlementPeriod(startTimeStr + "-" + endTimeStr); // 结算周期
        exportLoanSettlementVo.setCompleteOrderAmount(CalculateUtil.converFenToYuan(completeOrderAmount)); // 已完成订单金额 单位元
        exportLoanSettlementVo.setRefundOrderAmount(CalculateUtil.converFenToYuan(refundOrderAmount)); // 已退款订单金额 单位元
        exportLoanSettlementVo.setRealyOrderAmount(CalculateUtil.converFenToYuan(completeOrderAmount - refundOrderAmount)); // 实际成交订单金额 单位元
        exportLoanSettlementVo.setCouponAmount("0"); // 优惠券金额 单位元
        exportLoanSettlementVo.setExpressFee(CalculateUtil.converFenToYuan(expressAmount)); // 运费金额 单位元
        exportLoanSettlementVo.setPayAmout(CalculateUtil.converFenToYuan(completeOrderAmount - refundOrderAmount + expressAmount)); // 应付款 单位元

        log.info("导出商户货款结算表 组装导出数据ExportLoanSettlementVo:{}", JSONUtil.toJsonString(exportLoanSettlementVo));

        return exportLoanSettlementVo;
    }

    @Override
    public List<ExportExpressFeeVo> exportExpressFee(Date startTime, Date endTime, List<String> appIdList) throws Exception {
        // 1. 查询入账子订单
        // 1.1 查询
        List<OrderDetail> originOrderDetailList =
                orderDetailDao.selectOrderDetailsForReconciliation(null, startTime, endTime);
        log.info("导出运费实际收款报表 获取的所有收入子订单:{}", JSONUtil.toJsonString(originOrderDetailList));
        if (CollectionUtils.isEmpty(originOrderDetailList)) {
            log.warn("导出运费实际收款报表 数据为空");
            return null;
        }

        // 1.2 根据第1步查询主订单
        List<Integer> queryOrderIdList = originOrderDetailList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        List<Orders> ordersList = ordersDao.selectOrdersByIdsAndAppIds(queryOrderIdList, appIdList);
        log.info("导出运费实际收款报表 获取主订单列表:{}", JSONUtil.toJsonString(ordersList));

        // x 转map 按照供应商分类 key:merchantId, value : 该供应商下的主订单集合
        Map<Integer, List<Orders>> ordersMap = new HashMap<>();
        for (Orders orders : ordersList) {
            Integer _merchantId = orders.getMerchantId();
            List<Orders> _ordersList = ordersMap.get(_merchantId);

            if (_ordersList == null) {
                _ordersList = new ArrayList<>();
                ordersMap.put(_merchantId, _ordersList);
            }

            _ordersList.add(orders);
        }
        log.info("导出运费实际收款报表 按照供货商分类主订单信息:{}", JSONUtil.toJsonString(ordersMap));

        // 1.3 根据appIds过滤子订单
        List<Integer> ordersIdList = ordersList.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<OrderDetail> incomeOrderDetailList = new ArrayList<>(); // 根据appIds过滤后 剩下的子订单
        for (OrderDetail orderDetail : originOrderDetailList) {
            if (ordersIdList.contains(orderDetail.getOrderId())) {
                incomeOrderDetailList.add(orderDetail);
            }
        }
        log.info("导出运费实际收款报表 获取入账子订单:{}", JSONUtil.toJsonString(incomeOrderDetailList));

        if (CollectionUtils.isEmpty(incomeOrderDetailList)) {
            log.warn("导出运费实际收款报表 数据为空!!");
            return null;
        }

        // x 将入账订单按照merchantId分类 转map
        Map<Integer, List<OrderDetail>> orderDetailMap = new HashMap<>();
        for (OrderDetail orderDetail : incomeOrderDetailList) {
            Integer _merchantId = orderDetail.getMerchantId();
            List<OrderDetail> _orderDetailList = orderDetailMap.get(_merchantId);

            if (_orderDetailList == null) {
                _orderDetailList = new ArrayList<>();
                orderDetailMap.put(_merchantId, _orderDetailList);
            }

            _orderDetailList.add(orderDetail);
        }

        log.info("导出运费实际收款报表 按照供货商分类子订单信息:{}", JSONUtil.toJsonString(orderDetailMap));

        // 3. 查询运费模版
        List<Integer> merchantIdList = incomeOrderDetailList.stream()
                .map(f -> f.getMerchantId()).collect(Collectors.toList());
        List<ShipTemplateBean> shipTemplateBeanList = freightsRpcService.queryMerchantExceptionFee(merchantIdList);
        // 转map
        Map<Integer, ShipTemplateBean> shipTemplateBeanMap =
                shipTemplateBeanList.stream().collect(Collectors.toMap(s -> s.getMerchantId(), s -> s));
        log.info("导出运费实际收款报表 获取运费模版map:{}", JSONUtil.toJsonString(shipTemplateBeanMap));


        // 5. 处理导出数据
        // 5.1 计算供应商的运费
        Map<Integer, Integer> merchantExpressFeeMap = new HashMap<>();
        StringBuilder logBuilder = new StringBuilder(); // 记录计算明细log
        for (Integer merchantId : orderDetailMap.keySet()) { // 遍历供应商
            List<OrderDetail> orderDetailList = orderDetailMap.get(merchantId); // 该供应商下的子订单列表

            // 将子订单按照主订单的纬度转map
            Map<Integer, List<OrderDetail>> _orderMap = new HashMap<>();
            for (OrderDetail _orderDetail : orderDetailList) {
                List<OrderDetail> _tmpList = _orderMap.get(_orderDetail.getOrderId());
                if (_tmpList == null) {
                    _tmpList = new ArrayList<>();
                    _orderMap.put(_orderDetail.getOrderId(), _tmpList);
                }
                _tmpList.add(_orderDetail);
            }

            // 计算该供应商的运费
            Integer merchantExpressFee = 0;
            for (Integer orderId : _orderMap.keySet()) {
                List<OrderDetail> _orderDetailList = _orderMap.get(orderId);
                Integer _tmpExpressFee = calcExpressFee(_orderDetailList, shipTemplateBeanMap.get(merchantId));

                logBuilder.append("商户:" + merchantId + " 主订单:" + orderId + " 运费:" + _tmpExpressFee + "\r\n");

                merchantExpressFee = merchantExpressFee + _tmpExpressFee;
            }

            merchantExpressFeeMap.put(merchantId, merchantExpressFee);
        }

        log.info("导出运费实际收款报表 商户运费计算明细:{}", logBuilder.toString());

        log.info("导出运费实际收款报表 计算供应商的运费:{}", JSONUtil.toJsonString(merchantExpressFeeMap));

        // 5.2 计算用户的运费
        Map<Integer, Integer> userExpressFeeMap = new HashMap<>();
        for (Integer merchantId : ordersMap.keySet()) {
            List<Orders> _ordersList =  ordersMap.get(merchantId);

            Integer userExpressFee = 0;
            for (Orders orders : _ordersList) {
                userExpressFee = userExpressFee
                        + CalculateUtil.convertYuanToFen(orders.getServFee() == null ? "0" : String.valueOf(orders.getServFee()));
            }

            userExpressFeeMap.put(merchantId, userExpressFee);
        }

        log.info("导出运费实际收款报表 计算用户的运费:{}", JSONUtil.toJsonString(userExpressFeeMap));

        // x. 查询一下供应商名称
        List<SysCompanyX> sysCompanyXList = vendorsRpcService.queryAllCompanyList();
        // 转map key:merchantId
        Map<Integer, SysCompanyX> merchantMap = sysCompanyXList.stream().collect(Collectors.toMap(s -> s.getId().intValue(), s -> s));
        log.info("导出运费实际收款报表 获取供应商map:{}", JSONUtil.toJsonString(merchantMap));

        // 6 组装导出数据
        List<ExportExpressFeeVo> exportExpressFeeVoList = new ArrayList<>();
        for (Integer merchantId : orderDetailMap.keySet()) {
            ExportExpressFeeVo exportExpressFeeVo = new ExportExpressFeeVo();
            exportExpressFeeVo.setMerchantId(merchantId);
            exportExpressFeeVo.setMerchantName(merchantMap.get(merchantId) == null ? "--" : merchantMap.get(merchantId).getName()); // 商户名称
            exportExpressFeeVo.setUserExpressFee(CalculateUtil.converFenToYuan(userExpressFeeMap.get(merchantId))); // 用户支付运费金额 单位元
            exportExpressFeeVo.setMerchantExpressFee(CalculateUtil.converFenToYuan(merchantExpressFeeMap.get(merchantId))); // 供应商运费金额 单位元

            exportExpressFeeVoList.add(exportExpressFeeVo);
        }

        log.info("导出运费实际收款报表 组装导出数据List<ExportExpressFeeVo>:{}", JSONUtil.toJsonString(exportExpressFeeVoList));

        return exportExpressFeeVoList;
    }

    @Override
    public List<ExportMerchantReceiptVo> exportMerchantReceipt(Date startTime, Date endTime,
                                                               List<String> appIdList, Integer merchantId) {
        // 1. 获取入账子订单集合
        List<OrderDetail> incomeOrderDetailList = queryIncomeOrderDetails(startTime, endTime, appIdList, merchantId);

        log.info("导出供应商发票 获取入账子订单:{}", JSONUtil.toJsonString(incomeOrderDetailList));
        if (CollectionUtils.isEmpty(incomeOrderDetailList)) {
            log.warn("导出供应商发票 数据为空!!");
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
        log.info("导出供应商发票 获取退款信息:{}", JSONUtil.toJsonString(workOrderList));

        // 将退款的子订单按照子订单号纬度转map
        Map<String, WorkOrder> workOrderMap = workOrderList.stream().collect(Collectors.toMap(w -> w.getOrderId(), w -> w));
        log.info("导出供应商发票 获取退款信息转map:{}", JSONUtil.toJsonString(workOrderMap));

        // x. 查询商品信息
        List<String> mpuList = incomeOrderDetailList.stream().map(i -> i.getMpu()).collect(Collectors.toList());
        List<ProductInfoBean> productInfoBeanList = productRpcService.findProductListByMpuIdList(mpuList);
        // 转map key：mpu
        Map<String, ProductInfoBean> productInfoBeanMap = productInfoBeanList.stream()
                .collect(Collectors.toMap(p -> p.getMpu(), p -> p, (value1, value2) -> {
                    return value2;
                }));
        log.info("导出供应商发票 获取商品map:{}", JSONUtil.toJsonString(productInfoBeanMap));

        // 3. 合并出账入账订单 & 根据mpu的纬度聚合
        Map<String, List<OrderDetail>> mergeOrderDetailInMpu = new HashMap<>();
        for (OrderDetail incomeOrderDetail : incomeOrderDetailList) { // 遍历入账子订单
            // 设置商品名称
            String productName = productInfoBeanMap.get(incomeOrderDetail.getMpu()) == null ?
                    incomeOrderDetail.getName() : productInfoBeanMap.get(incomeOrderDetail.getMpu()).getName();
            incomeOrderDetail.setName(productName);

            // 合并
            String subOrderId = incomeOrderDetail.getSubOrderId();
            WorkOrder _workOrder = workOrderMap.get(subOrderId);
            if (_workOrder != null) {
                incomeOrderDetail.setNum(incomeOrderDetail.getNum() - _workOrder.getReturnedNum());
            }

            //
            List<OrderDetail> orderDetailList = mergeOrderDetailInMpu.get(incomeOrderDetail.getMpu());
            if (orderDetailList == null) {
                orderDetailList = new ArrayList<>();
                mergeOrderDetailInMpu.put(incomeOrderDetail.getMpu(), orderDetailList);
            }
            orderDetailList.add(incomeOrderDetail);
        }
        log.info("导出供应商发票 合并出账入账后的以mpu纬度聚合的子订单map:{}", JSONUtil.toJsonString(mergeOrderDetailInMpu));

        // 7. 组装导出数据
        List<ExportMerchantReceiptVo> exportMerchantReceiptVoList = new ArrayList<>();
        for (String mpu : mergeOrderDetailInMpu.keySet()) {
            List<OrderDetail> orderDetailList = mergeOrderDetailInMpu.get(mpu);

            String productName = productInfoBeanMap.get(mpu) == null ?
                    "--" : productInfoBeanMap.get(mpu).getName();
            String categoryName = productInfoBeanMap.get(mpu) == null ?
                    "--" : productInfoBeanMap.get(mpu).getCategoryName();
            Integer num = 0;
            String taxRate = productInfoBeanMap.get(mpu).getTaxRate(); // 税率

            Integer orderDetailTotalAmountInMpu = 0; // 子订单的总价(含税金额)
            for (OrderDetail orderDetail : orderDetailList) {
//                productName = orderDetail.getName();
//                categoryName = orderDetail.getCategory();
                num = num + orderDetail.getNum();

                if (orderDetail.getSprice() != null) {
                    orderDetailTotalAmountInMpu = orderDetailTotalAmountInMpu +
                            CalculateUtil.convertYuanToFen(orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).toString());
                }
            }

            ExportMerchantReceiptVo exportMerchantReceiptVo = new ExportMerchantReceiptVo();
            exportMerchantReceiptVo.setProductName(productName); // 商品名称
            exportMerchantReceiptVo.setCategoryId(categoryName); // 品类
            exportMerchantReceiptVo.setCategoryName(categoryName); // 品类
            exportMerchantReceiptVo.setNum(num); // 数量
            exportMerchantReceiptVo.setTaxRate(taxRate); // 税率
            exportMerchantReceiptVo.setAmount(CalculateUtil.converFenToYuan(orderDetailTotalAmountInMpu)); // 含税金额

            // 税额
            if (StringUtils.isNotBlank(taxRate)) {
                int taxPrice = new BigDecimal(orderDetailTotalAmountInMpu)
                        .divide(new BigDecimal(taxRate).add(new BigDecimal(1)),2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(taxRate))
                        .setScale(0, BigDecimal.ROUND_HALF_UP)
                        .intValue(); // 单位分

                exportMerchantReceiptVo.setTaxAmount(CalculateUtil.converFenToYuan(taxPrice)); // 税额
            }

            // 进货单价
            if (num == 0) {
                exportMerchantReceiptVo.setSprice("0");
            } else {
                exportMerchantReceiptVo.setSprice(CalculateUtil.converFenToYuan(orderDetailTotalAmountInMpu / num)); // 进货单价
            }
            exportMerchantReceiptVo.setTaxType(""); // 税收类型

            exportMerchantReceiptVoList.add(exportMerchantReceiptVo);
        }

        log.info("导出供应商发票 组装导出结果List<ExportMerchantReceiptVo>:{}",
                JSONUtil.toJsonString(exportMerchantReceiptVoList));


        return exportMerchantReceiptVoList;
    }

    /// ========================== private ======================================

    /**
     * 计算子订单的运费
     *
     * @param orderDetail
     * @param expressFeeMap
     * @return
     */
    @Deprecated
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
        int basePrice = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getBasePrice()));
        int baseAmount = shipRegionsBean.getBaseAmount();
        int cumulativePrice = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getCumulativePrice()));
        int cumulativeUnit = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getCumulativeUnit()));

        //
        int num = orderDetail.getNum();
        if (baseAmount <= num)  {
            fee = basePrice;
        } else {
            fee = basePrice + ((num - baseAmount) / cumulativeUnit) * cumulativePrice;
        }

        return fee;
    }


    /**
     * 计算子订单集合的运费（主订单纬度）
     *
     * @param orderDetailList
     * @param shipTemplateBean
     * @return
     */
    private Integer calcExpressFee(List<OrderDetail> orderDetailList, ShipTemplateBean shipTemplateBean) {
        Integer fee = 0;

        if (shipTemplateBean == null) {
            log.warn("商户ID:{} 没有运费模版", shipTemplateBean.getMerchantId());
            return 0;
        }

        List<ShipRegionsBean> shipRegionsBeanList = shipTemplateBean.getRegions();
        if (CollectionUtils.isEmpty(shipRegionsBeanList)) {
            log.warn("商户ID:{} 没有区域运费模版", shipTemplateBean.getMerchantId());
            return 0;
        }

        ShipRegionsBean shipRegionsBean = shipTemplateBean.getRegions().get(0);
        int basePrice = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getBasePrice()));
        int baseAmount = shipRegionsBean.getBaseAmount();
        int cumulativePrice = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getCumulativePrice()));
        int cumulativeUnit = CalculateUtil.convertYuanToFen(String.valueOf(shipRegionsBean.getCumulativeUnit()));

        // 计算子订单的总数量
        int totalNum = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            totalNum = totalNum + orderDetail.getNum();
        }

        // 计算运费
        if (baseAmount <= totalNum)  {
            fee = basePrice;
        } else {
            fee = basePrice + ((totalNum - baseAmount) / cumulativeUnit) * cumulativePrice;
        }

        return fee;
    }

    /**
     * 查询入账订单
     *
     * @param startTime
     * @param endTime
     * @param merchantId
     * @return
     */
    private List<OrderDetail> queryIncomeOrderDetails(Date startTime, Date endTime, List<String> appIdList, Integer merchantId) {
        // 1. 查询入账子订单
        // 1.1 查询子订单
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

        return incomeOrderDetailList;
    }


}


