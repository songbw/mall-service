package com.fengchao.product.aoyi.model;

public class AoyiBaseFulladdress {
    private Integer aid;

    private String id;

    private String pid;

    private String level;

    private String name;

    private String secondpid;

    private String zipcode;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSecondpid() {
        return secondpid;
    }

    public void setSecondpid(String secondpid) {
        this.secondpid = secondpid == null ? null : secondpid.trim();
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode == null ? null : zipcode.trim();
    }
}