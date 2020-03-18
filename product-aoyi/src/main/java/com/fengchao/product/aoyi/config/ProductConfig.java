package com.fengchao.product.aoyi.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@ConfigurationProperties
@Component
@Slf4j
@Getter
public class ProductConfig {

    @Value("${weipinhui.imagePrefix}")
    private String weipinhuiImagePrefix;

    @Value("${fengchao.imagePrefix}")
    private String fengchaoImagePrefix;

    @PostConstruct
    private void init() {
        log.info("config properties start!");

        log.info("weipinhui.imagePrefix = {}", weipinhuiImagePrefix);
        log.info("fengchao.imagePrefix = {}", fengchaoImagePrefix);
    }
}
