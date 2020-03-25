package com.fengchao.order.rpc.extmodel.weipinhui;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单地址信息
 */
@Setter
@Getter
public class AoyiDeliverAddress {

    /**
     * 地址 Code 只能识别区级别地址代码
     */
    private String divisionCode;

    /**
     * 收件人名称
     */
    private String fullName;

    /**
     * 收件人手机号
     */
    private String mobile;

    /**
     * 收件人详细地址
     */
    private String addressDetail;
}
