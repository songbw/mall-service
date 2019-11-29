package com.fengchao.pingan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.config.PingAnClientConfig;
import com.fengchao.pingan.exception.PinganClientException;
import com.fengchao.pingan.feign.WSPayClientService;
import com.fengchao.pingan.service.PaymentService;
import com.fengchao.pingan.utils.HttpClient;
import com.fengchao.pingan.utils.JSONUtil;
import com.fengchao.pingan.utils.Pkcs8Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@EnableConfigurationProperties({PingAnClientConfig.class})
@Service
public class PaymentServiceImpl implements PaymentService {

    private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private WSPayClientService payClientService;
    @Autowired
    private PingAnClientConfig config;

    @Override
    public PaymentResult paymentOrder(PaymentBean paymentBean) throws PinganClientException {
        logger.info("ping url is "+ HttpClient.PINGAN_PATH + HttpClient.PAYMENT_ORDER);
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.PINGAN_PATH + HttpClient.PAYMENT_ORDER);
        ZfResquest resquest = new ZfResquest();
        PaymentParam param = new PaymentParam();
        param.setTotalFee(paymentBean.getAmount());
        param.setActPayFee(paymentBean.getAmount());
        param.setBody(paymentBean.getGoodsName());
        param.setOutTradeNo(paymentBean.getAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
        param.setNotifyUrl(config.getNotifyUrl());
        resquest.setData(param);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(param, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        try {
            String sign = Pkcs8Util.getSign(messageString);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("payment request is " + objectMapper.writeValueAsString(resquest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(resquest, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            PaymentResult paymentResult = response.readEntity(PaymentResult.class);
            try {
                logger.info("payment response is " + objectMapper.writeValueAsString(paymentResult));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return  paymentResult;
        } else {
            try {
                logger.info("payment error response is " + objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public OperaResponse<CreatePaymentOrderBean> createPaymentOrder(CreatePaymentOrderRequestBean paymentBean) {
        logger.info("ping an url is "+ config.getPayBasePath() + HttpClient.CREATE_PAYMENT_ORDER);
        PaymentParamBean<CreatePaymentOrderRequestBean> paramBean = new PaymentParamBean<CreatePaymentOrderRequestBean>() ;
        paramBean.setAppId(config.getPayAppId());
        paymentBean.setMerchantNo(config.getPayMerchantNo());
        WebTarget webTarget = HttpClient.createClient().target(config.getPayBasePath() + HttpClient.CREATE_PAYMENT_ORDER);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(paymentBean, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        String sign = Pkcs8Util.getSM3(messageString + config.getPayAppKey()) ;
        paramBean.setSign(sign);
        paramBean.setMessage(paymentBean);
        logger.info("请求平安 create payment order 参数： {}",JSONUtil.toJsonString(paramBean));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(paramBean, MediaType.APPLICATION_JSON));
        OperaResponse<CreatePaymentOrderBean> result = response.readEntity(OperaResponse.class);
        result.setMsg(result.getMessage());
        logger.info("获取create payment order 返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }

    @Override
    public PaymentResult payRefund(RefundBean bean) throws PinganClientException {
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.PINGAN_PATH + HttpClient.PAY_REFUND);
        ZfResquest resquest = new ZfResquest();
        RefundParam param = new RefundParam();
        param.setTotalFee(bean.getAmount());
        param.setRefundFee(bean.getAmount());
        param.setNoticeUrl(config.getNotifyUrl());
        param.setOutRefundNo(bean.getOutRefundNo());
        param.setSourceOutTradeNo(bean.getSourceOutTradeNo());
        param.setSourceOrderNo(bean.getSourceOrderNo());
        resquest.setData(param);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(param, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        try {
            String sign = Pkcs8Util.getSign(messageString);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("payRefund request is " + objectMapper.writeValueAsString(resquest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(resquest, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            System.out.println(response.readEntity(String.class));
            return null;
//            return  response.readEntity(PaymentResult.class);
        } else {
            return null;
        }
    }

    @Override
    public PaymentResult wsPayClient(PaymentBean paymentBean) {
        PrePayDTO prePayDTO = new PrePayDTO();
        prePayDTO.setOutTradeNo(paymentBean.getAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
        prePayDTO.setTotalFee(paymentBean.getAmount() + "");
        prePayDTO.setActPayFee(paymentBean.getAmount() + "");
        prePayDTO.setBody(paymentBean.getGoodsName());
        prePayDTO.setNotifyUrl(config.getNotifyUrl());
        logger.info("聚合支付请求参数值： {}", JSONUtil.toJsonString(prePayDTO));
        PaymentResult result = new PaymentResult();
        CommonResult<PrePayResultDTO> prePayResultDTOCommonResult = payClientService.payment(prePayDTO) ;
        logger.info("聚合支付返回值： {}", JSONUtil.toJsonString(prePayResultDTOCommonResult));
        if (prePayResultDTOCommonResult.getCode() == 200) {
            PrePayResultDTO prePayResultDTO = prePayResultDTOCommonResult.getData();
            OrderNo orderNo = new OrderNo();
            orderNo.setOrderNo(prePayResultDTO.getOrderNo());
            orderNo.setOutTradeNo(prePayResultDTO.getOutTradeNo());
            result.setReturnCode("200");
            result.setData(orderNo);
            return result;
        } else {
            result.setReturnCode(prePayResultDTOCommonResult.getCode() + "");
            result.setMsg(prePayResultDTOCommonResult.getMessage());
            return result;
        }
    }

    @Override
    public OperaResponse<QueryPaymentOrderBean> queryPaymentOrder(QueryPaymentOrderRequestBean paymentBean) {
        logger.info("query payment order url is "+ config.getPayBasePath() + HttpClient.QUERY_PAYMENT_ORDER);
        PaymentParamBean<QueryPaymentOrderRequestBean> paramBean = new PaymentParamBean<QueryPaymentOrderRequestBean>() ;
        paramBean.setAppId(config.getPayAppId());
        paymentBean.setMerchantNo(config.getPayMerchantNo());
        WebTarget webTarget = HttpClient.createClient().target(config.getPayBasePath() + HttpClient.QUERY_PAYMENT_ORDER);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(paymentBean, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        String sign = Pkcs8Util.getSM3(messageString + config.getPayAppKey()) ;
        paramBean.setSign(sign);
        paramBean.setMessage(paymentBean);
        logger.info("请求平安 query payment order 参数： {}", JSONUtil.toJsonString(paramBean));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(paramBean, MediaType.APPLICATION_JSON));
        OperaResponse<QueryPaymentOrderBean> result = response.readEntity(OperaResponse.class);
        result.setMsg(result.getMessage());
        logger.info("获取 query payment order 返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }

    @Override
    public OperaResponse<OrderRefundBean> orderRefund(OrderRefundRequestBean paymentBean) {
        logger.info("order refund url is "+ config.getPayBasePath() + HttpClient.ORDER_REFUND);
        PaymentParamBean<OrderRefundRequestBean> paramBean = new PaymentParamBean<OrderRefundRequestBean>() ;
        paramBean.setAppId(config.getPayAppId());

        paymentBean.setMerchantNo(config.getPayMerchantNo());
        WebTarget webTarget = HttpClient.createClient().target(config.getPayBasePath() + HttpClient.ORDER_REFUND);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(paymentBean, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        String sign = Pkcs8Util.getSM3(messageString + config.getPayAppKey()) ;
        paramBean.setSign(sign);
        paramBean.setMessage(paymentBean);
        logger.info("请求平安 order refund 参数： {}", JSONUtil.toJsonString(paramBean));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(paramBean, MediaType.APPLICATION_JSON));
        OperaResponse<OrderRefundBean> result = response.readEntity(OperaResponse.class);
        result.setMsg(result.getMessage());
        logger.info("获取 order refund 返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }

    @Override
    public String backNotify(BackNotifyRequestBean paymentBean) {
        logger.info("平安支付回调 back notify 参数： {}", JSONUtil.toJsonString(paymentBean));
        String backSign = paymentBean.getSign() ;
        paymentBean.setSign(null);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(paymentBean, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        String sign = Pkcs8Util.getSM3(messageString + config.getPayAppKey()) ;
        if (backSign.equals(sign)) {
            // TODO 聚合支付服务
            return "{\"code\": \"SUCCESS\"}" ;
        }
        return "{\"code\": \"false\"}" ;
    }
}
