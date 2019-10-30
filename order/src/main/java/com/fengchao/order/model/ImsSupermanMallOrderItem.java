package com.fengchao.order.model;

import java.math.BigDecimal;

public class ImsSupermanMallOrderItem {
    private Integer id;

    private Integer orderid;

    private Integer itemid;

    private Byte type;

    private String title;

    private String number;

    private String cover;

    private Integer total;

    private BigDecimal price;

    private Byte special;

    private Integer skuid;

    private String unit;

    private Byte iscomment;

    private String barcode;

    private Byte serviceType;

    private BigDecimal partner3Commission;

    private BigDecimal partner2Commission;

    private BigDecimal partner1Commission;

    private String tax;

    private BigDecimal taxPrice;

    private BigDecimal nakedPrice;

    private String jdOrderId;

    private String cJdOrderId;

    private Boolean jdConfim;

    private Boolean isConfim;

    private Integer finishTime;

    private Boolean source;

    private Integer thirdSkuId;

    private String thirdPackageId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
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

    public Byte getSpecial() {
        return special;
    }

    public void setSpecial(Byte special) {
        this.special = special;
    }

    public Integer getSkuid() {
        return skuid;
    }

    public void setSkuid(Integer skuid) {
        this.skuid = skuid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Byte getIscomment() {
        return iscomment;
    }

    public void setIscomment(Byte iscomment) {
        this.iscomment = iscomment;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
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

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax == null ? null : tax.trim();
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getNakedPrice() {
        return nakedPrice;
    }

    public void setNakedPrice(BigDecimal nakedPrice) {
        this.nakedPrice = nakedPrice;
    }

    public String getJdOrderId() {
        return jdOrderId;
    }

    public void setJdOrderId(String jdOrderId) {
        this.jdOrderId = jdOrderId == null ? null : jdOrderId.trim();
    }

    public String getcJdOrderId() {
        return cJdOrderId;
    }

    public void setcJdOrderId(String cJdOrderId) {
        this.cJdOrderId = cJdOrderId == null ? null : cJdOrderId.trim();
    }

    public Boolean getJdConfim() {
        return jdConfim;
    }

    public void setJdConfim(Boolean jdConfim) {
        this.jdConfim = jdConfim;
    }

    public Boolean getIsConfim() {
        return isConfim;
    }

    public void setIsConfim(Boolean isConfim) {
        this.isConfim = isConfim;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getSource() {
        return source;
    }

    public void setSource(Boolean source) {
        this.source = source;
    }

    public Integer getThirdSkuId() {
        return thirdSkuId;
    }

    public void setThirdSkuId(Integer thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }

    public String getThirdPackageId() {
        return thirdPackageId;
    }

    public void setThirdPackageId(String thirdPackageId) {
        this.thirdPackageId = thirdPackageId == null ? null : thirdPackageId.trim();
    }
}