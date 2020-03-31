package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.vo.ExportExpressFeeVo;
import com.fengchao.order.bean.vo.ExportLoanSettlementVo;
import com.fengchao.order.bean.vo.ExportMerchantReceiptVo;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.feign.FreightsServiceClient;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.OrderDetailX;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.*;
import com.fengchao.order.rpc.extmodel.*;
import com.fengchao.order.service.ExportStatisticService;
import com.fengchao.order.utils.CalculateUtil;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import io.micrometer.core.instrument.util.JsonUtils;
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
        Map<Integer, SysCompanyX> merchantMap = sysCompanyXList.stream().collect(Collectors.toMap(s -> Integer.valueOf(s.getCorporationId()), s -> s));
        log.info("导出商户货款结算表 获取供应商map:{}", JSONUtil.toJsonString(merchantMap));

        // 5. 计算导出数据遍历入账的子订单
        String startTimeStr = DateUtil.dateTimeFormat(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        String endTimeStr = DateUtil.dateTimeFormat(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        // 5.1 处理入账相关的导出信息
        Integer completeOrderAmount = 0; // 总计完成订单金额
        Integer expressAmount = 0; // 总计快递费
        for (OrderDetail orderDetail : incomeOrderDetailList) {
            expressAmount = expressAmount + calcExpressFee(orderDetail, shipTemplateBeanMap);
            completeOrderAmount = completeOrderAmount +
                    orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).intValue();
        }

        // 5.2 处理出账
        // 查询子订单
        List<String> subOrderIdList = workOrderList.stream().map(w -> w.getOrderId()).collect(Collectors.toList());
        List<OrderDetail> outOrderDetailList = orderDetailDao.selectOrderDetailListBySubOrderIds(subOrderIdList);
        log.info("导出商户货款结算表 出账子订单是:{}", JSONUtil.toJsonString(outOrderDetailList));
        // 计算退款总价
        Integer refundOrderAmount = 0;
        for (OrderDetail orderDetail : outOrderDetailList) {
            refundOrderAmount = refundOrderAmount +
                    CalculateUtil.convertYuanToFen(orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).toString());
        }

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
        for (Integer merchantId : orderDetailMap.keySet()) {
            List<OrderDetail> orderDetailList = orderDetailMap.get(merchantId);

            Integer merchantExpressFee = 0;
            for (OrderDetail orderDetail : orderDetailList) {
                Integer orderDetailExpressFee = calcExpressFee(orderDetail, shipTemplateBeanMap);
                merchantExpressFee = merchantExpressFee + orderDetailExpressFee;
            }

            merchantExpressFeeMap.put(merchantId, merchantExpressFee);
        }

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
        Map<Integer, SysCompanyX> merchantMap = sysCompanyXList.stream().collect(Collectors.toMap(s -> Integer.valueOf(s.getCorporationId()), s -> s));
        log.info("导出运费实际收款报表 获取供应商map:{}", JSONUtil.toJsonString(merchantMap));

        // 6 组装导出数据
        List<ExportExpressFeeVo> exportExpressFeeVoList = new ArrayList<>();
        for (Integer merchantId : orderDetailMap.keySet()) {
            ExportExpressFeeVo exportExpressFeeVo = new ExportExpressFeeVo();
            exportExpressFeeVo.setMerchantName(merchantMap.get(merchantId) == null ? "--" : merchantMap.get(merchantId).getCorporationName()); // 商户名称
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

        // 将退款的子订单按照子订单号唯独转map
        Map<String, WorkOrder> workOrderMap = workOrderList.stream().collect(Collectors.toMap(w -> w.getOrderId(), w -> w));
        log.info("导出供应商发票 获取退款信息转map:{}", JSONUtil.toJsonString(workOrderMap));

        // x. 查询商品信息
        List<String> mpuList = incomeOrderDetailList.stream().map(i -> i.getMpu()).collect(Collectors.toList());
        List<ProductInfoBean> productInfoBeanList = productRpcService.findProductListByMpuIdList(mpuList);
        // 转map key：mpu
        Map<String, ProductInfoBean> productInfoBeanMap = productInfoBeanList.stream().collect(Collectors.toMap(p -> p.getMpu(), p -> p));
        log.info("导出供应商发票 获取商品map:{}", JSONUtil.toJsonString(productInfoBeanMap));

        // 3. 合并出账入账订单 & 根据mpu的纬度聚合
        Map<String, List<OrderDetail>> orderDetailInMpu = new HashMap<>();
        for (OrderDetail orderDetail : incomeOrderDetailList) { // 遍历入账子订单
            // 设置商品名称
            String productName = productInfoBeanMap.get(orderDetail.getMpu()) == null ?
                    orderDetail.getName() : productInfoBeanMap.get(orderDetail.getMpu()).getName();
            orderDetail.setName(productName);

            // 合并
            String subOrderId = orderDetail.getSubOrderId();
            WorkOrder _workOrder = workOrderMap.get(subOrderId);
            if (_workOrder != null) {
                orderDetail.setNum(orderDetail.getNum() - _workOrder.getReturnedNum());
            }

            //
            List<OrderDetail> orderDetailList = orderDetailInMpu.get(orderDetail.getMpu());
            if (orderDetailList == null) {
                orderDetailList = new ArrayList<>();
                orderDetailInMpu.put(orderDetail.getMpu(), orderDetailList);
            }
            orderDetailList.add(orderDetail);
        }
        log.info("导出供应商发票 合并出账入账后的以mpu纬度聚合的子订单map:{}", JSONUtil.toJsonString(orderDetailInMpu));

        // 7. 组装导出数据
        List<ExportMerchantReceiptVo> exportMerchantReceiptVoList = new ArrayList<>();
        for (String mpu : orderDetailInMpu.keySet()) {
            List<OrderDetail> orderDetailList = orderDetailInMpu.get(mpu);

            String productName = "";
            String categoryName = "";
            Integer num = 0;
            String taxRate = productInfoBeanMap.get(mpu).getTaxRate(); // 税率
            Integer orderDetailAmount = 0; // 子订单的总价(含税金额)
            for (OrderDetail orderDetail : orderDetailList) {
                productName = orderDetail.getName();
                categoryName = orderDetail.getCategory();
                num = num + orderDetail.getNum();
                orderDetailAmount = orderDetailAmount +
                        CalculateUtil.convertYuanToFen(orderDetail.getSprice().multiply(new BigDecimal(orderDetail.getNum())).toString());
            }

            ExportMerchantReceiptVo exportMerchantReceiptVo = new ExportMerchantReceiptVo();
            exportMerchantReceiptVo.setProductName(productName); // 商品名称
            exportMerchantReceiptVo.setCategoryId(""); // 品类
            exportMerchantReceiptVo.setCategoryName(categoryName); // 品类
            exportMerchantReceiptVo.setNum(num); // 数量
            exportMerchantReceiptVo.setTaxRate(taxRate); // 税率
            exportMerchantReceiptVo.setAmount(CalculateUtil.converFenToYuan(orderDetailAmount)); // 含税金额

            // 税额
            if (StringUtils.isNotBlank(taxRate)) {
                int taxPrice = new BigDecimal(orderDetailAmount)
                        .divide(new BigDecimal(taxRate).add(new BigDecimal(1)),2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(taxRate))
                        .setScale(0, BigDecimal.ROUND_HALF_UP)
                        .intValue(); // 单位分

                exportMerchantReceiptVo.setTaxAmount(CalculateUtil.converFenToYuan(taxPrice)); // 税额
            }

            // 进货单价
            exportMerchantReceiptVo.setSprice(CalculateUtil.converFenToYuan(orderDetailAmount / num)); // 进货单价
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


