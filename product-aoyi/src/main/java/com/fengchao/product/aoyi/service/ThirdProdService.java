package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.AyFcImages;

import java.util.List;

public interface ThirdProdService {

    OperaResult add(AoyiProdIndexX bean);

    OperaResult update(AoyiProdIndexX bean);

    OperaResult insertOrUpdateByMpu(AoyiProdIndex bean);

    void updatePrice(PriceBean bean);

    OperaResponse updateState(StateBean bean);

    void delete(Integer merchantId, Integer id) throws ProductException;

    void uploadProdImage() ;

    OperaResponse sync(ThirdSyncBean bean) ;

    OperaResponse syncCategory(CategorySyncBean bean) ;

    OperaResponse syncBrand(ThirdSyncBean bean) ;

    OperaResponse updateAyFcImageStatus(Long id, Integer status) ;

    OperaResponse syncStarProd() ;

    OperaResponse syncStarProdPrice() ;
}
