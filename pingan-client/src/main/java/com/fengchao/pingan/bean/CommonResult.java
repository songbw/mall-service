package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
@Setter
@Getter
public class CommonResult<T> implements Serializable {
    private long code = 200;
    private String message = "Success";
    private T data;
}
