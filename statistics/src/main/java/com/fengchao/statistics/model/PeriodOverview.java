package com.fengchao.statistics.model;

public class PeriodOverview {
    private Long id;

    private Integer lateAtNight;

    private Integer earlyMorning;

    private Integer morning;

    private Integer noon;

    private Integer afternoon;

    private Integer night;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLateAtNight() {
        return lateAtNight;
    }

    public void setLateAtNight(Integer lateAtNight) {
        this.lateAtNight = lateAtNight;
    }

    public Integer getEarlyMorning() {
        return earlyMorning;
    }

    public void setEarlyMorning(Integer earlyMorning) {
        this.earlyMorning = earlyMorning;
    }

    public Integer getMorning() {
        return morning;
    }

    public void setMorning(Integer morning) {
        this.morning = morning;
    }

    public Integer getNoon() {
        return noon;
    }

    public void setNoon(Integer noon) {
        this.noon = noon;
    }

    public Integer getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Integer afternoon) {
        this.afternoon = afternoon;
    }

    public Integer getNight() {
        return night;
    }

    public void setNight(Integer night) {
        this.night = night;
    }
}