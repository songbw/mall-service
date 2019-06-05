package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.SerachBean;

public interface AdminProdService {
    PageBean findProdList(Integer offset, Integer limit, String state);

    PageBean selectNameList(SerachBean bean);

    int getProdListToRedis();
}
