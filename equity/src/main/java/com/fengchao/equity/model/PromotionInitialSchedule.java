package com.fengchao.equity.model;

public class PromotionInitialSchedule {
    private Integer id;

    private String initialSchedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInitialSchedule() {
        return initialSchedule;
    }

    public void setInitialSchedule(String initialSchedule) {
        this.initialSchedule = initialSchedule == null ? null : initialSchedule.trim();
    }
}