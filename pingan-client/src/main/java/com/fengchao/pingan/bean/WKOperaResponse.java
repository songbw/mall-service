package com.fengchao.pingan.bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@SuppressWarnings("serial")
@Setter
@Getter
public class WKOperaResponse<T> implements Serializable {

	private String ret = "0" ;

	private String errorCode ;
	
	// 结果描述(消息框的内容)
	private String msg = "Success";
	private String smsg = "Success";

	//封装的对象
	private T data;

	private Long time ;
	
}
