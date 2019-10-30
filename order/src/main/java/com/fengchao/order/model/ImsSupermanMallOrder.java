package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImsSupermanMallOrder {
    private Integer id;

    private Integer pid;

    private Integer uniacid;

    private Integer shopid;

    private Integer uid;

    private String ordersn;

    private Integer partner3Id;

    private Integer partner2Id;

    private Integer partner1Id;

    private Integer total;

    private BigDecimal price;

    private BigDecimal payCredit;

    private Byte payType;

    private String paymentNo;

    private Integer payTime;

    private String remark;

    private String adminRemark;

    private String username;

    private String mobile;

    private String address;

    private String zipcode;

    private String expressTitle;

    private String expressAlias;

    private String expressNo;

    private BigDecimal expressFee;

    private String creditType;

    private BigDecimal rewardCredit;

    private BigDecimal cashCredit;

    private Byte dispatchType;

    private String customDelivery;

    private Byte status;

    private Byte type;

    private Byte commissionStatus;

    private Byte checkout;

    private Byte wxpayServiceProvider;

    private Integer payuRid;

    private Byte settlementStatus;

    private BigDecimal partner3Commission;

    private BigDecimal partner2Commission;

    private BigDecimal partner1Commission;

    private Integer createtime;

    private Integer updatetime;

    private String jdOrderId;

    private Integer fromJd;

    private String jdAddress;

    private String jdAddressDesc;

    private BigDecimal freight;

    private BigDecimal jdTotalPrice;

    private BigDecimal jdTotalTax;

    private Boolean jdConfim;

    private Integer confimTime;

    private Boolean isSplite;

    private Boolean source;

    private String thirdNo;

    private String thirdAddress;

    private BigDecimal thirdRealPrice;

    private BigDecimal thirdExpFee;

    private String thirdPackageId;

    private Boolean thirdApi1;

    private Boolean thirdApi3;

    private Boolean thirdApi4;

    private Date thirdUpdateDate;

    private String expCreateTime;

    private BigDecimal creditrefund;

    private BigDecimal cardrefund;

    private BigDecimal onlinerefund;

    private BigDecimal wechatrefund;

    private BigDecimal alipayrefund;

    private Integer isrefund;

    private String extend;

    private List<ImsSupermanMallOrderItem> itemList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getUniacid() {
        return uniacid;
    }

    public void setUniacid(Integer uniacid) {
        this.uniacid = uniacid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn == null ? null : ordersn.trim();
    }

    public Integer getPartner3Id() {
        return partner3Id;
    }

    public void setPartner3Id(Integer partner3Id) {
        this.partner3Id = partner3Id;
    }

    public Integer getPartner2Id() {
        return partner2Id;
    }

    public void setPartner2Id(Integer partner2Id) {
        this.partner2Id = partner2Id;
    }

    public Integer getPartner1Id() {
        return partner1Id;
    }

    public void setPartner1Id(Integer partner1Id) {
        this.partner1Id = partner1Id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPayCredit() {
        return payCredit;
    }

    public void setPayCredit(BigDecimal payCredit) {
        this.payCredit = payCredit;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo == null ? null : paymentNo.trim();
    }

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark == null ? null : adminRemark.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode == null ? null : zipcode.trim();
    }

    public String getExpressTitle() {
        return expressTitle;
    }

    public void setExpressTitle(String expressTitle) {
        this.expressTitle = expressTitle == null ? null : expressTitle.trim();
    }

    public String getExpressAlias() {
        return expressAlias;
    }

    public void setExpressAlias(String expressAlias) {
        this.expressAlias = expressAlias == null ? null : expressAlias.trim();
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo == null ? null : expressNo.trim();
    }

    public BigDecimal getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(BigDecimal expressFee) {
        this.expressFee = expressFee;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType == null ? null : creditType.trim();
    }

    public BigDecimal getRewardCredit() {
        return rewardCredit;
    }

    public void setRewardCredit(BigDecimal rewardCredit) {
        this.rewardCredit = rewardCredit;
    }

    public BigDecimal getCashCredit() {
        return cashCredit;
    }

    public void setCashCredit(BigDecimal cashCredit) {
        this.cashCredit = cashCredit;
    }

    public Byte getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(Byte dispatchType) {
        this.dispatchType = dispatchType;
    }

    public String getCustomDelivery() {
        return customDelivery;
    }

    public void setCustomDelivery(String customDelivery) {
        this.customDelivery = customDelivery == null ? null : customDelivery.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getCommissionStatus() {
        return commissionStatus;
    }

    public void setCommissionStatus(Byte commissionStatus) {
        this.commissionStatus = commissionStatus;
    }

    public Byte getCheckout() {
        return checkout;
    }

    public void setCheckout(Byte checkout) {
        this.checkout = checkout;
    }

    public Byte getWxpayServiceProvider() {
        return wxpayServiceProvider;
    }

    public void setWxpayServiceProvider(Byte wxpayServiceProvider) {
        this.wxpayServiceProvider = wxpayServiceProvider;
    }

    public Integer getPayuRid() {
        return payuRid;
    }

    public void setPayuRid(Integer payuRid) {
        this.payuRid = payuRid;
    }

    public Byte getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(Byte settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public BigDecimal getPartner3Commission() {
        return partner3Commission;
    }

    public void setPartner3Commission(BigDecimal partner3Commission) {
        this.partner3Commission = partner3Commission;
    }

    public BigDecimal getPartner2Commission() {
        return partner2Commission;
    }

    public void setPartner2Commission(BigDecimal partner2Commission) {
        this.partner2Commission = partner2Commission;
    }

    public BigDecimal getPartner1Commission() {
        return partner1Commission;
    }

    public void setPartner1Commission(BigDecimal partner1Commission) {
        this.partner1Commission = partner1Commission;
    }

    public Integer getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    public Integer getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    public String getJdOrderId() {
        return jdOrderId;
    }

    public void setJdOrderId(String jdOrderId) {
        this.jdOrderId = jdOrderId == null ? null : jdOrderId.trim();
    }

    public Integer getFromJd() {
        return fromJd;
    }

    public void setFromJd(Integer fromJd) {
        this.fromJd = fromJd;
    }

    public String getJdAddress() {
        return jdAddress;
    }

    public void setJdAddress(String jdAddress) {
        this.jdAddress = jdAddress == null ? null : jdAddress.trim();
    }

    public String getJdAddressDesc() {
        return jdAddressDesc;
    }

    public void setJdAddressDesc(String jdAddressDesc) {
        this.jdAddressDesc = jdAddressDesc == null ? null : jdAddressDesc.trim();
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getJdTotalPrice() {
        return jdTotalPrice;
    }

    public void setJdTotalPrice(BigDecimal jdTotalPrice) {
        this.jdTotalPrice = jdTotalPrice;
    }

    public BigDecimal getJdTotalTax() {
        return jdTotalTax;
    }

    public void setJdTotalTax(BigDecimal jdTotalTax) {
        this.jdTotalTax = jdTotalTax;
    }

    public Boolean getJdConfim() {
        return jdConfim;
    }

    public void setJdConfim(Boolean jdConfim) {
        this.jdConfim = jdConfim;
    }

    public Integer getConfimTime() {
        return confimTime;
    }

    public void setConfimTime(Integer confimTime) {
        this.confimTime = confimTime;
    }

    public Boolean getIsSplite() {
        return isSplite;
    }

    public void setIsSplite(Boolean isSplite) {
        this.isSplite = isSplite;
    }

    public Boolean getSource() {
        return source;
    }

    public void setSource(Boolean source) {
        this.source = source;
    }

    public String getThirdNo() {
        return thirdNo;
    }

    public void setThirdNo(String thirdNo) {
        this.thirdNo = thirdNo == null ? null : thirdNo.trim();
    }

    public String getThirdAddress() {
        return thirdAddress;
    }

    public void setThirdAddress(String thirdAddress) {
        this.thirdAddress = thirdAddress == null ? null : thirdAddress.trim();
    }

    public BigDecimal getThirdRealPrice() {
        return thirdRealPrice;
    }

    public void setThirdRealPrice(BigDecimal thirdRealPrice) {
        this.thirdRealPrice = thirdRealPrice;
    }

    public BigDecimal getThirdExpFee() {
        return thirdExpFee;
    }

    public void setThirdExpFee(BigDecimal thirdExpFee) {
        this.thirdExpFee = thirdExpFee;
    }

    public String getThirdPackageId() {
        return thirdPackageId;
    }

    public void setThirdPackageId(String thirdPackageId) {
        this.thirdPackageId = thirdPackageId == null ? null : thirdPackageId.trim();
    }

    public Boolean getThirdApi1() {
        return thirdApi1;
    }

    public void setThirdApi1(Boolean thirdApi1) {
        this.thirdApi1 = thirdApi1;
    }

    public Boolean getThirdApi3() {
        return thirdApi3;
    }

    public void setThirdApi3(Boolean thirdApi3) {
        this.thirdApi3 = thirdApi3;
    }

    public Boolean getThirdApi4() {
        return thirdApi4;
    }

    public void setThirdApi4(Boolean thirdApi4) {
        this.thirdApi4 = thirdApi4;
    }

    public Date getThirdUpdateDate() {
        return thirdUpdateDate;
    }

    public void setThirdUpdateDate(Date thirdUpdateDate) {
        this.thirdUpdateDate = thirdUpdateDate;
    }

    public String getExpCreateTime() {
        return expCreateTime;
    }

    public void setExpCreateTime(String expCreateTime) {
        this.expCreateTime = expCreateTime == null ? null : expCreateTime.trim();
    }

    public BigDecimal getCreditrefund() {
        return creditrefund;
    }

    public void setCreditrefund(BigDecimal creditrefund) {
        this.creditrefund = creditrefund;
    }

    public BigDecimal getCardrefund() {
        return cardrefund;
    }

    public void setCardrefund(BigDecimal cardrefund) {
        this.cardrefund = cardrefund;
    }

    public BigDecimal getOnlinerefund() {
        return onlinerefund;
    }

    public void setOnlinerefund(BigDecimal onlinerefund) {
        this.onlinerefund = onlinerefund;
    }

    public BigDecimal getWechatrefund() {
        return wechatrefund;
    }

    public void setWechatrefund(BigDecimal wechatrefund) {
        this.wechatrefund = wechatrefund;
    }

    public BigDecimal getAlipayrefund() {
        return alipayrefund;
    }

    public void setAlipayrefund(BigDecimal alipayrefund) {
        this.alipayrefund = alipayrefund;
    }

    public Integer getIsrefund() {
        return isrefund;
    }

    public void setIsrefund(Integer isrefund) {
        this.isrefund = isrefund;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
    }

    public List<ImsSupermanMallOrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ImsSupermanMallOrderItem> itemList) {
        this.itemList = itemList;
    }
}