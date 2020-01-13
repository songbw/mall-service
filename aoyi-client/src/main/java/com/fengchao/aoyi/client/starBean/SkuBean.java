package com.fengchao.aoyi.client.starBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2020/1/13 16:26
 */
@Setter
@Getter
public class SkuBean {
    // 商品编码
    private String code ;
    //商品logo  【如果没有取spu主图】
    private String goodsLogo ;
    //起购数量
    private Integer purchaseQty ;
    //商品skuid
    private String skuId ;
    private List<SpuPropertyBean> skuPropertyList ;
    //商品状态：0、已上架1、已下架
    private Integer status ;
}
