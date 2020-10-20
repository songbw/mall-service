package com.fengchao.aoyi.client.bean.dto.weipinhui.req;

import com.fengchao.aoyi.client.bean.dto.weipinhui.AoyiItem;
import com.fengchao.aoyi.client.bean.dto.weipinhui.AoyiDeliverAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AoyiRenderOrderRequest extends BaseRquestParam {

    private String orderNo;

    /**
     * 订单总金额(不 含运费) 单位元
     */
    private String amount;

    /**
     * 运费 单位元
     */
    private String freight;

    /**
     * 商品信息
     */
    private List<AoyiItem> items;

    /**
     * 订单地址信息
     */
    private AoyiDeliverAddress deliveryAddress;

    /**
     * 数据来源。智慧城市唯品会传参，ZhCityVip，关爱通：不填为null
     */
    private String sourceCode ;
}
