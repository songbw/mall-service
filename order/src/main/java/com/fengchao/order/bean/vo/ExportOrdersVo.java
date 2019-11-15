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
 *     // 平台分润比， 收件人名， 省 ， 市， 区, 收件人手机号
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
     * 结算类型 : 结算类型（0：普通类结算， 1：秒杀类结算， 2：精品类结算）
     */
    private String settlementType;

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
     * sku实际支付价格 单位:分
     */
    private Integer skuPayPrice;

    /**
     * 券支付金额 单位分
     */
    private Integer couponPrice;

    /**
     * 主订单实际支付的价格 单位分
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

    /**
     * 运费
     */
    private String expressFee;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 供应商名称
     */
    private String merchantName;

    /**
     * 供应商id
     */
    private Long merchantId;

    /**
     * sku 优惠券使用金额 单位:分
     */
    private Integer skuCouponDiscount;

    /**
     * 0：待付款；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：已取消，申请售后
     */
    private String orderDetailStatus;

    /**
     * 余额支付金额 单位 元
     */
    private String balanceFee;

    /**
     * 惠民卡支付金额 单位 元
     */
    private String huiminCardFee;

    /**
     * 联机账户支付 单位 元
     */
    private String woaFee;

    /**
     * 快捷支付 单位 元
     */
    private String quickPayFee;

    /**
     * 子订单退款金额 单位 元
     */
    private String orderDetailRefundAmount;

    /**
     * 余额退款金额 单位 元
     */
    private String balanceRefund;

    /**
     * 惠民卡退款金额 单位 元
     */
    private String huiminCardRefund;

    /**
     * 联机账户退款 单位 元
     */
    private String woaRefund;

    /**
     * 快捷退款 单位 元
     */
    private String quickPayRefund;

    /**
     * 收件人手机号
     */
    private String mobile;

    /**
     * 支付订单号
     */
    private String paymentNo;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 奥弋ID
     */
    private String aoyiID;


}
