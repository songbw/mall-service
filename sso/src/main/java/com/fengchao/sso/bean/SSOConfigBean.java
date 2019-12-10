package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/10 15:31
 */
@Setter
@Getter
public class SSOConfigBean {

    private String gatBackUrl;
    private String gatUrl;
    private String wxAppId;
    private String wxAppSecret;
    private String gatAppId;

}
