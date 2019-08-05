package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.constants.PaymentStatusEnum;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.db.annotation.DataSource;
import com.fengchao.order.db.config.DataSourceNames;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.mapper.*;
import com.fengchao.order.model.*;
import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailXMapper orderDetailXMapper;

    @Autowired
    private ReceiverMapper receiverMapper;

    @Autowired
    private InvoiceInfoMapper invoiceInfoMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private AoyiClientService aoyiClientService;

    @Autowired
    private AoyiBaseFulladdressMapper addressMapper;

    @Autowired
    private EquityServiceClient equityService;

    @Autowired
    private JobClient jobClient;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private AdminOrderDao adminOrderDao;


    @Override
    public OperaResult add2(OrderParamBean orderBean){
        OperaResult operaResult = new OperaResult();
        Order bean = new Order();
        bean.setOpenId(orderBean.getOpenId());
        bean.setCompanyCustNo(orderBean.getCompanyCustNo());
        bean.setInvoiceTitle(orderBean.getInvoiceTitleName());
        bean.setTaxNo(orderBean.getInvoiceEnterpriseNumber());
        bean.setInvoiceContent(orderBean.getInvoiceTitleName());
        bean.setPayment(orderBean.getPayment());
        orderBean.setTradeNo(new Date().getTime() + "");
        Receiver receiver = receiverMapper.selectByPrimaryKey(orderBean.getReceiverId());
        if (receiver == null) {
            operaResult.setCode(400501);
            operaResult.setMsg("收货地址为不存在，请查证后再提交订单！");
            return operaResult;
        }

        // TODO 验证数据有效性
        // TODO 验证业务逻辑有效性
        // TODO 存库
        // TODO 通知奥义 (如果成功则正常返回，如果失败则回滚数据库)

        receiver = handleBean(receiver);
        orderBean.setReceiverName(receiver.getReceiverName());
        orderBean.setTelephone(receiver.getTelephone());
        orderBean.setMobile(receiver.getMobile());
        orderBean.setEmail(receiver.getEmail());
        orderBean.setProvinceId(receiver.getProvinceId());
        orderBean.setCityId(receiver.getCityId());
        orderBean.setCountyId(receiver.getCountyId());
        orderBean.setTownId(receiver.getTownId());
        orderBean.setAddress(receiver.getAddress());
        orderBean.setZip(receiver.getZip());
        orderBean.setInvoiceState("1");
        orderBean.setInvoiceType("4");

        bean.setReceiverName(receiver.getReceiverName());
        bean.setTelephone(receiver.getTelephone());
        bean.setMobile(receiver.getMobile());
        bean.setEmail(receiver.getEmail());
        bean.setProvinceId(receiver.getProvinceId());
        bean.setCityId(receiver.getCityId());
        bean.setCountyId(receiver.getCountyId());
        bean.setTownId(receiver.getTownId());
        bean.setAddress(receiver.getAddress());
        bean.setZip(receiver.getZip());
        bean.setProvinceName(receiver.getProvinceName());
        bean.setCityName(receiver.getCityName());
        bean.setCountyName(receiver.getCountyName());

        bean.setInvoiceState("1");
        bean.setInvoiceType("4");
        bean.setStatus(0);
        bean.setType(1);
        Date date = new Date() ;
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);

        // 优惠券
        OrderCouponBean coupon = orderBean.getCoupon();
        List<OrderCouponMerchantBean> orderCouponMerchants = null;
        if (coupon != null) {
            orderCouponMerchants = coupon.getMerchants();
            // 预占优惠券
            boolean couponConsume = occupy(coupon.getId(), coupon.getCode()) ;
            if (!couponConsume) {
                // TODO 优惠券预占失败的话，订单失败
                logger.info("订单" + bean.getId() + "优惠券核销失败");
            } else {
                bean.setCouponStatus(2);
            }
        }
        // 多商户信息
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            bean.setTradeNo(orderMerchantBean.getTradeNo() + RandomUtil.randomString(orderBean.getTradeNo(), 8));
            orderMerchantBean.setTradeNo(bean.getTradeNo());
            bean.setMerchantNo(orderMerchantBean.getMerchantNo());
            bean.setMerchantId(orderMerchantBean.getMerchantId());
            bean.setServFee(orderMerchantBean.getServFee());
            bean.setAmount(orderMerchantBean.getAmount());
            bean.setSaleAmount(orderMerchantBean.getSaleAmount());
            bean.setSkus(orderMerchantBean.getSkus());
            if (orderCouponMerchants != null && orderCouponMerchants.size() > 0) {
                for (OrderCouponMerchantBean orderCouponMerchant : orderCouponMerchants) {
                    if (orderMerchantBean.getMerchantNo().equals(orderCouponMerchant.getMerchantNo())) {
                        bean.setCouponId(coupon.getId());
                        bean.setCouponCode(coupon.getCode());
                        bean.setCouponDiscount(coupon.getDiscount());
                    }
                }
            }
            // 添加主订单
            orderMapper.insert(bean);

            AtomicInteger i= new AtomicInteger(1);
            orderMerchantBean.getSkus().forEach(orderSku -> {
                AoyiProdIndex prodIndexWithBLOBs = findProduct(orderSku.getMpu());
                OrderDetailX orderDetailX = new OrderDetailX();
                orderDetailX.setPromotionId(orderSku.getPromotionId());
                orderDetailX.setSalePrice(orderSku.getSalePrice());
                orderDetailX.setPromotionDiscount(orderSku.getPromotionDiscount());
                orderDetailX.setCreatedAt(date);
                orderDetailX.setUpdatedAt(date);
                orderDetailX.setOrderId(bean.getId());
                orderDetailX.setImage(prodIndexWithBLOBs.getImage());
                orderDetailX.setModel(prodIndexWithBLOBs.getModel());
                orderDetailX.setName(prodIndexWithBLOBs.getName());
                orderDetailX.setStatus(0);
                orderDetailX.setSkuId(orderSku.getSkuId());
                orderDetailX.setMpu(orderSku.getMpu());
                orderDetailX.setMerchantId(orderSku.getMerchantId());
                orderDetailX.setSubOrderId(bean.getTradeNo() + String.format("%03d", i.getAndIncrement()));
                orderDetailX.setUnitPrice(orderSku.getUnitPrice());
                orderDetailX.setNum(orderSku.getNum());
                orderDetailX.setCategory(prodIndexWithBLOBs.getCategory());
                orderDetailX.setSkuCouponDiscount(orderSku.getSkuCouponDiscount() * 100) ;

                // 添加子订单
                orderDetailXMapper.insert(orderDetailX) ;
                // 删除购物车
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setOpenId(bean.getOpenId());
                shoppingCart.setMpu(orderSku.getSkuId());
                shoppingCartMapper.deleteByOpenIdAndSku(shoppingCart);
            });
            // 30分钟后取消订单
            JobClientUtils.orderCancelTrigger(jobClient, bean.getId());
        }
        // 传数据给奥义
        orderBean.setMerchants(orderMerchantBeans);
        orderBean.getMerchants().removeIf(merchant -> (merchant.getMerchantId() != 2));
