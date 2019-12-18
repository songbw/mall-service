package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author songbw
 * @date 2019/11/29 11:19
 */
@Setter
@Getter
public class PaymentParamBean<T> implements Serializable {
    private String appId ;
    private String sign ;
    private T message ;
}
