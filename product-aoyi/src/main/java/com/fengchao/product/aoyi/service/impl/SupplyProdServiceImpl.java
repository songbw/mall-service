package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.service.SupplyProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author songbw
 * @date 2020/12/28 16:08
 */
@Service
public class SupplyProdServiceImpl implements SupplyProdService {

    private final ProductDao productDao ;

    @Autowired
    public SupplyProdServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public OperaResponse findProductPageable(ProductQueryBean queryBean) {
        OperaResponse response = new OperaResponse();
        response.setData(productDao.selectPageable(queryBean));
        return response;
    }
}
