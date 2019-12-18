package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSPostBean {

    private String phone;

    /**
     * 短信模版id
     */
    private Integer templateId;

    /**
     * 参数
     */
    private String[] params;
}
