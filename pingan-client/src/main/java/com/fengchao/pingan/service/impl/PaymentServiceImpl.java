package com.fengchao.pingan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.exception.PinganClientException;
import com.fengchao.pingan.feign.WSPayClientService;
import com.fengchao.pingan.service.PaymentService;
import com.fengchao.pingan.utils.HttpClient;
import com.fengchao.pingan.utils.Pkcs8Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private WSPayClientService payClientService;

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
        param.setNotifyUrl(HttpClient.NOTIFY_URL);
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
    public PaymentResult payRefund(RefundBean bean) throws PinganClientException {
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.PINGAN_PATH + HttpClient.PAY_REFUND);
        ZfResquest resquest = new ZfResquest();
        RefundParam param = new RefundParam();
        param.setTotalFee(bean.getAmount());
        param.setRefundFee(bean.getAmount());
        param.setNoticeUrl(HttpClient.NOTIFY_URL);
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
        PaymentResult result = new PaymentResult();
        CommonResult<PrePayResultDTO> prePayResultDTOCommonResult = payClientService.payment(paymentBean) ;
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
}
