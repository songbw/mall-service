package com.fengchao.order.rpc.extmodel.weipinhui;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AoyiQueryInventoryResDto {

    private String itemId;

    private String skuId;

    private Boolean inventoy;
}
