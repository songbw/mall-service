package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.VendorsRpcService;
import com.fengchao.order.rpc.WorkOrderRpcService;
import com.fengchao.order.rpc.extmodel.*;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.EquityRpcService;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    private AdminOrderDao adminOrderDao;

    private EquityRpcService equityRpcService;

    private ProductRpcService productRpcService;

    private VendorsRpcService vendorsRpcService;

    private OrderDetailDao orderDetailDao;

    private OrdersDao ordersDao;

    private WorkOrderRpcService workOrderRpcService;

    @Autowired
    public AdminOrderServiceImpl(AdminOrderDao adminOrderDao,
                                 EquityRpcService equityRpcService,
                                 ProductRpcService productRpcService,
                                 VendorsRpcService vendorsRpcService,
                                 OrderDetailDao orderDetailDao,
                                 OrdersDao ordersDao,
                                 WorkOrderRpcService workOrderRpcService) {
        this.adminOrderDao = adminOrderDao;
        this.equityRpcService = equityRpcService;
        this.productRpcService = productRpcService;
        this.vendorsRpcService = vendorsRpcService;
        this.orderDetailDao = orderDetailDao;
        this.ordersDao = ordersDao;
        this.workOrderRpcService = workOrderRpcService;
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
        log.info("导出订单 查询数据库结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        // 4.获取组装结果的其他相关数据
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList);

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
        // 1. 通过工单系统获取"已退款"状态的订单(出账的订单)
        // 执行查询
        Date startDate = orderExportReqVo.getPayStartDate();
        Date endDate = orderExportReqVo.getPayEndDate();
        String _start = DateUtil.dateTimeFormat(startDate, DateUtil.DATE_YYYY_MM_DD);
        String _end = DateUtil.dateTimeFormat(endDate, DateUtil.DATE_YYYY_MM_DD);
        Date startDateTime = DateUtil.parseDateTime(_start + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDateTime = DateUtil.parseDateTime(_end + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsForReconciliation(orderExportReqVo.getMerchantId(), startDateTime, endDateTime);
        log.info("导出订单对账单(入账) 查询数据库入账的子订单 结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        if (CollectionUtils.isEmpty(orderDetailList)) {
            return Collections.emptyList();
        }

        // 2. 根据上一步，查询主订单
        // 获取主订单id列表
        List<Integer> ordersIdList = orderDetailList.stream().map(d -> d.getOrderId()).collect(Collectors.toList());
        // 查询主订单
        List<Orders> ordersList = ordersDao.selectOrdersListByIdList(ordersIdList);
        log.info("导出订单对账单(入账) 查询主订单 数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        // 3.组装结果
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList);

        // patch, 将状态统一为"已退款"
        if (CollectionUtils.isNotEmpty(exportOrdersVoList)) {
            exportOrdersVoList.stream().forEach(e -> e.setOrderDetailStatus("已完成"));
        }

        log.info("导出订单对账单(入账) 获取导出结果List<ExportOrdersVo>:{}", JSONUtil.toJsonString(exportOrdersVoList));

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
    public List<ExportOrdersVo> exportOrdersReconciliationOut(OrderExportReqVo orderExportReqVo) throws Exception {
        // 1. 获取"已退款"状态的子订单(入账的订单)
        // 执行查询
        Date startDate = orderExportReqVo.getPayStartDate();
        Date endDate = orderExportReqVo.getPayEndDate();
        String _start = DateUtil.dateTimeFormat(startDate, DateUtil.DATE_YYYY_MM_DD);
        String _end = DateUtil.dateTimeFormat(endDate, DateUtil.DATE_YYYY_MM_DD);
        Date startDateTime = DateUtil.parseDateTime(_start + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDateTime = DateUtil.parseDateTime(_end + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        // 1.1 获取已退款的子订单No集合
        List<String> orderDetailNoList = workOrderRpcService.queryRefundedOrderDetailIdList(orderExportReqVo.getMerchantId(), startDateTime, endDateTime);
        if (CollectionUtils.isEmpty(orderDetailNoList)) {
            return Collections.emptyList();
        }

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

        // 3.组装结果
        List<ExportOrdersVo> exportOrdersVoList = assembleExportOrderData(ordersList, orderDetailList);

        // patch, 将状态统一为"已退款"
        if (CollectionUtils.isNotEmpty(exportOrdersVoList)) {
            exportOrdersVoList.stream().forEach(e -> e.setOrderDetailStatus("已退款"));
        }

        log.info("导出订单对账单(出账) 获取导出结果List<ExportOrdersVo>:{}", JSONUtil.toJsonString(exportOrdersVoList));

        return exportOrdersVoList;
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


    //=============================== private =============================

    /**
     * 组装订单导出数据
     *
     * @param ordersList
     * @param orderDetailList
     * @return
     */
    private List<ExportOrdersVo> assembleExportOrderData(List<Orders> ordersList, List<OrderDetail> orderDetailList) {
        // x. 获取组装结果的其他相关数据 - 获取导出需要的coupon信息列表 - 获取coupon的id集合
        Set<Integer> couponUseInfoIdSet = new HashSet<>();
        for (Orders orders : ordersList) {
            if (orders.getCouponId() != null) {
                couponUseInfoIdSet.add(orders.getCouponId());
            }
        }

        // 2.1 将获取到的子订单转成map key：主订单id  value：List<OrderDtailBo>
        Map<Integer, List<OrderDetailBo>> orderDetailBoMap = new HashMap<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的promotion信息列表 - 获取promotion id集合
        Set<Integer> promotionIdSet = new HashSet<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的product信息列表 - 获取mpu id集合
        Set<String> mpuIdList = new HashSet<>();
        for (OrderDetail orderDetail : orderDetailList) {
            // 转bo
            OrderDetailBo orderDetailBo = convertToOrderDetailBo(orderDetail);
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
                    exportOrdersVo.setSubOrderId(orderDetailBo.getSubOrderId()); // 子订单编号
                    exportOrdersVo.setOrderDetailStatus(orderDetailBo.getOrderDetailStatus()); // 子订单状态
                    exportOrdersVo.setPaymentTime(ordersBo.getPaymentAt()); // 订单支付时间
                    exportOrdersVo.setCreateTime(ordersBo.getCreatedAt()); // 订单生成时间
                    exportOrdersVo.setAoyiId(ordersBo.getAoyiId()); // 苏宁订单号
                    exportOrdersVo.setPaymentNo(ordersBo.getPaymentNo());  // 支付订单号

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
                    }
                    exportOrdersVo.setCouponCode(ordersBo.getCouponCode()); // 券码
                    exportOrdersVo.setCouponId(ordersBo.getCouponId() == null ? null : ordersBo.getCouponId().longValue()); // 券码id
                    exportOrdersVo.setCouponSupplier(
                            couponUseInfoBeanMap.get(ordersBo.getCouponId()) == null ?
                                    "" : couponUseInfoBeanMap.get(ordersBo.getCouponId()).getSupplierMerchantName()); // 券来源（券商户）
                    Integer purchasePrice = null; // 进货价格
                    if (productInfoBean != null) {
                        String _sprice = productInfoBean.getSprice();
                        if (_sprice != null) {
                            BigDecimal bigDecimal = new BigDecimal(_sprice);
                            purchasePrice = bigDecimal.multiply(new BigDecimal(100)).intValue();
                        }
                    }
                    exportOrdersVo.setPurchasePrice(purchasePrice); // 进货价格 单位分
                    exportOrdersVo.setSkuPayPrice(orderDetailBo.getSalePrice() == null ?
                            0 : orderDetailBo.getSalePrice().multiply(new BigDecimal(100)).intValue()); // sku 实际支付价格 分
                    exportOrdersVo.setUnitPrice(orderDetailBo.getUnitPrice().multiply(new BigDecimal(100)).intValue()); // 商品单价-去除 活动 的价格
                    exportOrdersVo.setCouponPrice(ordersBo.getCouponDiscount() == null ?
                            0 : new BigDecimal(ordersBo.getCouponDiscount()).multiply(new BigDecimal(100)).intValue()); // 主订单 券支付金额
                    exportOrdersVo.setSkuCouponDiscount(orderDetailBo.getSkuCouponDiscount() == null ?
                            0 :orderDetailBo.getSkuCouponDiscount()); // 子订单 sku的优惠券支付金额 分

                    exportOrdersVo.setPayPrice(ordersBo.getSaleAmount() == null ?
                            0 : new BigDecimal(ordersBo.getSaleAmount()).multiply(new BigDecimal(100)).intValue()); // // 主订单实际支付的价格 单位:分 // (exportOrdersVo.getTotalRealPrice() - exportOrdersVo.getCouponPrice()); // orderDetailBo.getSalePrice().multiply(new BigDecimal(100)).intValue()
                    // exportOrdersVo.setShareBenefitPercent(); // 平台分润比!!!

                    exportOrdersVo.setBuyerName(ordersBo.getReceiverName()); // 收件人名
                    exportOrdersVo.setProvinceName(ordersBo.getProvinceName()); // 省
                    exportOrdersVo.setCityName(ordersBo.getCityName()); // 市
                    exportOrdersVo.setCountyName(ordersBo.getCountyName()); // 区
                    exportOrdersVo.setExpressFee(new BigDecimal(ordersBo.getServFee()).toString()); // 运费
                    exportOrdersVo.setAddress(ordersBo.getAddress() == null ? "" : ordersBo.getAddress()); // 详细地址
                    exportOrdersVo.setMobile(ordersBo.getMobile() == null ? "" : ordersBo.getMobile()); // 收件人电话
                    exportOrdersVo.setRemark(orderDetailBo.getRemark());

                    exportOrdersVoList.add(exportOrdersVo);
                } // 遍历子订单 end
            }

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
