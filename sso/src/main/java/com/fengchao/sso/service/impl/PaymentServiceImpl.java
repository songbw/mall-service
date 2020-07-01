package com.fengchao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.config.SSOConfiguration;
import com.fengchao.sso.feign.AoyiClientService;
import com.fengchao.sso.feign.GuanaitongClientService;
import com.fengchao.sso.feign.OrderServiceClient;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.rpc.OrderRpcService;
import com.fengchao.sso.service.IPaymentService;
import com.fengchao.sso.util.JSONUtil;
import com.fengchao.sso.util.OperaResult;
import com.fengchao.sso.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@EnableConfigurationProperties({SSOConfiguration.class})
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PinganClientService pinganClientService;
    @Autowired
    private OrderServiceClient orderService;
    @Autowired
    private AoyiClientService aoyiClientService;
    @Autowired
    private GuanaitongClientService guanaitongClientService;
    @Autowired
    private SSOConfiguration ssoConfiguration;
    @Autowired
    private OrderRpcService orderRpcService;

    @Override
    public OperaResult payment(PaymentBean paymentBean) {
        OperaResult result = new OperaResult();
        // 查询订单是否为待支付状态
        List<Order> orderList = findTradeNo(paymentBean.getiAppId(), paymentBean.getMerchantNo(),paymentBean.getOpenId() + paymentBean.getOrderNos());
        orderList.forEach(order -> {
            if (order.getPayStatus() == 5) {
                result.setCode(9000001);
                result.setMsg("存在已支付订单，请重新刷新订单列表");
            }
        });
        if (result.getCode() != 200) {
            return result ;
        }
        log.info("findTradeNo 返回 orderList: {}", JSONUtil.toJsonString(orderList));
        PaymentResult result1 = getPayment(paymentBean);
        log.info("getPayment 返回 result1: {}", JSONUtil.toJsonString(result1));
        if ("200".equals(result1.getReturnCode())) {
            result.getData().put("result", result1.getData());
            orderList.forEach(order -> {
                order.setPaymentNo(result1.getData().getOrderNo());
                order.setOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
                order.setUpdatedAt(new Date());
                if (order.getPayStatus() != 5) {
                    updatePaymentNo(order);
                }
            });
        } else if ("订单号重复".equals(result1.getMsg())) {
            List<Order> orders = findOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
            if (orders.size() > 0) {
                OrderNo orderNo = new OrderNo();
                orderNo.setOrderNo(orders.get(0).getPaymentNo());
                result.getData().put("result", orderNo);
            } else {
                result.setCode(-1);
                result.setMsg(result1.getMsg());
            }
        }else {
            result.setCode(-1);
            result.setMsg(result1.getMsg());
        }
        return result ;
    }

    @Override
    public OperaResult gPayment(PaymentBean paymentBean) {
        SSOConfigBean configBean =  getSSOConfig(paymentBean.getiAppId()) ;
        OperaResult result = new OperaResult();
        // 查询订单是否为待支付状态
        List<Order> orderList = findTradeNo(paymentBean.getiAppId(), paymentBean.getMerchantNo(),paymentBean.getOpenId() + paymentBean.getOrderNos());
        orderList.forEach(order -> {
            if (order.getPayStatus() == 5) {
                result.setCode(9000001);
                result.setMsg("存在已支付订单，请重新刷新订单列表");
            }
        });
        if (result.getCode() != 200) {
            return result ;
        }
        // 关爱通支付
        GuanaitongPaymentBean guanaitongPaymentBean = new GuanaitongPaymentBean();
        String outer_trade_no = paymentBean.gettAppId() + new Date().getTime() + RandomUtil.getRandomString(3) ;
        guanaitongPaymentBean.setOuter_trade_no(outer_trade_no);
        guanaitongPaymentBean.setBuyer_open_id(paymentBean.getOpenId());
        guanaitongPaymentBean.setReason("下单");
        float total_amount = 0.00f;
        for (Order order: orderList) {
            total_amount = total_amount + order.getSaleAmount() ;
        }
//        TradeInfoBean tradeInfoBean = new TradeInfoBean();
//        tradeInfoBean.setThirdTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
//        String tradeInfoString = JSON.toJSONString(tradeInfoBean) ;
//        guanaitongPaymentBean.setTrade_info(tradeInfoString);
        guanaitongPaymentBean.setReturn_url(paymentBean.getReturnUrl());
        guanaitongPaymentBean.setNotify_url(configBean.getGatBackUrl());
        guanaitongPaymentBean.setTotal_amount(total_amount);

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, String> map = oMapper.convertValue(guanaitongPaymentBean, Map.class);
        Result result1 = guanaitongClientService.payment(map, paymentBean.getiAppId()) ;
        JSONObject jsonObject = (JSONObject) JSON.toJSON(result1);
        String guanaitongUrl = configBean.getGatUrl() + jsonObject.getString("data") ;
        orderList.forEach(order -> {
            order.setPaymentNo(outer_trade_no);
            order.setOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
            order.setUpdatedAt(new Date());
            if (order.getPayStatus() != 5) {
                updatePaymentNo(order);
            }
        });
        UrlEncodeBean urlEncodeBean = new UrlEncodeBean();
        urlEncodeBean.setUrlEncode(guanaitongUrl);
        result.getData().put("result", urlEncodeBean);
        return result;
    }

    @Override
    public String back(BackRequest beanRequest) {
        BackBean bean = beanRequest.getData();
        List<Order> orders = findByOutTradeNoAndPaymentNo(bean.getOutTradeNo(), bean.getOrderNo());

        List<Integer> orderIdList = new ArrayList<>();
        // 遍历更新主订单
        orders.forEach(order1 -> {
            order1.setPaymentAmount(bean.getTotalFee());
            order1.setPaymentAt(new Date());
            order1.setStatus(1);
            order1.setPayee(bean.getPayee());
            order1.setPayer(bean.getPayer());
            order1.setPayStatus(bean.getPayStatus());
            order1.setPaymentTotalFee(bean.getTotalFee());
            order1.setPayOrderCategory(bean.getOrderCategory());
            order1.setPayType(bean.getPayType());
            order1.setRefundFee(bean.getRefundFee());
            order1.setAppId(bean.getAppId());

            // 执行更新
            updatePaymentByOutTradeNoAndPaymentNo(order1);

            // aoyi确认订单
            if (order1.getMerchantId() == 2) {
                confirmOrder(order1.getTradeNo());
            }
            orderIdList.add(order1.getId());
        });

        // 批量更新子订单状态为"待发货"
        orderRpcService.batchUpdateOrderDetailStatusByOrderIds(orderIdList, 1); // 0：已下单；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：失败

        return "success";
    }

    @Override
    public String gNotify(GATBackBean backBean) {
        String appId =  getSSOConfigByAppId(backBean.getAppid()) ;
        List<Order> orders = findByPaymentNoAndOpenId(backBean.getOuter_trade_no(), appId + backBean.getBuyer_open_id());
        int amount = Math.round(backBean.getTotal_amount() * 100);

        List<Integer> orderIdList = new ArrayList<>();
        orders.forEach(order1 -> {
            order1.setPaymentAmount(amount);
            order1.setPaymentAt(new Date());
            order1.setStatus(1);
            order1.setPayStatus(5);
            order1.setAppId(appId);
            // 执行更新
            updatePaymentByOutTradeNoAndPaymentNo(order1);

            // aoyi确认订单
            if ("10".equals(appId)) {
                confirmOrder(order1.getTradeNo());
            }

            orderIdList.add(order1.getId());
        });

        // 批量更新子订单状态为"待发货"
        orderRpcService.batchUpdateOrderDetailStatusByOrderIds(orderIdList, 1); // 0：已下单；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：失败
        OperaResponse operaResponse = orderService.sendTradeInfo(appId + backBean.getBuyer_open_id(), backBean.getOuter_trade_no(), appId) ;
        log.info("发送订单信息给关爱通，返回结果：{}", JSONUtil.toJsonString(operaResponse));
        return "success";
    }

    @Override
    public String refund(String subOrderNo) {
        // TODO 获取退款金额和外部订单号
        // TODO 调用退款接口
        // TODO 回调时更新退款订单状态
        return null;
    }

    private PaymentResult getPayment(PaymentBean paymentBean) {
        paymentBean.setAppId(paymentBean.getiAppId());
        OperaResult result = pinganClientService.payment(paymentBean);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            PaymentResult paymentResult = JSONObject.parseObject(jsonString, PaymentResult.class) ;
            return paymentResult;
        }
        return null;
    }

    private List<Order> findTradeNo(String appId, String merchantNo, String tradeNo) {
        OperaResult result = orderService.findOrderListByTradeNo(appId, merchantNo, tradeNo);
        log.info("order client findTradeNo 方法返回结果 result : {}", JSONUtil.toJsonString(result));
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) {
        OperaResult result = orderService.findByOutTradeNoAndPaymentNo(outTradeNo, paymentNo);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findByPaymentNoAndOpenId(String paymentNo, String openId) {
        OperaResult result = orderService.findByPaymentNoAndOpenId(paymentNo, openId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findOutTradeNo(String outTradeNo) {
        OperaResult result = orderService.findOrderListByOutTradeNo(outTradeNo);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private void updatePaymentNo(Order order) {
        OperaResult result = orderService.payment(order);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
        }
    }

    /**
     * 支付回调-更新主订单相关信息
     *
     * @param order
     */
    private void updatePaymentByOutTradeNoAndPaymentNo(Order order) {
        OperaResult result = orderService.paymentByOutTradeNoAndPaymentNo(order);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
        }
    }

    private boolean confirmOrder(String orderId) {
        OperaResponse<Boolean> result = aoyiClientService.conform(orderId);
        if (result.getCode() == 200) {
//            Map<String, Object> data = result.getData() ;
//            Object object = data.get("result");
//            String jsonString = JSON.toJSONString(object);
            boolean flag = result.getData();
            return flag;
        }
        return false;
    }

    private SSOConfigBean getSSOConfig(String appId) {
        return ssoConfiguration.getRegion().get(appId) ;
    }

    private String getSSOConfigByAppId(String appId) {
        Map<String, SSOConfigBean> map = ssoConfiguration.getRegion() ;
        for (String key : map.keySet()) {
            SSOConfigBean configBean = map.get(key);
            if (appId.equals(configBean.getGatAppId())) {
                return key ;
            }
        }
        return null ;
    }

}
