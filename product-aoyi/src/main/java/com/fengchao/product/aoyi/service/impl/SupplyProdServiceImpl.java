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
        if (queryBean.getPageSize() > 200) {
            queryBean.setPageSize(200);
        }
        response.setData(productDao.selectPageable(queryBean));
        return response;
    }

    @Override
    public OperaResponse batchFindSkuBySpu(ProductQueryBean queryBean) {
        OperaResponse response = new OperaResponse() ;
        if (queryBean.getSpus() == null) {
            return response ;
        }
        if (queryBean.getSpus().size() > 200) {
            response.setCode(2000001);
            response.setMsg("spus 必须小于200");
            return response;
        }
        List<StarSku> starSkus = starSkuDao.selectBySpuIds(queryBean.getSpus(), 1) ;
        response.setData(starSkus);
        return response;
    }
}
