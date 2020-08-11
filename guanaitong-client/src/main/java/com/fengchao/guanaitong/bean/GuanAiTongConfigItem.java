package com.fengchao.guanaitong.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 关爱通支付配置项
 * @author clark
 * @since 2020-05-04
 * */
@Getter
@Setter
public class GuanAiTongConfigItem {

    /** 凤巢客户端多端（无锡，最珠海...） appId*/
    private String iAppId;

    /** 关爱通分配给客户的 appId,appSecret,urlPrefix */
    private String appId;
    private String appSecret;
    private String urlPrefix;
}
