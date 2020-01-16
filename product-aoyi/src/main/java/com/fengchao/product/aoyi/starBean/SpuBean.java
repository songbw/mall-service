package com.fengchao.product.aoyi.starBean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2020/1/13 15:42
 */
@Getter
@Setter
public class SpuBean {
    //品牌ID
    private Integer brandId ;
    //商品三级分类ID
    private Integer categoryId ;
    //是否海淘商品：0、境内,1、境外海淘
    private Integer crossBorder ;
    //预览图地址列表,   商品轮播图
    private List<String> detailImgUrlList ;
    //图文说明。仅详情页输出
    private String detailInfo ;
    private String goodsAreaInfo ;
    //是否限制销售范围 0:无限制 1:有限制
    private String goodsAreaKbn ;
    private String goodsCode ;
    //是否限制ID购买数量: 0-不限制 ,1-限制
    private Integer goodsIslimit ;
    //每个ID限购数量,空表示不限购
    private Integer goodsLimitNum ;
    //SPU  主图
    private String mainImgUrl ;
    //商品名称
    private String name ;
    private String simpleDesc ;
    //商品spuID
    private String spuId ;
    //商品属性
    private List<SpuPropertyBean> spuPropertyList ;
    //商品状态：0、已上架,1、已下架
    private Integer status ;
    //是否包邮：商品运费：0为买家承担,1为卖家承担
    private Integer transFee ;
    //视频地址
    private String videoUrl ;
}
