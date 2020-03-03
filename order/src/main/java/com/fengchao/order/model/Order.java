package com.fengchao.order.model;

import com.fengchao.order.bean.PageBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private Integer id;

    private String openId;

    private String tradeNo;

    private String aoyiId;

    private String companyCustNo;

    private String receiverName;

    private String telephone;

    private String mobile;

    private String email;

    private String provinceId;

    private String provinceName;

    private String cityId;

    private String cityName;

    private String countyId;

    private String countyName;

    private String townId;

    private String address;

    private String zip;

    private String remark;

    private String invoiceState;

    private String invoiceType;

    private String invoiceTitle;

    private String invoiceContent;

    private String taxNo;

    private String payment;

    private Float servFee;

    private Float amount;

    private Float saleAmount;

    private Integer status;

    private Integer type;

    private String outTradeNo;
    //支付系统订单号
    private String paymentNo;

    private Date paymentAt;
    //实际退款金额
    private Integer paymentAmount;
    // 商户号，充值钱包的时候没有
    private String payee ;
    // 退款金额，退款时候有
    private int refundFee;
    // 支付方式
    private String payType;
    // 订单总金额
    private int paymentTotalFee;
    // C端个人账号。 表示唯一用户
    private String payer;
    // 支付状态 10初始创建订单  1下单成功，等待支付。  2支付中，3超时未支付  4支付失败  5支付成功  11支付成功，记账也成功   12支付成功，记账失败  14退款失败，15订单已退款
    private int payStatus;
    // 1支付，2充值，3退款，4提现
    private int payOrderCategory ;

    private Date createdAt;

    private Date updatedAt;

    private String merchantNo;

    private Integer merchantId;

    private Integer couponId;

    private String couponCode;

    private Float couponDiscount;

    private List<OrderDetailX> skus;

    private PageBean skusPage;

    private Integer couponStatus;

    private Integer balanceId ;

    private Float balanceDiscount ;

    private String appId ;

    private String regionId;

    private List<OrderDetail> orderDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getAoyiId() {
        return aoyiId;
    }

    public void setAoyiId(String aoyiId) {
        this.aoyiId = aoyiId == null ? null : aoyiId.trim();
    }

    public String getCompanyCustNo() {
        return companyCustNo;
    }

    public void setCompanyCustNo(String companyCustNo) {
        this.companyCustNo = companyCustNo == null ? null : companyCustNo.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId == null ? null : provinceId.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId == null ? null : countyId.trim();
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId == null ? null : townId.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip == null ? null : zip.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(String invoiceState) {
        this.invoiceState = invoiceState == null ? null : invoiceState.trim();
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent == null ? null : invoiceContent.trim();
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }

    public Float getServFee() {
        return servFee;
    }

    public void setServFee(Float servFee) {
        this.servFee = servFee;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderDetailX> getSkus() {
        return skus;
    }

    public void setSkus(List<OrderDetailX> skus) {
        this.skus = skus;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentAt() {
        return paymentAt;
    }

    public void setPaymentAt(Date paymentAt) {
        this.paymentAt = paymentAt;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getPaymentTotalFee() {
        return paymentTotalFee;
    }

    public void setPaymentTotalFee(int paymentTotalFee) {
        this.paymentTotalFee = paymentTotalFee;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayOrderCategory() {
        return payOrderCategory;
    }

    public void setPayOrderCategory(int payOrderCategory) {
        this.payOrderCategory = payOrderCategory;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Float getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Float saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Float getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Float couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public PageBean getSkusPage() {
        return skusPage;
    }

    public void setSkusPage(PageBean skusPage) {
        this.skusPage = skusPage;
    }

    public Integer getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Integer getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    public Float getBalanceDiscount() {
        return balanceDiscount;
    }

    public void setBalanceDiscount(Float balanceDiscount) {
        this.balanceDiscount = balanceDiscount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}