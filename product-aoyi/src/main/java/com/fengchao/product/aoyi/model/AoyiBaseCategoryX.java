package com.fengchao.product.aoyi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AoyiBaseCategoryX implements Serializable {
    private Integer categoryId;

    private String categoryName;

    private Integer parentId;

    private String categoryClass;

    private String categoryIcon;

    private String categoryDesc;

    private Integer sortOrder;

    private Boolean isShow;

    private Boolean isNav;

    private Byte isTopStyle;

    private Date idate;

    private List<AoyiBaseCategoryX> subs;

    private Integer subTotal;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCategoryClass() {
        return categoryClass;
    }

    public void setCategoryClass(String categoryClass) {
        this.categoryClass = categoryClass == null ? null : categoryClass.trim();
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon == null ? null : categoryIcon.trim();
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc == null ? null : categoryDesc.trim();
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getIsNav() {
        return isNav;
    }

    public void setIsNav(Boolean isNav) {
        this.isNav = isNav;
    }

    public Byte getIsTopStyle() {
        return isTopStyle;
    }

    public void setIsTopStyle(Byte isTopStyle) {
        this.isTopStyle = isTopStyle;
    }

    public Date getIdate() {
        return idate;
    }

    public void setIdate(Date idate) {
        this.idate = idate ;
    }

    public List<AoyiBaseCategoryX> getSubs() {
        return subs;
    }

    public void setSubs(List<AoyiBaseCategoryX> subs) {
        this.subs = subs;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }
}