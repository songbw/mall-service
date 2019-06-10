package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.SkuCode;

public interface SkuCodeService {

    Integer add(SkuCode bean) throws ProductException;

    Integer update(SkuCode bean) throws ProductException;

    SkuCode findByMerchantId(Integer merchantId) throws ProductException;

    SkuCode find(Integer id) throws ProductException;

    Integer updateSkuValueAddById(SkuCode bean) throws ProductException;
}
