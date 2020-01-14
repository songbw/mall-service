package com.fengchao.aoyi.client.bean.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeipinhuiResponse<T> {

    private Boolean success;

    private String message;

    /**
     * 200 成功
     */
    private Integer code;

    private Long timestamp;

    /**
     * 业务结果
     */
    private T result;
}
