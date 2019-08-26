package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;

public interface ThirdProdService {

    int add(AoyiProdIndexX bean) throws ProductException;

    int update(AoyiProdIndexX bean) throws ProductException;

    void updatePrice(PriceBean bean);

    void updateState(StateBean bean);

    void delete(Integer merchantId, Integer id) throws ProductException;
}
