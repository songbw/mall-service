package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;

public interface ThirdProdService {

    OperaResult add(AoyiProdIndexX bean);

    OperaResult update(AoyiProdIndexX bean);

    OperaResult insertOrUpdateByMpu(AoyiProdIndex bean);

    void updatePrice(PriceBean bean);

    void updateState(StateBean bean);

    void delete(Integer merchantId, Integer id) throws ProductException;

    void uploadProdImage() ;

    OperaResponse sync(ThirdSyncBean bean) ;
}
