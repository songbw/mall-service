package com.fengchao.guanaitong.config;

import com.alibaba.fastjson.JSON;
import com.fengchao.guanaitong.bean.GuanAiTongConfigItem;
import com.fengchao.guanaitong.constant.MyErrorEnum;
import com.fengchao.guanaitong.exception.MyException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 关爱通支付配置类
 * @author clark
 * @since 2020-05-04
 *
 * */

@Configuration
@ConfigurationProperties("guanaitong")
@Component
@Slf4j
@Getter
@Setter
public class GuanAiTongConfiguration implements InitializingBean {

    /**
     guanaitong:
     config[0]:
     iAppId: "08"
     appId: "20110908"
     appSecret: "cdbbe8838363006d244fbb214876348f"
     urlPrefix: "https://openapi.guanaitong.tech"
     config[1]:
     iAppId: "09"
     appId: "20110867"
     appSecret: "d09469d7a5b1a8c57d980d6ee3eff5eb"
     urlPrefix: "https://openapi.guanaitong.tech"
     * */

    private List<GuanAiTongConfigItem> config;

    @Override
    public void afterPropertiesSet() {
        log.info("===关爱通配置项: {}", JSON.toJSONString(config));

    }

    public String
    getConfigAppId(String iAppId){
        for(GuanAiTongConfigItem item: this.config){
            if(item.getIAppId().equals(iAppId)){
                return item.getAppId();
            }
        }
        throw new MyException(MyErrorEnum.CONFIG_BLANK_FOR_IAPPID,"appId="+iAppId);
    }

    public String
    getConfigAppSecret(String iAppId){
        for(GuanAiTongConfigItem item: this.config){
            if(item.getIAppId().equals(iAppId)){
                return item.getAppSecret();
            }
        }
        throw new MyException(MyErrorEnum.CONFIG_BLANK_FOR_IAPPID,"appId="+iAppId);
    }

    public String
    getConfigUrlPrefix(String iAppId){
        for(GuanAiTongConfigItem item: this.config){
            if(item.getIAppId().equals(iAppId)){
                return item.getUrlPrefix();
            }
        }
        throw new MyException(MyErrorEnum.CONFIG_BLANK_FOR_IAPPID,"appId="+iAppId);
    }
}
