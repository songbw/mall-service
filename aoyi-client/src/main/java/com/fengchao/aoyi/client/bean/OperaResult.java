package com.fengchao.aoyi.client.bean;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class OperaResult<T> implements Serializable {
	
	// 结果码
	private Integer code = 200;
	
	// 结果描述(消息框的内容)
	private String msg = "Success";
	
	//封装的对象
	private T data;
	
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
