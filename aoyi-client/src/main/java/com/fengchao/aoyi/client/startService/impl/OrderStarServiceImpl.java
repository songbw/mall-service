package com.fengchao.aoyi.client.startService.impl;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.config.StarClientConfig;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.OrderStarService;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.utils.StarHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songbw
 * @date 2020/1/3 16:21
 */
@EnableConfigurationProperties({StarClientConfig.class})
@Slf4j
@Service
public class OrderStarServiceImpl implements OrderStarService {

    @Autowired
    private StarClientConfig starClientConfig;

    @Override
    public OperaResponse addOrder(StarOrderBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("outOrderNo", bean.getOutOrderNo());
        params.put("receiverAreaId", bean.getReceiverAreaId());
        params.put("receiverAreaName", bean.getReceiverAreaName());
        params.put("receiverAddr", bean.getReceiverAddr());
        params.put("receiver", bean.getReceiver());
        params.put("receiverPhone", bean.getReceiverPhone());
        params.put("receiverMobile", bean.getReceiverMobile());
        params.put("freight", bean.getFreight());
        params.put("buyerRemark", bean.getBuyerRemark());
        params.put("sellerRemark", bean.getSellerRemark());
        //海淘商品必填
        if (!StringUtils.isEmpty(bean.getIdcardName())) {
            params.put("idcardName", bean.getIdcardName());
        }
        if (!StringUtils.isEmpty(bean.getIdcardNo())) {
            params.put("idcardNo", bean.getIdcardNo());
        }
        if (!StringUtils.isEmpty(bean.getIdcardFirstUrl())) {
            params.put("idcardFirstUrl", bean.getIdcardFirstUrl());
        }
        if (!StringUtils.isEmpty(bean.getIdcardSecondUrl())) {
            params.put("idcardSecondUrl", bean.getIdcardSecondUrl());
        }
        params.put("skuList", JSONUtil.toJsonString(bean.getSkuList()));
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_ADD_ORDER, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("订单下单, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse confirmOrder(String orderSn) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", orderSn);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_CONFIRM_ORDER, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("确认收货, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse applyRefundAndGoods(ApplyRefundAndGoodsBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", bean.getOrderSn());
        params.put("reason", bean.getReason());
        params.put("code", bean.getCode());
        params.put("returnType", bean.getReturnType());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_APPLY_REFUND_GOODS, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("退货退款, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse applyRefund(ApplyRefundAndGoodsBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", bean.getOrderSn());
        params.put("reason", bean.getReason());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_APPLY_REFUND, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("退款, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getOrderFreight(FreightBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("skuList", bean.getSkuList());
        params.put("receiverAreaId", bean.getReceiverAreaId());
        params.put("receiverAddr", bean.getReceiverAddr());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_FREIGHT, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询订单运费信息, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse cancelApplyRefund(CancelApplyRefundBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", bean.getOrderSn());
        params.put("serviceSn", bean.getServiceSn());
        params.put("reason", bean.getReason());
        //1：取消退款，2：取消退货退款
        params.put("type", bean.getType());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_CANCEL_APPLY_REFUND, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("取消退货退款申请, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findExpressInfoByOrderSn(String orderSn) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", orderSn);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_EXPRESS, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询物流信息, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findOrderByOrderSn(String orderSn) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", orderSn);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_FIND_BY_ORDERSN, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("根据星链子订单编号  查询订单, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getLogisticsCompanyList() {
        Map<String, String> params = new HashMap<>();
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_LOGISTICS_COMPANY, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询物流公司信息, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getReturnOrderStatuts(String serviceSn) {
        Map<String, String> params = new HashMap<>();
        params.put("serviceSn", serviceSn);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_RETURN_STATUS, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询退货退款状态, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getReturnOrderStatutsNotify(ReturnOrderGoodsBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", bean.getOrderSn());
        params.put("serviceSn", bean.getServiceSn());
        params.put("deliveryCorpSn", bean.getDeliveryCorpSn());
        params.put("deliveryCorpName", bean.getDeliveryCorpName());
        params.put("deliverySn", bean.getDeliverySn());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ORDER_RETURN_GOODS, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("买家发货, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }
}
