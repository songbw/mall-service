package com.fengchao.order.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.constants.OrderConstants;
import com.fengchao.order.constants.PaymentStatusEnum;
import com.fengchao.order.dao.AdminOrderDao;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.db.annotation.DataSource;
import com.fengchao.order.db.config.DataSourceNames;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.feign.BaseService;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.mapper.*;
import com.fengchao.order.model.*;
import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
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
    private AdminOrderDao adminOrderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private BaseService baseService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Transactional
    @Override
    public OperaResult add2(OrderParamBean orderBean){
        OperaResult operaResult = new OperaResult();
        if (orderBean == null) {
            operaResult.setCode(400501);
            operaResult.setMsg("???????????????????????????");
            return operaResult;
        }
        orderBean.setTradeNo(new Date().getTime() + "");
        orderBean.setTradeNo(RandomUtil.randomString(orderBean.getTradeNo(), 8));
        if (orderBean.getReceiverId() == null || orderBean.getReceiverId() <= 0) {
            operaResult.setCode(400501);
            operaResult.setMsg("receiverId ???????????????");
            return operaResult;
        }
        Receiver receiver = receiverMapper.selectByPrimaryKey(orderBean.getReceiverId());
        if (receiver == null) {
            operaResult.setCode(400501);
            operaResult.setMsg("?????????????????????????????????????????????????????????");
            return operaResult;
        }
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

        // TODO ?????????????????????
        // TODO ???????????????????????????
        /**
         * TODO
         * ???????????????????????????????????????
         * ????????????????????????????????????????????????
         *
         * ???????????????????????????????????????
         */

        // TODO ??????
        // TODO ???????????? (????????????????????????????????????????????????????????????)

        Order bean = new Order();
        bean.setOpenId(orderBean.getOpenId());
        bean.setCompanyCustNo(orderBean.getCompanyCustNo());
        bean.setInvoiceTitle(orderBean.getInvoiceTitleName());
        bean.setTaxNo(orderBean.getInvoiceEnterpriseNumber());
        bean.setInvoiceContent(orderBean.getInvoiceTitleName());
        bean.setPayment(orderBean.getPayment());
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

        // ?????????
        OrderCouponBean coupon = orderBean.getCoupon();
        List<OrderCouponMerchantBean> orderCouponMerchants = null;
        if (coupon != null) {
            orderCouponMerchants = coupon.getMerchants();
            // ???????????????
            CouponUseInfoBean couponUseInfoBean = new CouponUseInfoBean();
            couponUseInfoBean.setUserCouponCode(coupon.getCode());
            couponUseInfoBean.setId(coupon.getId());
            OperaResult occupyResult = equityService.occupy(couponUseInfoBean);
            if (occupyResult.getCode() != 200) {
                // TODO ??????????????????????????????????????????
                logger.info("??????" + bean.getId() + "?????????????????????");
                operaResult.setCode(400601);
                operaResult.setMsg(occupyResult.getMsg());
                return operaResult;
            } else {
                bean.setCouponStatus(2);
            }
        }


        List<InventoryMpus> inventories = new ArrayList<>() ;
        // ???????????????
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants();
        // ????????????
        OperaResult promotionResult = promotionVerify(orderMerchantBeans) ;
        if (promotionResult.getCode() != 200) {
            return promotionResult ;
        }
        // ????????????????????????????????????
        OperaResult verifyLimitResult = verifyPerLimit(orderMerchantBeans, orderBean.getOpenId()) ;
        if (verifyLimitResult != null && verifyLimitResult.getCode() != 200) {
            return verifyLimitResult ;
        }
        logger.info("???????????? ??????List<OrderMerchantBean>:{}", JSONUtil.toJsonString(orderMerchantBeans));
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            bean.setTradeNo(orderMerchantBean.getTradeNo() + orderBean.getTradeNo());
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
            // ???????????????
            logger.info("???????????? ???????????????:{}", JSONUtil.toJsonString(bean));
            orderMapper.insert(bean);
            AtomicInteger i= new AtomicInteger(1);
            for (OrderDetailX orderSku : orderMerchantBean.getSkus()) {
                AoyiProdIndex prodIndexWithBLOBs = findProduct(orderSku.getMpu());

                // ???????????????????????????
                if ("0".equals(prodIndexWithBLOBs.getState())) {
                    operaResult.setCode(400502);
                    operaResult.setMsg("??????" + prodIndexWithBLOBs.getName() + "????????????");
                    // ?????????????????????
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return operaResult;
                }
                // ????????????????????????
                if (orderSku.getMerchantId() != 2) {
                    InventoryMpus inventoryMpus = new InventoryMpus();
                    inventoryMpus.setMpu(orderSku.getMpu());
                    inventoryMpus.setRemainNum(orderSku.getNum());
                    inventories.add(inventoryMpus) ;
                }

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setPromotionId(orderSku.getPromotionId());
                orderDetail.setSalePrice(orderSku.getSalePrice());
                orderDetail.setPromotionDiscount(orderSku.getPromotionDiscount());
                // orderDetail.setCreatedAt(date);
                // orderDetail.setUpdatedAt(date);
                orderDetail.setOrderId(bean.getId());
                orderDetail.setImage(prodIndexWithBLOBs.getImage());
                orderDetail.setModel(prodIndexWithBLOBs.getModel());
                orderDetail.setName(prodIndexWithBLOBs.getName());
                orderDetail.setStatus(0);
                orderDetail.setSkuId(orderSku.getSkuId());
                orderDetail.setMpu(orderSku.getMpu());
                orderDetail.setMerchantId(orderSku.getMerchantId());
                orderDetail.setSubOrderId(bean.getTradeNo() + String.format("%03d", i.getAndIncrement()));
                orderDetail.setUnitPrice(orderSku.getUnitPrice());
                orderDetail.setCheckedPrice(orderSku.getCheckedPrice());
                orderDetail.setNum(orderSku.getNum());
                orderDetail.setCategory(prodIndexWithBLOBs.getCategory());
                orderDetail.setSkuCouponDiscount((int) (orderSku.getSkuCouponDiscount() * 100)) ;
                // ???????????????
                logger.info("???????????? ???????????????:{}", JSONUtil.toJsonString(orderDetail));
                orderDetailDao.insert(orderDetail);
//                orderDetailXMapper.insert(orderDetailX) ;

                // ???????????????
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setOpenId(bean.getOpenId());
                shoppingCart.setMpu(orderSku.getMpu());
                shoppingCartMapper.deleteByOpenIdAndSku(shoppingCart);
            }
            // 30?????????????????????
            JobClientUtils.orderCancelTrigger(jobClient, bean.getId());
        }
        // ??????????????????
        OperaResult inventoryResult = productService.inventorySub(inventories) ;
        if (inventoryResult.getCode() != 200) {
            operaResult.setCode(inventoryResult.getCode());
            operaResult.setMsg(inventoryResult.getMsg());
            // ?????????????????????
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  operaResult;
        }

        // ??????????????????
        // ?????????aoyi???????????????
        List<OrderMerchantBean> orderMerchantBeanList = new ArrayList<>(); // aoyi???????????????
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            if (orderMerchantBean.getMerchantId() == OrderConstants.AOYI_MERCHANG_CODE) {
                List<OrderDetailX> aoyiOrderDetail = new ArrayList<>() ;
                orderMerchantBean.getSkus().forEach(orderDetailX -> {
                    orderDetailX.setUnitPrice(orderDetailX.getCheckedPrice());
                    aoyiOrderDetail.add(orderDetailX) ;
                });
                orderMerchantBean.setSkus(aoyiOrderDetail);
                orderMerchantBeanList.add(orderMerchantBean);
            }
        }
        // ????????????????????????????????????
        if (orderMerchantBeanList != null && orderMerchantBeanList.size() > 0) {
            orderBean.setMerchants(orderMerchantBeanList);
            OperaResponse<List<SubOrderT>>  result = new OperaResponse<List<SubOrderT>>();
            if ("1001".equals(orderBean.getCompanyCustNo()) || "1002".equals(orderBean.getCompanyCustNo())) { // ?????????
                result = aoyiClientService.orderGAT(orderBean);
            } else { //
                result = aoyiClientService.order(orderBean);
            }
            logger.info("???????????? ??????aoyi rpc ??????:{}", JSONUtil.toJsonString(result));

            if (result.getCode() == 200) {
                List<SubOrderT> subOrderTS = result.getData();
                subOrderTS.forEach(subOrderT -> {
                    if (!StringUtils.isEmpty(subOrderT.getAoyiId())){
                        // ??????aoyiId??????
                        adminOrderDao.updateAoyiIdByTradeNo(subOrderT.getAoyiId(), subOrderT.getOrderNo());
                    }
                });
                logger.info("???????????? OrderServiceImpl#add2 ??????orderMerchantBeans:{}", JSONUtil.toJsonString(orderMerchantBeans));
                operaResult.getData().put("result", orderMerchantBeans) ;

                logger.info("???????????? OrderServiceImpl#add2 ??????:{}", JSONUtil.toJsonString(operaResult));
            } else {
                if (coupon != null) {
                    boolean couponRelease = release(coupon.getId(), coupon.getCode());
                    if (!couponRelease) {
                        // ????????????,??????????????????
                        logger.info("??????" + bean.getId() + "?????????????????????");
                    }
                }
                // ????????????
                if (inventories != null && inventories.size() > 0) {
                    logger.info("????????????????????????{}", JSONUtil.toJsonString(inventories));
                    OperaResult inventoryAddResult = productService.inventoryAdd(inventories) ;
                    logger.info("????????????, ???????????????{}", JSONUtil.toJsonString(inventoryAddResult));
                }
                operaResult.setCode(result.getCode());
                operaResult.setMsg(result.getMsg());
                // ?????????????????????
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return  operaResult;
            }
        } else {
            logger.info("???????????? OrderServiceImpl#add2 ??????orderMerchantBeans:{}", JSONUtil.toJsonString(orderMerchantBeans));
            operaResult.getData().put("result", orderMerchantBeans) ;

            logger.info("???????????? OrderServiceImpl#add2 ??????:{}", JSONUtil.toJsonString(operaResult));
        }
        return operaResult;
    }

    private OperaResult promotionVerify(List<OrderMerchantBean> orderMerchantBeans) {
        List<PromotionVerifyBean> promotionVerifyBeans = new ArrayList<>() ;
        orderMerchantBeans.forEach(orderMerchantBean -> {
            List<OrderDetailX> orderDetailXES = orderMerchantBean.getSkus();
            orderDetailXES.forEach(orderDetailX -> {
                PromotionVerifyBean promotionVerifyBean = new PromotionVerifyBean() ;
                promotionVerifyBean.setId(orderDetailX.getPromotionId());
                promotionVerifyBean.setMpu(orderDetailX.getMpu());
                promotionVerifyBeans.add(promotionVerifyBean) ;
            });
        });
        logger.info("promotion verify ?????????{}", JSONUtil.toJsonString(promotionVerifyBeans));
        OperaResult result = equityService.promotionVerify(promotionVerifyBeans) ;
        logger.info("promotion verify ????????????{}", JSONUtil.toJsonString(result));
        if (result != null && result.getCode() == 200) {
            Map<String, Object> data = result.getData();
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionInfoBean>  promotionInfoBeans = JSONObject.parseArray(jsonString, PromotionInfoBean.class);
            for (PromotionInfoBean promotionInfoBean : promotionInfoBeans) {
                if (promotionInfoBean.getStatus() != null && promotionInfoBean.getStatus() != 4) {
                    result.setCode(400602);
                    result.setMsg("???????????????????????????????????????");
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * ????????????????????????
     * @param orderMerchantBeans
     * @return
     */
    private OperaResult verifyPerLimit(List<OrderMerchantBean> orderMerchantBeans, String openId) {
        List<String> errorMpus = new ArrayList<>() ;
        List<String> mpus = new ArrayList<>() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            for (OrderDetailX orderSku : orderMerchantBean.getSkus()) {
                mpus.add(orderSku.getMpu()) ;
            }
        }
        OperaResult result = equityService.findPromotionByMpuList(mpus);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionMpuX> subOrderTS = JSONObject.parseArray(jsonString, PromotionMpuX.class);
            if (subOrderTS != null && subOrderTS.size() > 0) {
                for (PromotionMpuX promotionMpuX : subOrderTS) {
                    if (promotionMpuX.getPerLimited() == -1) {
                        continue;
                    } else {
                        List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByOpenIdAndMpuAndPromotionId(openId, promotionMpuX.getMpu(), promotionMpuX.getId()) ;
                        if (orderDetailList != null && orderDetailList.size() > 0) {
                            AtomicInteger sum = new AtomicInteger(0);
                            orderDetailList.forEach(orderDetail -> {
                                sum.set(sum.get() + orderDetail.getNum());
                            });
                            promotionMpuX.setPerLimited(promotionMpuX.getPerLimited() - sum.get());
                            for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
                                for (OrderDetailX orderSku : orderMerchantBean.getSkus()) {
                                    if (orderSku.getNum() > promotionMpuX.getPerLimited()) {
                                        errorMpus.add(orderSku.getMpu());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (errorMpus != null && errorMpus.size() > 0) {
                result.setCode(4000001);
                result.setMsg("??????????????????????????????????????????");
                result.getData().put("mpus", errorMpus) ;
                return result;
            }
        }
        return null;
    }


    @Override
    public Integer cancel(Integer id) {
        Orders order = adminOrderDao.selectById(id);
        if (order != null) {
            // ?????????????????????
            OrderDetail orderDetail = new OrderDetail() ;
            orderDetail.setOrderId(id);
            orderDetail.setStatus(4);
            adminOrderDao.updateOrderDetailStatus(orderDetail) ;
            // ???????????????????????????????????????
            order.setId(id);
            order.setUpdatedAt(new Date());
            order.setStatus(3);
            if (order.getCouponCode() != null && order.getCouponStatus() == 2) {
                order.setCouponStatus(1);
            }
            // ????????????
            adminOrderDao.updateStatusAndCouponStatusById(order) ;
            if (order.getCouponCode() != null) {
                List<Orders> ordersList = adminOrderDao.selectByCouponIdAndCouponCode(order.getCouponId(),order.getCouponCode(), 2) ;
                if (ordersList == null || ordersList.size() == 0) {
                    // TODO ???????????????
                    release(order.getCouponId(), order.getCouponCode()) ;
                }
            }
            // ??????????????????????????????????????????MPU??????
            List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByOrdersId(id) ;
            List<InventoryMpus> inventoryMpuses = new ArrayList<>() ;
            orderDetailList.forEach(orderDetail1 -> {
                InventoryMpus inventoryMpus = new InventoryMpus() ;
                inventoryMpus.setMpu(orderDetail1.getMpu());
                inventoryMpus.setRemainNum(orderDetail1.getNum());
                inventoryMpuses.add(inventoryMpus) ;
            });
            logger.info("???????????????????????????????????????????????????{}", JSONUtil.toJsonString(inventoryMpuses));
            OperaResult result = productService.inventoryAdd(inventoryMpuses) ;
            if (result.getCode() != 200) {
                logger.info("?????????????????????????????????????????? ???????????????{}", JSONUtil.toJsonString(result));
            }
        }
        return id;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public Order findById(Integer id) {
        Order order = orderMapper.selectByPrimaryKey(id) ;
        if (order!= null) {
            List<OrderDetailX> orderDetailXES = orderDetailXMapper.selectByOrderId(id) ;
            order.setSkus(orderDetailXES);
        }
        return order;
    }

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

    @Override
    public Integer updateStatus(Order bean) {
        logger.info("?????????????????? ??????:{}", JSONUtil.toJsonString(bean));

        bean.setUpdatedAt(new Date());
        // ?????????????????????
        if (bean.getStatus() == 2) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(bean.getId());
            orderDetail.setStatus(3);

            // adminOrderDao.updateOrderDetailStatus(orderDetail);

            adminOrderDao.updateOrderDetailStatusForReceived(orderDetail);
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
        map.put("aoyiId",orderBean.getAoyiId());
        if(orderBean.getPayDateStart() != null && !orderBean.getPayDateStart().equals("")){
            map.put("payDateStart", orderBean.getPayDateStart() + " 00:00:00");
        }
        if(orderBean.getPayDateEnd() != null && !orderBean.getPayDateEnd().equals("")){
            map.put("payDateEnd", orderBean.getPayDateEnd() + " 23:59:59");
        }
        if(orderBean.getCompleteDateStart() != null && !orderBean.getCompleteDateStart().equals("")){
            map.put("completeDateStart", orderBean.getCompleteDateStart() + " 00:00:00");
        }
        if(orderBean.getCompleteDateEnd() != null && !orderBean.getCompleteDateEnd().equals("")){
            map.put("completeDateEnd", orderBean.getCompleteDateEnd() + " 23:59:59");
        }

        logger.info("???????????? ?????????????????????:{}", JSONUtil.toJsonString(map));

        List<OrderDetailBean> orderBeans = new ArrayList<>();
        total = orderMapper.selectCount(map);
        if(total >  0){
            orderBeans = orderMapper.selectOrderLimit(map);
            orderBeans.forEach(order -> {
                if(order.getImage() == null || "".equals(order.getImage())){
                    AoyiProdIndex productIndex = findProduct(order.getSkuId());
                    if (productIndex != null) {
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
                }
            });
        }
        pageBean = PageBean.build(pageBean, orderBeans, total, orderBean.getPageIndex(), orderBean.getPageSize());
        return pageBean;
    }

    @Override
    public Integer updateRemark(Order bean) {
        return orderMapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public Integer updateOrderAddress(Order bean) {
        return orderMapper.updateByPrimaryKeySelective(bean);
    }

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
    public OperaResult getLogist(String merchantNo, String orderId) {
        OperaResult result = new OperaResult();
        JSONArray jsonArray = new JSONArray();
        List<Orders> orders = ordersDao.selectOrdersByTradeNo(orderId);
        if (orders == null || orders.size() == 0) {
            result.setCode(4000001);
            result.setMsg("??????????????????");
            return result ;
        }
        List<OrderDetailX> logistics = orderDetailXMapper.selectBySubOrderId(orderId + "%");
        if (logistics != null && logistics.size() > 0) {
            for (OrderDetailX logist : logistics) {
                if (logist != null && logist.getLogisticsId() != null && logist.getComCode() != null) {
                    OperaResponse<JSONObject> response =  baseService.kuaidi100(logist.getLogisticsId(), logist.getComCode()) ;
                    if (response.getCode() == 200) {
                        JSONObject jsonObject = response.getData();
                        jsonArray.add(jsonObject);
                    }
                }
            }
        }
        logger.info("????????????????????? {}", JSONUtils.toJSONString(jsonArray));
        result.getData().put("result", jsonArray) ;
        return result;
    }

    @Override
    public List<Order> findTradeNo(String appId, String merchantNo,String tradeNo) {
        logger.info("findTradeNo ???????????? appId : " + appId + " merchantNo : " + merchantNo + " tradeNo : " + tradeNo);
        List<Order> orders = new ArrayList<>();
        if (StringUtils.isEmpty(merchantNo)) {
            orders = orderMapper.selectByTradeNo(appId + "%" + tradeNo);
        } else {
            orders = orderMapper.selectByTradeNo(appId + "%" + merchantNo + "%" + tradeNo);
        }
        logger.info("findTradeNo ???????????????orders : {}", JSONUtil.toJsonString(orders));
        return orders ;
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
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setOrderId(order.getId());
//        orderDetail.setStatus(1);
//        adminOrderDao.updateOrderDetailStatus(orderDetail);

        return orderMapper.updatePaymentNo(order);
    }

    @Override
    public Integer updatePaymentByOutTradeNoAndPaymentNo(Order order) {
        // ???????????????
        if (order.getCouponId() != null && order.getCouponId() > 0 && order.getCouponCode() != null && (!"".equals(order.getCouponCode()))) {
            consume(order.getCouponId(), order.getCouponCode()) ;
            order.setCouponStatus(3);
        }
        int id = orderMapper.updatePaymentByOutTradeNoAndPaymentNo(order);
        // TODO ??????????????????
        virtualHandle(order);
        return id ;
    }

    @Override
    public Integer batchUpdateOrderDetailStatus(List<Integer> orderIdList, Integer status) {
        logger.info("???????????????id???????????????????????????????????? ??????????????? orderIdList:{}, status:{}",
                JSONUtil.toJsonString(orderIdList), status);

        int count = orderDetailDao.batchUpdateStatusByOrderIdList(orderIdList, status);

        logger.info("???????????????id???????????????????????????????????? ???????????????:{}", count);

        return count;
    }

    @Override
    public DayStatisticsBean findOverviewStatistics() throws Exception {
        // 1.????????????????????????; 2.(?????????)????????????; 3.(?????????)????????????
        Float dayPaymentCount = orderMapper.selectPayedOrdersAmount(); // ???????????????????????? SUM(sale_amount)
        int dayCount = orderMapper.selectPayedOrdersCount(); // (?????????)???????????? count(id) FROM orders
        int dayPeopleCount = orderMapper.selectPayedOdersUserCount(); // (?????????)???????????? count(DISTINCT(open_id))

        DayStatisticsBean dayStatisticsBean = new DayStatisticsBean();
        dayStatisticsBean.setOrderPaymentAmount(dayPaymentCount);
        dayStatisticsBean.setOrderCount(dayCount);
        dayStatisticsBean.setOrderPeopleNum(dayPeopleCount);

        logger.info("???????????????????????????????????????????????? DayStatisticsBean:{}", JSONUtil.toJsonString(dayStatisticsBean));
        return dayStatisticsBean;
    }

    @Deprecated
    public DayStatisticsBean findMerchantOverallStatisticsDeprecated(Integer merchantId) throws Exception {
        // 1. ????????????????????? ??????a.????????????????????????; b.(?????????)????????????; c.(?????????)????????????
        BigDecimal orderAmount = new BigDecimal(0); // ????????????????????????
        Integer orderCount = 0; // (?????????)????????????
        Integer openIdCount = 0; // (?????????)????????????

        // 2. ????????????????????????????????????????????????????????????
        int currentPageNo = 1;
        int pageSize = 300;

        // 2.1 ???????????????
        logger.info("?????????????????????????????????????????????????????? ??????????????????????????? ??????????????? merchantId:{}, currentPageNo:{}, pageSize:{}",
                merchantId, currentPageNo, pageSize);
        PageInfo<OrderDetail> pageInfo = orderDetailDao.selectOrderDetailsByMerchantIdPageable(merchantId, currentPageNo, pageSize);
        logger.info("?????????????????????????????????????????????????????? ??????????????????????????? ???????????????pageInfo:{}", JSONUtil.toJsonString(pageInfo));

        // ?????????
        Integer totalPage = pageInfo.getPages();

        Set<String> openIdSet = new HashSet<>(); // ?????????id ??????
        // 2.2 ??????????????????
        while (currentPageNo <= totalPage) {
            if (currentPageNo > 1) {
                logger.info("?????????????????????????????????????????????????????? ?????????????????? ??????????????? merchantId:{}, currentPageNo:{}, pageSize:{}",
                        merchantId, currentPageNo, pageSize);
                pageInfo = orderDetailDao.selectOrderDetailsByMerchantIdPageable(merchantId, currentPageNo, pageSize);
                logger.info("?????????????????????????????????????????????????????? ?????????????????? ???????????????pageInfo:{}", JSONUtil.toJsonString(pageInfo));
            }

            // ??????????????????????????????
            List<OrderDetail> orderDetailList = pageInfo.getList();
            // ???????????????id??????
            Set<Integer> orderIdList = orderDetailList.stream().map(o -> o.getOrderId()).collect(Collectors.toSet());

            // ???????????????id??????????????????????????????????????????
            List<Orders> payedOrdersList = ordersDao.selectPayedOrdersListByOrdersId(new ArrayList<>(orderIdList));
            // ?????????????????????id??????
            Set<Integer> payedOrdersIdSet = payedOrdersList.stream().map(o -> o.getId()).collect(Collectors.toSet());
            // ??????????????????????????????id???
            for (Orders orders : payedOrdersList) {
                openIdSet.add(orders.getOpenId());
            }

            // ???????????????????????????????????????
            for (OrderDetail orderDetail : orderDetailList) {
                if (payedOrdersIdSet.contains(orderDetail.getOrderId())) { // ???????????????????????????
                    orderAmount = orderAmount.add(orderDetail.getSalePrice()); // ?????????????????????
                    orderCount = orderCount + 1; // ???????????????
                }
            }

            currentPageNo++;
        }

        // 3. ????????????
        DayStatisticsBean dayStatisticsBean = new DayStatisticsBean();
        dayStatisticsBean.setOrderPaymentAmount(orderAmount.floatValue());
        dayStatisticsBean.setOrderCount(orderCount);
        dayStatisticsBean.setOrderPeopleNum(openIdSet.size()); // ????????????

        logger.info("?????????????????????????????????????????????????????? DayStatisticsBean:{}", JSONUtil.toJsonString(dayStatisticsBean));

        return dayStatisticsBean;
    }

    @Override
    public DayStatisticsBean findMerchantOverallStatistics(Integer merchantId) throws Exception {
        // 1. ????????????????????? ??????a.????????????????????????; b.(?????????)????????????; c.(?????????)????????????
        BigDecimal orderAmount = new BigDecimal(0); // ????????????????????????
        Integer orderCount = 0; // (?????????)????????????
        Set<String> openIdSet = new HashSet<>(); // ?????????id ??????

        // 2. ??????????????????????????????????????????????????????(??????????????????)
        int currentPageNo = 1;
        int pageSize = 300;

        // 2.1 ???????????????
        logger.info("?????????????????????????????????????????????????????? ?????????????????????????????? ??????????????? merchantId:{}, currentPageNo:{}, pageSize:{}",
                merchantId, currentPageNo, pageSize);
        PageInfo<Orders> pageInfo = ordersDao.selectPayedOrderListByMerchantIdPageable(merchantId, currentPageNo, pageSize);
        logger.info("?????????????????????????????????????????????????????? ?????????????????????????????? ??????????????? pageInfo:{}", JSONUtil.toJsonString(pageInfo));

        // ?????????
        Integer totalPage = pageInfo.getPages(); // FIXME : ????????????????????????????


        // 2.2 ??????????????????
        while (currentPageNo <= totalPage) {
            if (currentPageNo > 1) {
                logger.info("?????????????????????????????????????????????????????? ????????????????????? ??????????????? merchantId:{}, currentPageNo:{}, pageSize:{}",
                        merchantId, currentPageNo, pageSize);
                pageInfo = ordersDao.selectPayedOrderListByMerchantIdPageable(merchantId, currentPageNo, pageSize);
                logger.info("?????????????????????????????????????????????????????? ????????????????????? ???????????????pageInfo:{}", JSONUtil.toJsonString(pageInfo));
            }

            // ????????????????????????????????? ?????????
            List<Orders> ordersList = pageInfo.getList();
            for (Orders orders : ordersList) {
                orderAmount = orderAmount.add(new BigDecimal(orders.getSaleAmount() - orders.getServFee()));
                orderCount = orderCount + 1; // ????????????
                openIdSet.add(orders.getOpenId()); //
            }

            currentPageNo++;
        }

        // 3. ????????????
        DayStatisticsBean dayStatisticsBean = new DayStatisticsBean();
        dayStatisticsBean.setOrderPaymentAmount(orderAmount.floatValue());
        dayStatisticsBean.setOrderCount(orderCount);
        dayStatisticsBean.setOrderPeopleNum(openIdSet.size()); // ????????????

        logger.info("?????????????????????????????????????????????????????? DayStatisticsBean:{}", JSONUtil.toJsonString(dayStatisticsBean));

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
        // 1. ???????????????????????????????????????????????????
        logger.info("??????????????????????????????????????????????????? ????????????????????????????????? ??????????????? startDateTime:{}, endDateTime:{}", startDateTime, endDateTime);
        Date startTime = DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endTime = DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        List<Orders> ordersList = ordersDao.selectPayedOrdersListByPaymentTime(startTime, endTime);
        logger.info("??????????????????????????????????????????????????? ????????????????????????????????? ???????????????List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        // ???map key : ordersId, value: Orders
        Map<Integer, Orders> ordersMap = ordersList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));

        // 2. ?????????????????????????????????
        // 2.1 ???????????????????????????????????????id??????
        List<Integer> ordersIdList =
                ordersList.stream().map(orders -> orders.getId()).collect(Collectors.toList());
        logger.info("??????????????????????????????????????????????????? ????????????????????? ???????????????:{}", JSONUtil.toJsonString(ordersIdList));
        // 2.2 ?????????????????????
        if (CollectionUtils.isEmpty(ordersIdList)) {
            return Collections.emptyList();
        }
        List<OrderDetail> payedOrderDetailList = orderDetailDao.selectOrderDetailsByOrdersIdList(ordersIdList);
        logger.info("??????????????????????????????????????????????????? ????????????????????? ???????????????List<OrderDetail>:{}", JSONUtil.toJsonString(payedOrderDetailList));

        // 3. ???dto
        List<OrderDetailBean> orderDetailBeanList = new ArrayList<>();
        for (OrderDetail orderDetail : payedOrderDetailList) {
            OrderDetailBean orderDetailBean = convertToOrderDetailBean(orderDetail);
            orderDetailBean.setPayStatus(PaymentStatusEnum.PAY_SUCCESS.getValue());

            // ??????????????????
            Orders orders = ordersMap.get(orderDetail.getOrderId());
            // a.??????????????????
            orderDetailBean.setCityId(orders == null ? "" : orders.getCityId());
            orderDetailBean.setCityName(orders == null ? "" : orders.getCityName());
            // b.??????????????????
            orderDetailBean.setPaymentAt(orders.getPaymentAt());

            orderDetailBeanList.add(orderDetailBean);
        }

        logger.info("??????????????????????????????????????????????????? ???List<OrderDetailBean>:{}",
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

    @Override
    public List<Orders> findOrderListByOpenId(String openId) {
        List<Orders> orders = ordersDao.selectOrderListByOpenId(openId);
        return orders;
    }

    @Override
    public Integer updateSubOrderStatus(OrderDetail bean) {
        orderDetailDao.updateOrderDetailStatus(bean) ;
        return bean.getId();
    }

    @Override
    public Integer subOrderCancel(OrderDetail bean) {
        bean.setStatus(5);
        orderDetailDao.updateOrderDetailStatus(bean) ;
        return bean.getId();
    }

    @Override
    public OperaResponse logistics(List<Logisticsbean> logisticsbeans) {
        OperaResponse<List<Logisticsbean>> response = new OperaResponse<List<Logisticsbean>>();
        List<Logisticsbean> logisticsbeanList = new ArrayList<>();
        for (Logisticsbean logistics: logisticsbeans) {
            if (StringUtils.isEmpty(logistics.getLogisticsId())) {
                logisticsbeanList.add(logistics);
                continue;
            }
            if (StringUtils.isEmpty(logistics.getLogisticsContent())) {
                logisticsbeanList.add(logistics);
                continue;
            }
            if (StringUtils.isEmpty(logistics.getSubOrderId())) {
                logisticsbeanList.add(logistics);
                continue;
            }
            orderDetailDao.updateBySubOrderId(logistics) ;
            OrderDetail orderDetail = orderDetailDao.selectBySubOrderId(logistics.getSubOrderId()) ;
            JobClientUtils.subOrderFinishTrigger(jobClient, orderDetail.getId());
        }
        if (logisticsbeanList != null && logisticsbeanList.size() > 0) {
            response.setCode(4000002);
            response.setMsg("???????????????");
            response.setData(logisticsbeanList);
            return response;
        }
        return response;
    }

    @Override
    public OrderDetail findDetailById(int id) {
        return orderDetailMapper.selectByPrimaryKey(id) ;
    }

    @Override
    public Integer finishOrderDetail(Integer id) {
        OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(id) ;
        orderDetail.setStatus(3);
        orderDetailDao.updateOrderDetailStatus(orderDetail) ;
        return id;
    }

    // ========================================= private ======================================

    private AoyiProdIndex findProduct(String skuId) {
        OperaResult result = productService.find(skuId);
        logger.info("??????MPU???" + skuId + " ????????????????????????????????????{}", JSONUtil.toJsonString(result));
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
        orderDetailBean.setNum(orderDetail.getNum());
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
        // orderDetailBean.setPayee(); // ???????????????????????????????????????
        // orderDetailBean.setRefundFee(); // ??????????????????????????????
        // orderDetailBean.setPayType(); // ????????????
        // orderDetailBean.setPaymentTotalFee(); // ???????????????
        // orderDetailBean.setPayer(); // C?????????????????? ??????????????????
        // orderDetailBean.setPayStatus(); // ???????????? 10??????????????????  1??????????????????????????????  2????????????3???????????????  4????????????  5????????????  11??????????????????????????????   12???????????????????????????  14???????????????15???????????????
        // orderDetailBean.setPayOrderCategory(); // 1?????????2?????????3?????????4??????
        // orderDetailBean.setCouponId();
        // orderDetailBean.setCouponCode();
        // orderDetailBean.setCouponDiscount();
        orderDetailBean.setPromotionId(orderDetail.getPromotionId());
        // orderDetailBean.setPromotionDiscount();
        orderDetailBean.setSaleAmount(orderDetail.getSalePrice() == null ? 0 : orderDetail.getSalePrice().floatValue()); // ??????????????????
        orderDetailBean.setCategory(orderDetail.getCategory()); // ??????

        return orderDetailBean;
    }

    /**
     * ????????????????????????
     * @param order
     */
    private void virtualHandle(Order order) {
        // TODO ???????????????????????????????????????????????????????????????????????????????????????????????????
        List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByOrdersId(order.getId()) ;
        logger.info("????????????ID???" + order.getId() + " ???????????????????????????????????????{}", JSONUtil.toJsonString(orderDetailList));
        orderDetailList.forEach(orderDetail -> {
            AoyiProdIndex prodIndex = findProduct(orderDetail.getMpu()) ;
            if (prodIndex != null && prodIndex.getType() == 1) {
                VirtualTicketsBean virtualTicketsBean = new VirtualTicketsBean() ;
                virtualTicketsBean.setOpenId(order.getOpenId());
                virtualTicketsBean.setOrderId(orderDetail.getId());
                virtualTicketsBean.setMpu(orderDetail.getMpu());
                virtualTicketsBean.setNum(orderDetail.getNum());
                OperaResult operaResult = equityService.createVirtual(virtualTicketsBean) ;
                if (operaResult.getCode() != 200) {
                    // TODO ???????????????????????????????????????
                    logger.info("????????????????????????????????????????????????{}", JSONUtil.toJsonString(virtualTicketsBean));
                    return;
                }
                // ???????????????
                finishOrderDetail(orderDetail.getId()) ;
            }
        });
    }


}
