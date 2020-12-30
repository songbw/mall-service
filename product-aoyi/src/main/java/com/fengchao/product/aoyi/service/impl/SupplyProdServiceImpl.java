package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.service.SupplyProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/28 16:08
 */
@Service
public class SupplyProdServiceImpl implements SupplyProdService {

    private final ProductDao productDao ;
    private final StarSkuDao starSkuDao ;

    @Autowired
    public SupplyProdServiceImpl(ProductDao productDao, StarSkuDao starSkuDao) {
        this.productDao = productDao;
        this.starSkuDao = starSkuDao;
    }

    @Override
    public OperaResponse findProductPageable(ProductQueryBean queryBean) {
        OperaResponse response = new OperaResponse();
        response.setData(productDao.selectPageable(queryBean));
        return response;
    }

    @Override
    public OperaResponse batchFindSkuBySpu(ProductQueryBean queryBean) {
        List<StarSku> starSkus = starSkuDao.selectBySpuIds(queryBean.getSpus(), 1) ;
        return null;
    }
}
