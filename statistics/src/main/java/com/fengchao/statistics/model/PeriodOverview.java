package com.fengchao.statistics.model;

public class PeriodOverview {
    private Long id;

    private float lateAtNight;

    private float earlyMorning;

    private float morning;

    private float noon;

    private float afternoon;

    private float night;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getLateAtNight() {
        return lateAtNight;
    }

    public void setLateAtNight(float lateAtNight) {
        this.lateAtNight = lateAtNight;
    }

    public float getEarlyMorning() {
        return earlyMorning;
    }

    public void setEarlyMorning(float earlyMorning) {
        this.earlyMorning = earlyMorning;
    }

    public float getMorning() {
        return morning;
    }

    public void setMorning(float morning) {
        this.morning = morning;
    }

    public float getNoon() {
        return noon;
    }

    public void setNoon(float noon) {
        this.noon = noon;
    }

    public float getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(float afternoon) {
        this.afternoon = afternoon;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }
}