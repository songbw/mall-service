package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author tom
 * @Date 19-7-17 下午4:05
 *
 * 字段不限于：
 *      // 用户id，主订单编号，子订单编号， 订单支付时间， 订单生成时间，品类， 品牌（通过mpu获取），
 *     // sku， mpu， 商品名称， 购买数量 ， 活动 ， 券码， 券来源（券商户），进货价， 销售价，  券支付金额， 订单支付金额，
 *     // 平台分润比， 收件人名， 省 ， 市， 区
 */
@Getter
@Setter
public class ExportOrdersVo {

    /**
     * 用户id
     */
    private String openId;

    /**
     * 主订单编号
     */
    private String tradeNo;

    /**
     * 子订单编号
     */
    private String subOrderId;

    /**
     * 订单支付时间
     */
    private Date paymentTime;

    /**
     * 订单生成时间
     */
    private Date createTime;

    /**
     * 品类
     */
    private String category;

    /**
     * 品牌
     */
    private String brand;

    private String sku;

    private String mpu;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 活动
     */
    private String promotion;

    /**
     * 活动id
     */
    private Long promotionId;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 券码id
     */
    private Long couponId;

    /**
     * 券来源（券商户）
     */
    private String couponSupplier;

    /**
     * 进货价格 单位分
     */
    private Integer purchasePrice;

    /**
     * sku单价 单位：分
     * 计算：商品单价-去除 活动 的价格
     */
    private Integer unitPrice;

    /**
     * sku 的总价 单位：分
     * 计算：unitPrice * quantity(购买数量)
     */
    private Integer totalRealPrice;

    /**
     * 券支付金额 单位分
     */
    private Integer couponPrice;

    /**
     * 实际支付的价格 单位分
     * 计算：totalRealPrice - couponPrice
     */
    private Integer payPrice;

    /**
     * 平台分润比
     */
    private String shareBenefitPercent;

    /**
     * 收件人名
     */
    private String buyerName;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区
     */
    private String countyName;


}
