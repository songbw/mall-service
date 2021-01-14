package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.service.SupplyProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public OperaResponse batchFindSkuPrice(List<SupplyBean> supplyBeans) {
        OperaResponse response = new OperaResponse() ;
        if (supplyBeans== null) {
            return response ;
        }
        if (supplyBeans.size() > 50) {
            response.setCode(2000001);
            response.setMsg("列表数量 必须小于50");
            return response;
        }
        List<AoyiProdIndexWithBLOBs> prodIndexWithBLOBs = productDao.selectProdBySpuIds(supplyBeans) ;
        List<StarSku> starSkus = starSkuDao.selectProdBySpuIds(supplyBeans) ;
        List<SupplyBean> supplyBeanList = new ArrayList<>() ;
        prodIndexWithBLOBs.forEach(prod -> {
            SupplyBean supplyBean = new SupplyBean() ;
            supplyBean.setSpuId(prod.getMpu());
            supplyBean.setSkuId(prod.getMpu());
            supplyBean.setPrice(prod.getSprice());
            supplyBean.setAdvisePrice(prod.getPrice());
            supplyBeanList.add(supplyBean) ;
        });
        starSkus.forEach(starSku -> {
            SupplyBean supplyBean = new SupplyBean() ;
            supplyBean.setSpuId(starSku.getSpuId());
            supplyBean.setSkuId(starSku.getCode());
            supplyBean.setPrice(new BigDecimal(starSku.getSprice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
            supplyBean.setAdvisePrice(new BigDecimal(starSku.getAdvisePrice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
            supplyBeanList.add(supplyBean) ;
        });
        response.setData(supplyBeanList);
        return response;
    }

    @Override
    public OperaResponse batchFindSkuInventory(List<SupplyBean> supplyBeans) {
        OperaResponse response = new OperaResponse() ;
        if (supplyBeans== null) {
            return response ;
        }
        if (supplyBeans.size() > 50) {
            response.setCode(2000001);
            response.setMsg("列表数量 必须小于50");
            return response;
        }
        List<AoyiProdIndexWithBLOBs> prodIndexWithBLOBs = productDao.selectProdBySpuIds(supplyBeans) ;
        List<StarSku> starSkus = starSkuDao.selectProdBySpuIds(supplyBeans) ;
        return null;
    }
}
