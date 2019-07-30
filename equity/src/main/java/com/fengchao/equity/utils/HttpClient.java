package com.fengchao.equity.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class HttpClient {

    //头食接口
    public static final String TOUDHI_PATH = "https://top.chinainnet.com";
//    领取券获取code和URL
    public static final String TOUDHI_COUPONCODE= "/coupon/getCouponCode" ;

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static Client client = null;

    public static Client createClient() {
        Client client = ClientBuilder.newClient();
        return client;
    }

}
