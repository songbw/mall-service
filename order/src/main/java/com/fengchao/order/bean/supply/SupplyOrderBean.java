package com.fengchao.order.bean.supply;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2021/1/18 16:09
 */
@Setter
@Getter
public class SupplyOrderBean {
    private String openId ;
    private String appId ;
    private String tradeNo ;
    private String receiverName ;
    private String mobile ;
    private String regionId ;
    private String address ;
    private String zip ;
    private String remark ;
    private String servFee ;
    private String amount ;
    private List<SupplySkuBean> skuList ;
}
