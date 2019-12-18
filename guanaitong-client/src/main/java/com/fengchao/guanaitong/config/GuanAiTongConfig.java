package com.fengchao.guanaitong.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GuanAiTongConfig implements InitializingBean {

    @Value("${GAT_APP_ID}")
    public String GAT_APP_ID;

    @Value("${GAT_APP_SECRET}")
    public  String GAT_APP_SECRET;

    @Value("${GAT_URL_PREFIX}")
    public  String GAT_URL_PREFIX;

    private static String CONFIG_GAT_APP_ID;
    private static String CONFIG_GAT_APP_SECRET;
    private static String CONFIG_GAT_URL_PREFIX;


    @Value("${WECHAT_JSSDK_APP_ID}")
    public  String WECHAT_JSSDK_APP_ID;

    @Value("${WECHAT_JSSDK_APP_SECRET}")
    public  String WECHAT_JSSDK_APP_SECRET;

    @Value("${WECHAT_JSSDK_URL_PREFIX}")
    public  String WECHAT_JSSDK_URL_PREFIX;

    private static String CONFIG_WECHAT_JSSDK_APP_ID;
    private static String CONFIG_WECHAT_JSSDK_APP_SECRET;
    private static String CONFIG_WECHAT_JSSDK_URL_PREFIX;

    @Override
    public void afterPropertiesSet() {
        CONFIG_GAT_APP_ID = GAT_APP_ID;
        CONFIG_GAT_APP_SECRET = GAT_APP_SECRET;
        CONFIG_GAT_URL_PREFIX = GAT_URL_PREFIX;
        CONFIG_WECHAT_JSSDK_APP_ID = WECHAT_JSSDK_APP_ID;
        CONFIG_WECHAT_JSSDK_APP_SECRET = WECHAT_JSSDK_APP_SECRET;
        CONFIG_WECHAT_JSSDK_URL_PREFIX = WECHAT_JSSDK_URL_PREFIX;
        log.info("==== get guan_ai_tong config appId={}, appSecret={}, url={}",
                CONFIG_GAT_APP_ID,CONFIG_GAT_APP_SECRET,CONFIG_GAT_URL_PREFIX);
        log.info("==== get JSSDK config appId={}, appSecret={}, url={}",
                CONFIG_WECHAT_JSSDK_APP_ID,CONFIG_WECHAT_JSSDK_APP_SECRET,CONFIG_WECHAT_JSSDK_URL_PREFIX);

    }

    public static String getAppId() {
        return CONFIG_GAT_APP_ID;
    }

    public static String getAppSecret() {
        return CONFIG_GAT_APP_SECRET;
    }

    public static String getConfigGatUrlPrefix() {
        return CONFIG_GAT_URL_PREFIX;
    }

    public static String getJSSDKAppId(){return CONFIG_WECHAT_JSSDK_APP_ID;}

    public static String getJSSDKAppSecret(){return CONFIG_WECHAT_JSSDK_APP_SECRET;}

    public static String getJSSDKUrlPrefix(){return CONFIG_WECHAT_JSSDK_URL_PREFIX;}
}

