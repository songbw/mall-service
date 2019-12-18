package com.fengchao.guanaitong.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JssdkSignBean {
    private String appId; // 必填，公众号的唯一标识
    private String timestamp;// 必填，生成签名的时间戳
    private String nonceStr; // 必填，生成签名的随机串
    private String signature; // 必填，签名，见附录1
}
