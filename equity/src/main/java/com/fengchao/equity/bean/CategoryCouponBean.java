package com.fengchao.equity.bean;

import com.fengchao.equity.model.CouponTags;
import lombok.Data;

import java.util.List;

@Data
public class CategoryCouponBean {

    private List<Category> categorys;
    private List<CouponTags> tags;
}