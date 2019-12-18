package com.fengchao.product.aoyi.model;

import java.util.Date;

public class AyFcImages {
    private Long id;

    private String fcImage;

    private String ayImage;

    private String path;

    private String type;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFcImage() {
        return fcImage;
    }

    public void setFcImage(String fcImage) {
        this.fcImage = fcImage == null ? null : fcImage.trim();
    }

    public String getAyImage() {
        return ayImage;
    }

    public void setAyImage(String ayImage) {
        this.ayImage = ayImage == null ? null : ayImage.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}