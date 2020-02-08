package com.fengchao.aoyi.client.starBean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songbw
 * @date 2020/1/3 16:23
 */
@Setter
@Getter
public class StarOrderBean {
    private String outOrderNo ;
//    private String receiverAreaId ;
//    private String receiverAreaName ;
    private String regionId ;
    private String receiverAddr ;
    private String receiver ;
    private String receiverPhone ;
    private String receiverMobile ;
    private String freight ;
    private String buyerRemark ;
    private String sellerRemark ;
    private String idcardName ;
    private String idcardNo ;
    private String idcardFirstUrl ;
    private String idcardSecondUrl ;
    private List<StarCodeBean> skuList = new ArrayList<>();
}
