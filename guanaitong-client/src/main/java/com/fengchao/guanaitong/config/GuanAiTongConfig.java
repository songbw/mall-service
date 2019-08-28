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

    private static String CONFIG_GAT_APP_ID;
    private static String CONFIG_GAT_APP_SECRET;

    @Override
    public void afterPropertiesSet() {
        CONFIG_GAT_APP_ID = GAT_APP_ID;
        CONFIG_GAT_APP_SECRET = GAT_APP_SECRET;
        log.info("==== get guan_ai_tong config appId={}, appSecret={}",CONFIG_GAT_APP_ID,CONFIG_GAT_APP_SECRET);
    }

    public static String getAppId() {
        return CONFIG_GAT_APP_ID;
    }

    public static String getAppSecret() {
        return CONFIG_GAT_APP_SECRET;
    }
}

