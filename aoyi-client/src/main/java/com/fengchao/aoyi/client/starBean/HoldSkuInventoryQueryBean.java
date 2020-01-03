package com.fengchao.aoyi.client.starBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/2 15:24
 */
@Setter
@Getter
public class HoldSkuInventoryQueryBean {
    // (弃用)省市区唯一ID, 使用英文逗号拼接
    private String areaId ;
    // 第三方订单号 <= 32位
    private String outOrderNo ;
    // 商品及库存信息json, 参考下面的demo
    private String codeInvList ;
}
