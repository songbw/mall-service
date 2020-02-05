package com.fengchao.product.aoyi.starBean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<StarCodeBean> codeInvList = new ArrayList<>();
}
