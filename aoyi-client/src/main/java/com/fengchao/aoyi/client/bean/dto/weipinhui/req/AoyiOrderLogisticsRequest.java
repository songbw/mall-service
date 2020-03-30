package com.fengchao.aoyi.client.bean.dto.weipinhui.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiOrderLogisticsRequest extends BaseRquestParam {

    /**
     * 子订单号
     */
    private String subOrderNo;
}
