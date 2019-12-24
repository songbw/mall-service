package com.fengchao.guanaitong.config;

import com.fengchao.guanaitong.bean.WXIds;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@ConfigurationProperties("weixin")
@Component
@Slf4j
@Getter
@Setter
public class WeChatConfiguration {

    private List<WXIds> ids;

}

