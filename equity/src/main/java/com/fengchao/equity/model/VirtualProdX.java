package com.fengchao.equity.model;

import java.math.BigDecimal;
import java.util.List;

public class VirtualProdX {
    private Integer id;

    private String mpu;

    private Integer effectiveDays;

    private BigDecimal parValue;

    private List<VirtualTickets> tickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu == null ? null : mpu.trim();
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public List<VirtualTickets> getTickets() {
        return tickets;
    }

    public void setTickets(List<VirtualTickets> tickets) {
        this.tickets = tickets;
    }
}