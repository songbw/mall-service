package com.fengchao.order.rpc.extmodel.weipinhui;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiConfirmOrderRequest extends BaseRquestParam {

    /**
     * 外部主订单号
     */
    private String orderNo;
}
