package com.fengchao.guanaitong.config;

import com.alibaba.fastjson.JSON;
import com.fengchao.guanaitong.bean.WXIds;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 微信公众号支付配置类
 * @author clark
 *
 * */

@Configuration
@ConfigurationProperties("weixin")
@Component
@Slf4j
@Getter
@Setter
public class WeChatConfiguration implements InitializingBean {

    private List<WXIds> ids;

    @Override
    public void afterPropertiesSet() {
        log.info("===微信支付配置项: {}", JSON.toJSONString(ids));

    }
}

