package com.fengchao.pingan.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.InitCodeRequestBean;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Date;

public class HttpClient {

//    public static final String PINGAN_PATH = "http://citycard-stg.pingan.com.cn";
    public static final String PINGAN_PATH = "http://119.3.111.161:8088/v1";
//    public static final String PINGAN_PATH = "http://192.168.200.122:8088/v1";

    public static final String ACCESS_TOKEN = "http://citycard-stg.pingan.com.cn/open-front/api/accessToken" ;

    public static final String USER_INFO = "http://citycard-stg.pingan.com.cn/open-front/api/userInfo" ;

    // 预下单接口地址
//    public static final String PAYMENT_ORDER = "/wuxi-pay-web/partner/fengchao/preOrder" ;
    public static final String PAYMENT_ORDER = "/preOrder" ;
    // 退款接口地址
    public static final String PAY_REFUND = "/wuxi-pay-web/partner/refundOrder" ;
    // 发起支付接口地址
    public static final String PAY_ORDER = "/wuxi-pay-web/sdk/fengchao/payOrderByOrderNo" ;
    // 回调通知地址
    public static final String NOTIFY_URL = "http://api.weesharing.com/v2/ssoes/payment/pingan/back" ;
//    public static final String NOTIFY_URL = "http://192.168.200.37:8000/v2/ssoes/payment/pingan/back" ;


    public static final String PAY_BILL = "http://citycard-stg.pingan.com.cn/nanning-gateway/appfront/download_bill" ;

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static Client client = null;

    public static Client createClient() {
        Client client = ClientBuilder.newClient();
        return client;
    }

    public static void main(String args[]) {
        InitCodeRequestBean bean = new InitCodeRequestBean();
        bean.setTimestamp(new Date().getTime() + "");
        bean.setRandomSeries(RandomUtil.getRandomString(10));
        bean.setAppId("ea70244ca3604a4ebc1c2fd8e48756d5");
        BeanMap testMap = new BeanMap(bean);
        System.out.println(testMap);
    }

}
