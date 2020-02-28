package com.fengchao.pingan.service.impl;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.config.PingAnClientConfig;
import com.fengchao.pingan.feign.WSPayClientService;
import com.fengchao.pingan.service.WKPaymentService;
import com.fengchao.pingan.utils.HttpClient;
import com.fengchao.pingan.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * @author song
 * @2020-02-26 4:56 下午
 **/
@Slf4j
@EnableConfigurationProperties({PingAnClientConfig.class})
@Service
public class WKPaymentServiceImpl implements WKPaymentService {

    @Autowired
    private PingAnClientConfig config;
    @Autowired
    private WSPayClientService payClientService;

    @Override
    public WKOperaResponse<WKRefund> refundApply(WKRefundRequestBean bean) {
        log.info("万科云城退款 请求 参数： {}", JSONUtil.toJsonString(bean));
        WebTarget webTarget = HttpClient.createClient().target(config.getWkBaseUrl() + HttpClient.WK_PAYMENT_REFUND);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        WKOperaResponse result = response.readEntity(WKOperaResponse.class);
        log.info("获取万科退款返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }

    @Override
    public String paymentNotify(WKPaymentNotifyRequestBean bean) {
        log.info("万科云城支付回调 back notify 参数： {}", JSONUtil.toJsonString(bean));
        // 聚合支付服务
        if ("1".equals(bean.getStatus())) {
            AggPayBackBean aggPayBackBean = new AggPayBackBean() ;
            aggPayBackBean.setTradeNo(bean.getPaymentNo());
            aggPayBackBean.setOrderNo(bean.getOrderNo());
            aggPayBackBean.setPayFee(bean.getOrderAmount().multiply(new BigDecimal(100)).intValue() + "");
            aggPayBackBean.setTradeDate(bean.getPaymentTime());
            aggPayBackBean.setPayType("yuncheng");
            CommonResult<String> aggPayBackResult = payClientService.aggPayBack(aggPayBackBean) ;
            if (aggPayBackResult.getCode() == 200) {
                return "SUCCESS" ;
            }
        } else {
            // 支付失败 status != 1
            return "SUCCESS" ;
        }
        return "error";
    }

    @Override
    public String refundNotify(WKRefundNotifyRequestBean bean) {
        log.info("万科云城退款回调 refund notify 参数： {}", JSONUtil.toJsonString(bean));
        // 聚合支付服务
        if ("1".equals(bean.getStatus())) {
            AggPayBackBean aggPayBackBean = new AggPayBackBean() ;
            aggPayBackBean.setTradeNo(bean.getOrderNo());
            aggPayBackBean.setOrderNo(bean.getOrderNo());
            aggPayBackBean.setRefundNo(bean.getRefundNo());
            aggPayBackBean.setRefundFee(bean.getRefundAmount().multiply(new BigDecimal(100)).intValue() + "");
            aggPayBackBean.setTradeDate(bean.getRefundTime());
            aggPayBackBean.setPayType("yuncheng");
            CommonResult<String> aggPayBackResult = payClientService.aggPayBack(aggPayBackBean) ;
            if (aggPayBackResult.getCode() == 200) {
                return "SUCCESS" ;
            }
        } else {
            // 支付失败 status != 1
            return "SUCCESS" ;
        }
        return "error";
    }
}
