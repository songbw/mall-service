package com.fengchao.aoyi.client.bean.dto.weipinhui;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiReleaseOrderRequest extends BaseRquestParam {

    /**
     * 外部主订单号
     */
    private String orderNo;
}
