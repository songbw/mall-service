package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<StarCodeBean> codeInvList = new ArrayList<>();
    private String regionId ;
}
