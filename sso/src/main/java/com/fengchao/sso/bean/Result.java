package com.fengchao.sso.bean;

import java.io.Serializable;

public class Result implements Serializable {
    // 结果码
    private Integer code = 200;

    // 结果描述(消息框的内容)
    private String msg = "Success";

    //封装的对象
    private Object data = new Object();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
