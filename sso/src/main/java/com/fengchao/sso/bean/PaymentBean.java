package com.fengchao.sso.bean;

import java.io.Serializable;

public class PaymentBean implements Serializable {
    private String openId;
    private String iAppId;   // 凤巢APPID
    private String tAppId;   // 第三方APPID
    private String appId;   // 平安支付凤巢APPID
    private String merchantNo;
    private String orderNos;
    private String goodsName;
    private int amount;
    private String returnUrl;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getiAppId() {
        return iAppId;
    }

    public void setiAppId(String iAppId) {
        this.iAppId = iAppId;
    }

    public String gettAppId() {
        return tAppId;
    }

    public void settAppId(String tAppId) {
        this.tAppId = tAppId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(String orderNos) {
        this.orderNos = orderNos;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
