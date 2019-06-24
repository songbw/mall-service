package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.feign.EquityService;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.mapper.*;
import com.fengchao.order.model.*;
import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.CosUtil;
import com.fengchao.order.utils.Kuaidi100;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

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
    private EquityService equityService;


    @Override
    public List<SubOrderT> add2(OrderParamBean orderBean){
        Order bean = new Order();
        bean.setOpenId(orderBean.getOpenId());
        bean.setCompanyCustNo(orderBean.getCompanyCustNo());
        orderBean.setTradeNo(new Date().getTime() + "");
        Receiver receiver = receiverMapper.selectByPrimaryKey(orderBean.getReceiverId());
        if (receiver != null) {
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
        }
        orderBean.setInvoiceState("1");
        orderBean.setInvoiceType("4");

        bean.setInvoiceState("1");
        bean.setInvoiceType("4");
        bean.setInvoiceTitle(orderBean.getInvoiceTitleName());
        bean.setTaxNo(orderBean.getInvoiceEnterpriseNumber());
        bean.setInvoiceContent(orderBean.getInvoiceTitleName());
        bean.setPayment(orderBean.getPayment());
        bean.setStatus(0);
        bean.setType(1);
        Date date = new Date() ;
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        List<SubOrderT> subOrders = null;
        subOrders = createOrder(orderBean) ;
        OrderCouponBean coupon = orderBean.getCoupon();
        List<OrderCouponMerchantBean> orderCouponMerchants = null;
        if (coupon != null) {
            orderCouponMerchants = coupon.getMerchants();
        }
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            for (SubOrderT subOrder : subOrders) {
                if (subOrder.getOrderNo().contains(orderMerchantBean.getTradeNo())) {
                    if (orderCouponMerchants != null && orderCouponMerchants.size() > 0) {
                        for (OrderCouponMerchantBean orderCouponMerchant : orderCouponMerchants) {
                            if (orderMerchantBean.getMerchantNo().equals(orderCouponMerchant.getMerchantNo())) {
                                bean.setCouponId(coupon.getId());
                                bean.setCouponCode(coupon.getCode());
                            }
                        }
                    }
                    subOrder.setMerchantNo(orderMerchantBean.getMerchantNo());
                    bean.setMerchantNo(orderMerchantBean.getMerchantNo());
                    bean.setTradeNo(subOrder.getOrderNo());
                    bean.setServFee(orderMerchantBean.getServFee());
                    bean.setAmount(orderMerchantBean.getAmount());
                    bean.setSaleAmount(orderMerchantBean.getSaleAmount());
                    bean.setSkus(orderMerchantBean.getSkus());
                    // 添加主订单
                    mapper.insert(bean);
                    // 核销优惠券
                    boolean couponConsume = consume(coupon.getId(), coupon.getCode()) ;
                    if (!couponConsume) {
                        logger.info("订单" + bean.getId() + "优惠券核销失败");
                    }
                    subOrder.getAoyiSkus().forEach(sku -> {
                        AoyiProdIndex prodIndexWithBLOBs = findProduct(sku.getSkuId());
                        OrderDetail orderDetail = new OrderDetail();
                        orderMerchantBean.getSkus().forEach(orderSku -> {
                            if (sku.getSkuId().equals(orderSku.getSkuId())) {
                                orderDetail.setPromotionId(orderSku.getPromotionId());
                                orderDetail.setSalePrice(orderSku.getSalePrice());
                            }
                        });
                        orderDetail.setCreatedAt(date);
                        orderDetail.setUpdatedAt(date);
                        orderDetail.setOrderId(bean.getId());
                        orderDetail.setImage(prodIndexWithBLOBs.getImage());
                        orderDetail.setModel(prodIndexWithBLOBs.getModel());
                        orderDetail.setName(prodIndexWithBLOBs.getName());
                        orderDetail.setStatus(0);
                        orderDetail.setSkuId(sku.getSkuId());
                        orderDetail.setSubOrderId(sku.getSubOrderNo());
                        orderDetail.setUnitPrice(new BigDecimal(sku.getUnitPrice()));
                        orderDetail.setNum(Integer.parseInt(sku.getNum()));

                        // 添加子订单
                        orderDetailMapper.insert(orderDetail) ;
                        // 删除购物车
                        ShoppingCart shoppingCart = new ShoppingCart();
                        shoppingCart.setOpenId(bean.getOpenId());
                        shoppingCart.setSkuId(sku.getSkuId());
                        shoppingCartMapper.deleteByOpenIdAndSkuId(shoppingCart);
                    });
                }
            }
        }
        return subOrders;
    }

    @Override
    public Integer cancel(Integer id) {
        Order order = new Order();
        order.setId(id);
        order.setUpdatedAt(new Date());
        order.setStatus(3);
        mapper.updateStatusById(order) ;
        return id;
    }

    @Override
    public Order findById(Integer id) {
        Order order = mapper.selectByPrimaryKey(id) ;
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id) ;
        order.setSkus(orderDetails);
        return order;
    }

    @Override
    public Integer delete(Integer id) {
        Order order = new Order();
        order.setId(id);
        order.setUpdatedAt(new Date());
        order.setStatus(-1);
        mapper.updateStatusById(order) ;
        return id;
    }

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
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            orders = mapper.selectLimit(map);
            orders.forEach(order -> {
                order.setSkus(orderDetailMapper.selectByOrderId(order.getId()));
            });
        }
        pageBean = PageBean.build(pageBean, orders, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public Integer updateStatus(Order bean) {
        bean.setUpdatedAt(new Date());
        mapper.updateStatusById(bean) ;
        return bean.getId();
    }

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
        if(orderBean.getPayDateStart() != null && !orderBean.getPayDateStart().equals("")){
            map.put("payDateStart", orderBean.getPayDateStart()) ;
        }
        if(orderBean.getPayDateEnd() != null && !orderBean.getPayDateEnd().equals("")){
            map.put("payDateEnd", orderBean.getPayDateEnd()) ;
        }
        List<OrderDetailBean> orderBeans = new ArrayList<>();
        total = mapper.selectCount(map);
        if(total >  0){
            orderBeans = mapper.selectOrderLimit(map);
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

    @Override
    public Integer updateRemark(Order bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public Integer updateOrderAddress(Order bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public PageBean searchDetail(OrderQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("orderId", queryBean.getOrderId());
        Order order = mapper.selectByPrimaryKey(queryBean.getOrderId());
        total = orderDetailMapper.selectCount(map);
        if (total > 0) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectLimit(map);
                orderDetails.forEach(orderDetail -> {
                    orderDetail.setAoyiProdIndex(findProduct(orderDetail.getSkuId()));
                });
            order.setSkus(orderDetails);
        }
        pageBean = PageBean.build(pageBean, order, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
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
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(Logistics.getOrderId());
                orderDetail.setSubOrderId(Logistics.getSubOrderId());
                orderDetail.setLogisticsContent(Logistics.getLogisticsContent());
                orderDetail.setLogisticsId(Logistics.getLogisticsId());
                orderDetailMapper.updateByOrderId(orderDetail);
            });
        }
        return i;
    }

    @Override
    public JSONArray getLogist(String merchantNo, String orderId) {
        JSONArray jsonArray = new JSONArray();
        List<OrderDetail> logistics = orderDetailMapper.selectBySubOrderId(orderId + "%");
        if (logistics != null && logistics.size() > 0) {
            logistics.forEach(logist -> {
                String jsonString = Kuaidi100.synQueryData(logist.getLogisticsId(), logist.getComCode()) ;
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                jsonArray.add(jsonObject);
            });
        }
        return jsonArray;
    }

    @Override
    public List<Order> findTradeNo(String tradeNo) {
        return mapper.selectByTradeNo(tradeNo);
    }

    @Override
    public List<Order> findOutTradeNo(String outTradeNo) {
        return mapper.selectByOutTradeNo(outTradeNo);
    }

    @Override
    public List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) {
        Order order = new Order();
        order.setOutTradeNo(outTradeNo);
        order.setPaymentNo(paymentNo);
        return mapper.selectByOutTradeNoAndPaymentNo(order);
    }

    @Override
    public Integer updatePaymentNo(Order order) {
        return mapper.updatePaymentNo(order);
    }

    @Override
    public Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) {
        return mapper.updatePaymentByOutTradeNoAndPaymentNo(order);
    }

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
        OperaResult result = aoyiClientService.order(bean);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<SubOrderT> subOrderTS = JSONObject.parseArray(jsonString, SubOrderT.class);
            return subOrderTS;
        }
        return null;
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
}
