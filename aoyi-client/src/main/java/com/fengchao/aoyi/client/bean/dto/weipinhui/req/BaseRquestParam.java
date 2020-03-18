package com.fengchao.aoyi.client.bean.dto.weipinhui.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: labanYB
 * @Date: 2019/6/14 0014 15:37
 * @Description: public param
 */
@Setter
@Getter
public class BaseRquestParam {

    //appId
    private String appId;

    //app_secret
    private String appSecret;

    //timestamp
    private Long timestamp;

    //sign
    private String sign;
}