//        createOrder(orderBean) ;
        OperaResponse<List<SubOrderT>>  result = aoyiClientService.order(orderBean);
        if (result.getCode() == 200) {
            operaResult.getData().put("result", orderMerchantBeans) ;
        } else {
            operaResult.setCode(result.getCode());
            operaResult.setMsg(result.getMsg());
            // 异常数据库回滚
        }
        return operaResult;
    }


    @CachePut(value = "orders", key = "#id")
    @Override
    public Integer cancel(Integer id) {
        Orders order = adminOrderDao.selectById(id);
        if (order != null) {
            // 更新子订单状态
            OrderDetail orderDetail = new OrderDetail() ;
            orderDetail.setOrderId(id);
            orderDetail.setStatus(4);
            adminOrderDao.updateOrderDetailStatus(orderDetail) ;
            // 修改主订单状态和优惠券状态
            order.setId(id);
            order.setUpdatedAt(new Date());
            order.setStatus(3);
            if (order.getCouponCode() != null && order.getCouponStatus() == 2) {
                order.setCouponStatus(1);
            }
            // 取消订单
            adminOrderDao.updateStatusAndCouponStatusById(order) ;
            if (order.getCouponCode() != null) {
                List<Orders> ordersList = adminOrderDao.selectByCouponIdAndCouponCode(order.getCouponId(),order.getCouponCode(), 2) ;
                if (ordersList == null || ordersList.size() == 0) {
                    // TODO 释放优惠券
                    release(order.getCouponId(), order.getCouponCode()) ;
                }
            }
        }
        return id;
    }

    @Cacheable(value = "orders", key = "#id")
    @DataSource(DataSourceNames.TWO)
    @Override
    public Order findById(Integer id) {
        Order order = orderMapper.selectByPrimaryKey(id) ;
        List<OrderDetailX> orderDetailXES = orderDetailXMapper.selectByOrderId(id) ;
        order.setSkus(orderDetailXES);
        return order;
    }

    @CacheEvict(value = "orders", key = "#id")
    @Override
    public Integer delete(Integer id) {
        Order order = new Order();
        order.setId(id);
        order.setUpdatedAt(new Date());
        order.setStatus(-1);
        orderMapper.updateStatusById(order) ;
        return id;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findList(OrderQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("openId", queryBean.getOpenId()) ;
        if (queryBean.getStatus() != null && queryBean.getStatus() != -1) {
            map.put("status", queryBean.getStatus()) ;
        }
        List<Order> orders = new ArrayList<>();
        total = orderMapper.selectLimitCount(map);
        if (total > 0) {
            orders = orderMapper.selectLimit(map);
            orders.forEach(order -> {
                order.setSkus(orderDetailXMapper.selectByOrderId(order.getId()));
            });
        }
        pageBean = PageBean.build(pageBean, orders, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @CachePut(value = "orders", key = "#bean.id")
    @Override
    public Integer updateStatus(Order bean) {
        bean.setUpdatedAt(new Date());
        // 更新子订单状态
        if (bean.getStatus() == 2) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(bean.getId());
            orderDetail.setStatus(3);
            adminOrderDao.updateOrderDetailStatus(orderDetail);
        }
        orderMapper.updateStatusById(bean) ;
        return bean.getId();
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean searchOrderList(OrderBean orderBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(orderBean.getPageIndex(), orderBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", orderBean.getPageSize());
        map.put("openId", orderBean.getOpenId());
        map.put("subOrderId", orderBean.getSubOrderId());
        map.put("id", orderBean.getId());
        map.put("mobile", orderBean.getMobile()) ;
        map.put("tradeNo",orderBean.getTradeNo());
        map.put("status",orderBean.getStatus());
        map.put("merchantId",orderBean.getMerchantId());
        map.put("subStatus",orderBean.getSubStatus());
        if(orderBean.getPayDateStart() != null && !orderBean.getPayDateStart().equals("")){
            map.put("payDateStart", orderBean.getPayDateStart()) ;
        }
        if(orderBean.getPayDateEnd() != null && !orderBean.getPayDateEnd().equals("")){
            map.put("payDateEnd", orderBean.getPayDateEnd()) ;
        }
        List<OrderDetailBean> orderBeans = new ArrayList<>();
        total = orderMapper.selectCount(map);
        if(total >  0){
            orderBeans = orderMapper.selectOrderLimit(map);
            orderBeans.forEach(order -> {
                if(order.getImage() == null || "".equals(order.getImage())){
                    AoyiProdIndex productIndex = findProduct(order.getSkuId());
                    String imagesUrl = productIndex.getImagesUrl();
                    if (imagesUrl != null && (!"".equals(imagesUrl))) {
                        String image = "";
                        if (imagesUrl.indexOf("/") == 0) {
                            image = CosUtil.iWalletUrlT + imagesUrl.split(":")[0];
                        } else {
                            image = CosUtil.baseAoyiProdUrl + imagesUrl.split(":")[0];
                        }
                        order.setImage(image);
                    }
                }
            });
        }
        pageBean = PageBean.build(pageBean, orderBeans, total, orderBean.getPageIndex(), orderBean.getPageSize());
        return pageBean;
    }

    @CachePut(value = "orders", key = "#bean.id")
    @Override
    public Integer updateRemark(Order bean) {
        return orderMapper.updateByPrimaryKeySelective(bean);
    }

    @CachePut(value = "orders", key = "#bean.id")
    @Override
    public Integer updateOrderAddress(Order bean) {
        return orderMapper.updateByPrimaryKeySelective(bean);
    }

    @Cacheable(value = "orders")
    @DataSource(DataSourceNames.TWO)
    @Override
    public Order searchDetail(OrderQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("orderId", queryBean.getOrderId());
        Order order = orderMapper.selectByPrimaryKey(queryBean.getOrderId());
        total = orderDetailXMapper.selectCount(map);
        if (total > 0) {
            List<OrderDetailX> orderDetailXES = orderDetailXMapper.selectLimit(map);
            pageBean = PageBean.build(pageBean, orderDetailXES, total, queryBean.getPageNo(), queryBean.getPageSize());
            order.setSkusPage(pageBean);
        }
        return order;
    }

    private Receiver handleBean(Receiver receiver) {
        AoyiBaseFulladdress data = new AoyiBaseFulladdress();
        data.setId(receiver.getProvinceId());
        data.setLevel("1");
        AoyiBaseFulladdress province = addressMapper.selectById(data);
        if (province != null) {
            receiver.setProvinceName(province.getName());
        }
        data.setId(receiver.getCityId());
        data.setLevel("2");
        AoyiBaseFulladdress city = addressMapper.selectById(data);
        if (city != null) {
            receiver.setCityName(city.getName());
        }
        data.setId(receiver.getCountyId());
        data.setLevel("3");
        data.setPid(receiver.getCityId());
        AoyiBaseFulladdress county = addressMapper.selectById(data);
        if (county != null) {
            receiver.setCountyName(county.getName());
        }
        return receiver;
    }

    @Override
    public Integer uploadLogistics(Logisticsbean bean) {
        final int i = 1;
        if (bean.getTotal() > 0) {
            List<Logisticsbean> logisticsList = bean.getLogisticsList();
            logisticsList.forEach(Logistics -> {
                OrderDetailX orderDetailX = new OrderDetailX();
                orderDetailX.setOrderId(Logistics.getOrderId());
                orderDetailX.setSubOrderId(Logistics.getSubOrderId());
                orderDetailX.setLogisticsContent(Logistics.getLogisticsContent());
                orderDetailX.setComCode(Logistics.getComCode());
                orderDetailX.setLogisticsId(Logistics.getLogisticsId());
                orderDetailX.setStatus(2);
                orderDetailXMapper.updateByOrderId(orderDetailX);
            });
        }
        return i;
    }

    @Override
    public JSONArray getLogist(String merchantNo, String orderId) {
        JSONArray jsonArray = new JSONArray();
        List<OrderDetailX> logistics = orderDetailXMapper.selectBySubOrderId(orderId + "%");
        if (logistics != null && logistics.size() > 0) {
            logistics.forEach(logist -> {
                if (logist.getLogisticsId() != null && logist.getComCode() != null) {
                    String jsonString = Kuaidi100.synQueryData(logist.getLogisticsId(), logist.getComCode()) ;
                    JSONObject jsonObject = JSONObject.parseObject(jsonString);
                    jsonArray.add(jsonObject);
                }
            });
        }
        return jsonArray;
    }

    @Override
    public List<Order> findTradeNo(String appId, String merchantNo,String tradeNo) {
        return orderMapper.selectByTradeNo(appId + "%" + merchantNo + "%" + tradeNo);
    }

    @Override
    public List<Order> findOutTradeNo(String outTradeNo) {
        return orderMapper.selectByOutTradeNo(outTradeNo);
    }

    @Override
    public List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) {
        Order order = new Order();
        order.setOutTradeNo(outTradeNo);
        order.setPaymentNo(paymentNo);
        return orderMapper.selectByOutTradeNoAndPaymentNo(order);
    }

    @Override
    public Integer updatePaymentNo(Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        orderDetail.setStatus(1);
        adminOrderDao.updateOrderDetailStatus(orderDetail);
        return orderMapper.updatePaymentNo(order);
    }

    @Override
    public Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) {
        // 核销优惠券
        if (order.getCouponId() != null && order.getCouponId() > 0 && order.getCouponCode() != null && (!"".equals(order.getCouponCode()))) {
            consume(order.getCouponId(), order.getCouponCode()) ;
            order.setCouponStatus(3);
        }
        return orderMapper.updatePaymentByOutTradeNoAndPaymentNo(order);
    }

    @Override
    public DayStatisticsBean findOverviewStatistics() throws Exception {
        // 1.获取订单支付总额; 2.(已支付)订单总量; 3.(已支付)下单人数
        int dayPaymentCount = orderMapper.selectPayedOrdersAmount(); // 获取订单支付总额 SUM(sale_amount)
        int dayCount = orderMapper.selectPayedOrdersCount(); // (已支付)订单总量 count(id) FROM orders
        int dayPeopleCount = orderMapper.selectPayedOdersUserCount(); // (已支付)下单人数 count(DISTINCT(open_id))

        DayStatisticsBean dayStatisticsBean = new DayStatisticsBean();
        dayStatisticsBean.setOrderPaymentAmount(dayPaymentCount);
        dayStatisticsBean.setOrderCount(dayCount);
        dayStatisticsBean.setOrderPeopleNum(dayPeopleCount);

        logger.info("获取平台的关于订单的总体统计数据 DayStatisticsBean:{}", JSONUtil.toJsonString(dayStatisticsBean));
        return dayStatisticsBean;
    }

    public String queryLogisticsInfo(String logisticsId) {
        String comcode = orderDetailXMapper.selectComCode(logisticsId);
        if(comcode == null || comcode.equals("")){
            ArrayList<String> strings = Kuaidi100.queryAutoComNumByKuadi100(logisticsId);
            comcode = strings.get(0);
        }
        return Kuaidi100.synQueryData(logisticsId, comcode);
    }

    @Override
    public List<PromotionPaymentBean> findDayPromotionPaymentCount(String dayStart, String dayEnd) {
        HashMap map = new HashMap();
        map.put("dayStart", dayStart);
        map.put("dayEnd", dayEnd);
        return orderMapper.selectDayPromotionPaymentCount(map);
    }

    @Override
    public List<MerchantPaymentBean> findDayMerchantPaymentCount(String dayStart, String dayEnd) {
        HashMap map = new HashMap();
        map.put("dayStart", dayStart);
        map.put("dayEnd", dayEnd);
        return orderMapper.selectDayMerchantPaymentCount(map);
    }

    @Override
    public List<OrderDetailBean> queryPayedOrderDetail(String startDateTime, String endDateTime) throws Exception {
        // 1. 按照时间范围查询子订单集合
        logger.info("按照时间范围查询已支付的子订单列表 数据库入参 startDateTime:{}, endDateTime:{}", startDateTime, endDateTime);
        Date startTime = DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endTime = DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        List<OrderDetail> orderDetailList = adminOrderDao.selectOrderDetailsByCreateTimeRange(startTime, endTime);
        logger.info("按照时间范围查询已支付的子订单列表 数据库返回List<OrderDetail>:{}", JSONUtil.toJsonString(orderDetailList));

        // 2. 过滤出已支付的子订单集合
        // 2.1 根据获取到的子订单获取主订单
        List<Integer> ordersIdList =
                orderDetailList.stream().map(detail -> detail.getOrderId()).collect(Collectors.toList());
        logger.info("按照时间范围查询已支付的子订单列表 查询主订单列表 数据库入参:{}", JSONUtil.toJsonString(ordersIdList));
                // 2.2 查询主订单
        List<Orders> ordersList = adminOrderDao.selectOrdersListByIdList(ordersIdList);
        logger.info("按照时间范围查询已支付的子订单列表 查询主订单列表 数据库返回List<Orders>:{}", JSONUtil.toJsonString(ordersList));
        // 转map key : ordersId, value: Orders
        Map<Integer, Orders> ordersMap = ordersList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));

        // 2.3 过滤
        List<OrderDetail> payedOrderDetailList = new ArrayList<>(); // 已支付的子订单集合
        for (OrderDetail orderDetail : orderDetailList) { // 遍历子订单
            Integer orderId = orderDetail.getOrderId();
            Orders orders = ordersMap.get(orderId);
            if (orders != null && orders.getPayStatus() != null) {
                // 支付状态 10初始创建订单  1下单成功，等待支付。  2支付中，3超时未支付  4支付失败  5支付成功  11支付成功，记账也成功   12支付成功，记账失败  14退款失败，15订单已退款
                if (PaymentStatusEnum.PAY_SUCCESS.getValue() == orders.getPayStatus().intValue()) { // 如果 支付成功
                    payedOrderDetailList.add(orderDetail);
                }
            }
        }
        logger.info("按照时间范围查询已支付的子订单列表 获取的已支付子订单集合List<OrderDetail>:{}",
                JSONUtil.toJsonString(payedOrderDetailList));

        // 3. 转dto
        List<OrderDetailBean> orderDetailBeanList = new ArrayList<>();
        for (OrderDetail orderDetail : payedOrderDetailList) {
            OrderDetailBean orderDetailBean = convertToOrderDetailBean(orderDetail);
            orderDetailBean.setPayStatus(PaymentStatusEnum.PAY_SUCCESS.getValue());

            // 设置相关信息
            Orders orders = ordersMap.get(orderDetail.getOrderId());
            // a.设置城市信息
            orderDetailBean.setCityId(orders == null ? "" : orders.getCityId());
            orderDetailBean.setCityName(orders == null ? "" : orders.getCityName());
            // b.设置支付时间
            orderDetailBean.setPaymentAt(orders.getPaymentAt());

            orderDetailBeanList.add(orderDetailBean);
        }

        logger.info("按照时间范围查询已支付的子订单列表 转List<OrderDetailBean>:{}",
                JSONUtil.toJsonString(orderDetailBeanList));

        return orderDetailBeanList;
    }

