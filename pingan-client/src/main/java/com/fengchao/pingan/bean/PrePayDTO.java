package com.fengchao.pingan.bean;

import lombok.Data;

/**
 * <p>
 * 	预支付对象
 * </p>
 *
 * @author ZhangPeng
 * @since 2019-09-18
 */
@Data
public class PrePayDTO {

    private String outTradeNo;

    private String body;

    private String remark;

    private String totalFee;

    private String actPayFee;

    private String limitPay;

    private String returnUrl;

    private String notifyUrl;

}
