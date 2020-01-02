package com.fengchao.aoyi.client.starBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/2 15:31
 */
@Setter
@Getter
public class ReleaseSkuInventoryQueryBean {
    // 第三方订单号 <= 32位
    private String outOrderNo ;
    // 商品信息json, [{\"code\":\"SL-ECP-6072\"}]
    private String codeInvList ;
}