//    @Override
//    @Deprecated
//    public Integer findDayPaymentCount(String dayStart, String dayEnd) {
//        HashMap map = new HashMap();
//        map.put("dayStart", dayStart);
//        map.put("dayEnd", dayEnd);
//        return orderMapper.selectDayPaymentCount(map);
//    }

    @Override
    public String findPaymentStatus(String outerTradeNo) {
        List<Orders> ordersList = adminOrderDao.selectPaymentStatusByPaymentNo(outerTradeNo) ;
        if (ordersList != null && ordersList.size() > 0) {
            return "success" ;
        }
        return "fail";
    }

    @Override
    public List<Orders> findByPaymentNoAndOpenId(String paymentNo, String openId) {
        List<Orders> ordersList = adminOrderDao.selectByPaymentNoAndOpenId(paymentNo, openId) ;
        return ordersList;
    }

    @Override
    public Integer updateSubOrder(OrderDetail bean) {
        return adminOrderDao.updateOrderDetail(bean);
    }

    // ========================================= private ======================================

    private AoyiProdIndex findProduct(String skuId) {
        OperaResult result = productService.find(skuId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            AoyiProdIndex aoyiProdIndex = JSONObject.parseObject(jsonString, AoyiProdIndex.class) ;
            return aoyiProdIndex;
        }
        return null;
    }

    private List<SubOrderT> createOrder(OrderParamBean bean) {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info(msg);
        OperaResponse result = aoyiClientService.order(bean);
        if (result.getCode() == 200) {
//            Map<String, Object> data = result.getData() ;
//            Object object = data.get("result");
//            String jsonString = JSON.toJSONString(object);
//            List<SubOrderT> subOrderTS = JSONObject.parseArray(jsonString, SubOrderT.class);
            List<SubOrderT> subOrderTS = (List<SubOrderT>) result.getData();
            return subOrderTS;
        }
        return null;
    }

    private boolean occupy(int id, String code) {
        CouponUseInfoBean bean = new CouponUseInfoBean();
        bean.setUserCouponCode(code);
        bean.setId(id);
        OperaResult result = equityService.occupy(bean);
        if (result.getCode() == 200) {
            return true;
        }
        return false;
    }

    private boolean consume(int id, String code) {
        CouponUseInfoBean bean = new CouponUseInfoBean();
        bean.setUserCouponCode(code);
        bean.setId(id);
        OperaResult result = equityService.consume(bean);
        if (result.getCode() == 200) {
            return true;
        }
        return false;
    }

    private boolean release(int id, String code) {
        CouponUseInfoBean bean = new CouponUseInfoBean();
        bean.setUserCouponCode(code);
        bean.setId(id);
        OperaResult result = equityService.release(bean);
        if (result.getCode() == 200) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param orderDetail
     * @return
     */
    private OrderDetailBean convertToOrderDetailBean(OrderDetail orderDetail) {
        OrderDetailBean orderDetailBean = new OrderDetailBean();

        orderDetailBean.setId(orderDetail.getId());
        // orderDetailBean.setOpenId(orderDetail.get);
        orderDetailBean.setSubOrderId(orderDetail.getSubOrderId());
        // orderDetailBean.setTradeNo();
        // orderDetailBean.setAoyiId();
        // orderDetailBean.setCompanyCustNo();
        orderDetailBean.setMerchantId(orderDetail.getMerchantId());
        // orderDetailBean.setMerchantNo(orderDetail.getmer);
        // orderDetailBean.setReceiverName(orderDetai);
        // orderDetailBean.setTelephone();
        // orderDetailBean.setMobile();
        // orderDetailBean.setEmail();
        // orderDetailBean.setProvinceId(orderDetail.);
        // orderDetailBean.setProvinceName();
        // orderDetailBean.setCityId();
        // orderDetailBean.setCityName();
        // orderDetailBean.setCountyId();
        // orderDetailBean.setCountyName();
        // orderDetailBean.setTownId();
        // orderDetailBean.setAddress();
        // orderDetailBean.setZip();
        // orderDetailBean.setRemark();
        // orderDetailBean.setInvoiceState();
        // orderDetailBean.setInvoiceType();
        // orderDetailBean.setInvoiceTitle();
        // orderDetailBean.setInvoiceContent();
        // orderDetailBean.setPayment();
        // orderDetailBean.setServFee();
        // orderDetailBean.setAmount();
        // orderDetailBean.setStatus();
        // orderDetailBean.setType();
        // orderDetailBean.setCreatedAt();
        // orderDetailBean.setUpdatedAt();
        // orderDetailBean.setSkuId();
        // orderDetailBean.setNum();
        // orderDetailBean.setUnitPrice();
        // orderDetailBean.setImage();
        // orderDetailBean.setModel();
        // orderDetailBean.setName();
        // orderDetailBean.setPaymentNo();
        // orderDetailBean.setPaymentAt();
        // orderDetailBean.setLogisticsId();
        // orderDetailBean.setLogisticsContent();
        // orderDetailBean.setOutTradeNo();
        // orderDetailBean.setPaymentAmount();
        // orderDetailBean.setPayee(); // 商户号，充值钱包的时候没有
        // orderDetailBean.setRefundFee(); // 退款金额，退款时候有
        // orderDetailBean.setPayType(); // 支付方式
        // orderDetailBean.setPaymentTotalFee(); // 订单总金额
        // orderDetailBean.setPayer(); // C端个人账号。 表示唯一用户
        // orderDetailBean.setPayStatus(); // 支付状态 10初始创建订单  1下单成功，等待支付。  2支付中，3超时未支付  4支付失败  5支付成功  11支付成功，记账也成功   12支付成功，记账失败  14退款失败，15订单已退款
        // orderDetailBean.setPayOrderCategory(); // 1支付，2充值，3退款，4提现
        // orderDetailBean.setCouponId();
        // orderDetailBean.setCouponCode();
        // orderDetailBean.setCouponDiscount();
        orderDetailBean.setPromotionId(orderDetail.getPromotionId());
        // orderDetailBean.setPromotionDiscount();
        orderDetailBean.setSaleAmount(orderDetail.getSalePrice() == null ? 0 : orderDetail.getSalePrice().floatValue()); // 实际支付价格
        orderDetailBean.setCategory(orderDetail.getCategory()); // 品类

        return orderDetailBean;
    }


}
