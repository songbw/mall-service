package com.fengchao.freight.model;

public class ShippingRegionsX {
    private Integer id;

    private Integer templateId;

    private Integer basePrice;

    private Integer baseAmount;

    private Integer cumulativePrice;

    private Integer cumulativeUnit;

    private String name;

    private String provinces;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Integer baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Integer getCumulativePrice() {
        return cumulativePrice;
    }

    public void setCumulativePrice(Integer cumulativePrice) {
        this.cumulativePrice = cumulativePrice;
    }

    public Integer getCumulativeUnit() {
        return cumulativeUnit;
    }

    public void setCumulativeUnit(Integer cumulativeUnit) {
        this.cumulativeUnit = cumulativeUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces == null ? null : provinces.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}