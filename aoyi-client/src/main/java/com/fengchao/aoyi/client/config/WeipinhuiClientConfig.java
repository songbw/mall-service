package com.fengchao.aoyi.client.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 */
@ConfigurationProperties
@Slf4j
@Component
@Getter
public class WeipinhuiClientConfig {

    @Value("${weipinhui.appid}")
    private String appId;

    @Value("${weipinhui.appSecret}")
    private String appSecret;

    @Value("${weipinhui.privateKey}")
    private String privateKey;

    @Value("${weipinhui.url.getBrand}")
    private String urlGetBrand;

    @Value("${weipinhui.url.getCategory}")
    private String urlGetCategory;

    @Value("${weipinhui.url.queryItemsList}")
    private String urlQueryItemsList;

    @Value("${weipinhui.url.queryItemDetial}")
    private String urlQueryItemDetial;

    @Value("${weipinhui.url.queryItemInventory}")
    private String urlQueryItemInventory;

    @Value("${weipinhui.url.renderOrder}")
    private String urlRenderOrder;

    @Value("${weipinhui.url.createOrder}")
    private String urlCreateOrder;

    @PostConstruct//在servlet初始化的时候加载，并且只加载一次，和构造代码块的作用类似
    private void init() {
        log.info("product config properties start!");

        // 唯品会相关
        log.info("weipinhui.appid = {}", appId);
        log.info("weipinhui.appSecret = {}", appSecret);
        log.info("weipinhui.privateKey = {}", privateKey);

        log.info("weipinhui.url.getBrand = {}", urlGetBrand);
        log.info("weipinhui.url.getCategory = {}", urlGetCategory);
        log.info("weipinhui.url.queryItemsList = {}", urlQueryItemsList);
        log.info("weipinhui.url.queryItemDetial = {}", urlQueryItemDetial);
        log.info("weipinhui.url.queryItemInventory = {}", urlQueryItemInventory);
        log.info("weipinhui.url.renderOrder = {}", urlRenderOrder);
        log.info("weipinhui.url.createOrder = {}", urlCreateOrder);
    }
}
