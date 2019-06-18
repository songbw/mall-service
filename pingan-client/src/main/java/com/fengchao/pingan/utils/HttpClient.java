package com.fengchao.pingan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class HttpClient {

    public static final String PINGAN_PATH = "http://citycard-stg.pingan.com.cn";

    public static final String ACCESS_TOKEN = "http://citycard-stg.pingan.com.cn/open-front/api/accessToken" ;

    public static final String USER_INFO = "http://citycard-stg.pingan.com.cn/open-front/api/userInfo" ;

//    private static final String PAYMENT_ORDER = "http://sc-cc-test.yun.city.pingan.com/nanning-gateway/appfront/createPaymentOrder" ;

    // 预下单接口地址
    public static final String PAYMENT_ORDER = "/wuxi-pay-web/partner/fengchao/preOrder" ;
    // 退款接口地址
    public static final String PAY_REFUND = "/wuxi-pay-web/partner/refundOrder" ;
    // 发起支付接口地址
    public static final String PAY_ORDER = "/wuxi-pay-web/sdk/fengchao/payOrderByOrderNo" ;
    // 回调通知地址
    public static final String NOTIFY_URL = "http://api.weesharing.com/v2/ssoes/payment/pingan/back" ;

//    private static final String PAY_REFUND = "http://citycard-stg.pingan.com.cn/nanning-gateway/appfront/purse/refund/payRefund" ;


    public static final String PAY_BILL = "http://citycard-stg.pingan.com.cn/nanning-gateway/appfront/download_bill" ;

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static Client client = null;

    public static Client createClient() {
        Client client = ClientBuilder.newClient();
        return client;
    }

}
