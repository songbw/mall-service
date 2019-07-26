package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.rpc.ProductRpcService;
import com.fengchao.order.rpc.extmodel.CouponBean;
import com.fengchao.order.rpc.extmodel.CouponUseInfoBean;
import com.fengchao.order.rpc.extmodel.ProductInfoBean;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.Orders;
import com.fengchao.order.rpc.EquityRpcService;
import com.fengchao.order.service.AdminOrderService;
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

    @Autowired
    public AdminOrderServiceImpl(AdminOrderDao adminOrderDao,
                                 EquityRpcService equityRpcService,
                                 ProductRpcService productRpcService) {
        this.adminOrderDao = adminOrderDao;
        this.equityRpcService = equityRpcService;
        this.productRpcService = productRpcService;
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
        List<Orders> ordersList = adminOrderDao.selectExportOrders(queryConditions,
                orderExportReqVo.getPayStartDate(), orderExportReqVo.getPayEndDate());
        log.info("导出订单 查询数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        if (CollectionUtils.isEmpty(ordersList)) {
            return Collections.emptyList();
        }

        // 2. 根据上一步，查询子订单(注意条件：子订单号、商家id)
        // 获取主订单id列表
        List<Integer> ordersIdList = new ArrayList<>();
        // x. 获取组装结果的其他相关数据 - 获取导出需要的coupon信息列表 - 获取coupon的id集合
        Set<Integer> couponUseInfoIdSet = new HashSet<>();
        for (Orders orders : ordersList) {
            ordersIdList.add(orders.getId());
            if (orders.getCouponId() != null) {
                couponUseInfoIdSet.add(orders.getCouponId());
            }
        }

        List<OrderDetail> orderDetailList =
                adminOrderDao.selectExportOrderDetail(ordersIdList, orderExportReqVo.getSubOrderId(), orderExportReqVo.getMerchantId());
        log.info("导出订单 查询数据库结果List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

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
        log.info("导出订单 根据id获取活动 List<PromotionBean>:{}", JSONUtil.toJsonString(promotionBeanList));
        // 转map key:promotionId, value:PromotionBean
        Map<Integer, PromotionBean> promotionBeanMap =
                promotionBeanList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        // 4.2 获取导出需要的coupon信息列表
        List<CouponUseInfoBean> couponUseInfoBeanList = equityRpcService.queryCouponUseInfoByIdList(new ArrayList<>(couponUseInfoIdSet));
        log.info("导出订单 根据id获取coupon List<CouponBean>:{}", JSONUtil.toJsonString(couponUseInfoBeanList));
        // 转map， key couponUseInfoId, value: CouponUseInfoBean
        Map<Integer, CouponUseInfoBean> couponUseInfoBeanMap =
                couponUseInfoBeanList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        // 4.3 获取导出需要的product信息列表
        List<ProductInfoBean> productInfoBeanList =
                productRpcService.findProductListByMpuIdList(new ArrayList<>(mpuIdList));
        log.info("导出订单 根据mpu获取product List<ProductInfoBean>:{}", JSONUtil.toJsonString(productInfoBeanList));
        // 转map， key:mpu  value:ProductInfoBean
        Map<String, ProductInfoBean> productInfoBeanMap =
                productInfoBeanList.stream().collect(Collectors.toMap(p -> p.getMpu(), p -> p));

        // x. ordersBoList 组装 List<ExportOrdersVo>
        List<ExportOrdersVo> exportOrdersVoList = new ArrayList<>();
        for (OrdersBo ordersBo : ordersBoList) { // 遍历主订单
            List<OrderDetailBo> orderDetailBoList = ordersBo.getOrderDetailBoList(); // 获取该订单的子订单
            if (CollectionUtils.isNotEmpty(orderDetailBoList)) { // 目前不存在子订单的这种情况是不存在的!!!!
                for (OrderDetailBo orderDetailBo : orderDetailBoList) { // 遍历子订单
                    ExportOrdersVo exportOrdersVo = new ExportOrdersVo();

                    exportOrdersVo.setOpenId(ordersBo.getOpenId()); // 用户id
                    exportOrdersVo.setTradeNo(ordersBo.getTradeNo()); // 主订单编号
                    exportOrdersVo.setSubOrderId(orderDetailBo.getSubOrderId()); // 子订单编号
                    exportOrdersVo.setPaymentTime(ordersBo.getPaymentAt()); // 订单支付时间
                    exportOrdersVo.setCreateTime(ordersBo.getCreatedAt()); // 订单生成时间

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
                    exportOrdersVo.setPromotionId(orderDetailBo.getPromotionId().longValue()); // 活动id
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
                    exportOrdersVo.setTotalRealPrice(
                            orderDetailBo.getUnitPrice().multiply(new BigDecimal(100)).intValue()
                                    * orderDetailBo.getNum()); // sku 的总价
                    exportOrdersVo.setUnitPrice(orderDetailBo.getUnitPrice().multiply(new BigDecimal(100)).intValue()); // 商品单价-去除 活动 的价格
                    exportOrdersVo.setCouponPrice(orderDetailBo.getSkuCouponDiscount()); // 券支付金额
                    exportOrdersVo.setPayPrice(exportOrdersVo.getTotalRealPrice() - exportOrdersVo.getCouponPrice()); // 实际支付的价格
                    // exportOrdersVo.setShareBenefitPercent(); // 平台分润比!!!
                    exportOrdersVo.setBuyerName(ordersBo.getReceiverName()); // 收件人名
                    exportOrdersVo.setProvinceName(ordersBo.getProvinceName()); // 省
                    exportOrdersVo.setCityName(ordersBo.getCityName()); // 市
                    exportOrdersVo.setCountyName(ordersBo.getCountyName()); // 区

                    exportOrdersVoList.add(exportOrdersVo);
                } // 遍历子订单 end
            }

        } // 遍历主订单 end

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
            exportOrdersVo.setTotalRealPrice(i);
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
     *
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
     *
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

        return orderDetailBo;
    }

    /**
     *
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
