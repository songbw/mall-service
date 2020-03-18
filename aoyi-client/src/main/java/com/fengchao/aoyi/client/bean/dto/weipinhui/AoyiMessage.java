package com.fengchao.aoyi.client.bean.dto.weipinhui;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiMessage {

    /**
     * 消息类型 10 上下架变更 11 价格变更 12 商品信息变更
     */
    private String type; //

    /**
     * 消息时间 2019-07-31 12:00:00"
     */
    private String msgTime;

    /**
     *
     */
    private String message;
}
