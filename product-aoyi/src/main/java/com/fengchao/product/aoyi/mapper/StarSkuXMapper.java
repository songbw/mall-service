package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.StarSkuBean;

import java.util.List;

public interface StarSkuXMapper {

    void batchInsert(List<StarSkuBean> starSkuList);
}