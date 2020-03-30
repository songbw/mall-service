package com.fengchao.aoyi.client.bean.dto.weipinhui.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiProductInventoryRequest extends BaseRquestParam {

    /**
     * 商品 itemid
     */
    private String itemId;

    /**
     * 商品 skuid
     */
    private String skuId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 地址 code
     */
    private String divisionCode;
}
