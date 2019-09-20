package com.fengchao.pingan.bean;

import lombok.Data;

/**
 * <p>
 * 预支付结果对象
 * </p>
 *
 * @author ZhangPeng
 * @since 2019-09-18
 */

@Data
public class PrePayResultDTO {

	private String orderNo;

	private String outTradeNo;

	public PrePayResultDTO(String orderNo, String outTradeNo) {
		this.orderNo = orderNo;
		this.outTradeNo = outTradeNo;
	}

}
