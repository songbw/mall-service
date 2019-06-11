package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.SerachBean;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;

public interface AdminProdService {
    PageBean findProdList(Integer offset, Integer limit, String state, Integer merchantId);

    PageBean selectNameList(SerachBean bean);

    int getProdListToRedis();

    int add(AoyiProdIndex bean) throws ProductException;

    int update(AoyiProdIndex bean) throws ProductException;

    void delete(Integer merchantId, Integer id) throws ProductException;
}
