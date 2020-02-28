package com.fengchao.pingan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class HttpClient {

    public static final String PINGAN_PATH = "http://119.3.111.161:8088/v1";

    public static final String ACCESS_TOKEN = "http://citycard-stg.pingan.com.cn/open-front/api/accessToken";

    public static final String USER_INFO = "http://citycard-stg.pingan.com.cn/open-front/api/userInfo";

    // 预下单接口地址
//    public static final String PAYMENT_ORDER = "/wuxi-pay-web/partner/fengchao/preOrder" ;
    public static final String PAYMENT_ORDER = "/preOrder";
    // 退款接口地址
    public static final String PAY_REFUND = "/wuxi-pay-web/partner/refundOrder";
    // 发起支付接口地址
    public static final String PAY_ORDER = "/wuxi-pay-web/sdk/fengchao/payOrderByOrderNo";
    public static final String INIT_CODE_URL = "/openPlatform/initCode/getInitCode.do" ;
    public static final String AUTH_CODE_URL = "/openPlatform/authCode/getAuthCode.do" ;
    public static final String ACCESS_TOKEN_URL = "/openPlatform/accessToken/getAccessToken.do" ;
    public static final String REFRESH_TOKEN_URL = "/openPlatform/accessToken/refreshAccessToken.do" ;
    public static final String CHECK_TOKEN_URL = "/openPlatform/accessToken/checkAccessToken.do" ;
    public static final String CHECK_REQUEST_CODE_URL = "/openPlatform/request/checkRequestCode.do" ;
    public static final String USER_iNFO_URL = "/openPlatform/userInfo/getUserInfo.do" ;
    public static final String CREATE_PAYMENT_ORDER = "/createPaymentOrder" ;
    public static final String QUERY_PAYMENT_ORDER = "/queryPaymentOrder" ;
    public static final String ORDER_REFUND = "/orderRefund" ;

    // 万科云城接口
    public static final String WK_USER_INFO_URL = "/uaa/sns/userinfo" ;
    public static final String WK_PAYMENT_REFUND = "/payservice/payment/adapt/order/refund" ;


    public static final String PAY_BILL = "http://citycard-stg.pingan.com.cn/nanning-gateway/appfront/download_bill";

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static Client client = null;

    public static Client createClient() {
        Client client = ClientBuilder.newClient();
        return client;
    }

}
