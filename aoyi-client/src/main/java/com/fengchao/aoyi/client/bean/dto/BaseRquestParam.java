package com.fengchao.aoyi.client.bean.dto;

import lombok.Data;

/**
 * @Auther: labanYB
 * @Date: 2019/6/14 0014 15:37
 * @Description: public param
 */
@Data
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
