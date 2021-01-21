package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.StarInventoryBean;
import com.fengchao.product.aoyi.bean.supply.InventoryResponse;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;
import com.fengchao.product.aoyi.bean.supply.SupplyInventoryBean;
import com.fengchao.product.aoyi.bean.supply.SupplyPriceBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.service.SupplyProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songbw
 * @date 2020/12/28 16:08
 */
@Service
public class SupplyProdServiceImpl implements SupplyProdService {

    private final ProductDao productDao ;
    private final StarSkuDao starSkuDao ;
    private final AoyiClientRpcService aoyiClientRpcService ;

    @Autowired
    public SupplyProdServiceImpl(ProductDao productDao, StarSkuDao starSkuDao, AoyiClientRpcService aoyiClientRpcService) {
        this.productDao = productDao;
        this.starSkuDao = starSkuDao;
        this.aoyiClientRpcService = aoyiClientRpcService;
    }

    @Override
    public OperaResponse findProductPageable(ProductQueryBean queryBean) {
        OperaResponse response = new OperaResponse();
        if (queryBean.getPageSize() > 200) {
            queryBean.setPageSize(200);
        }
        queryBean.setState("1");
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
        List<SupplyPriceBean> supplyBeanList = new ArrayList<>() ;
        if (prodIndexWithBLOBs != null && prodIndexWithBLOBs.size() > 0) {
            prodIndexWithBLOBs.forEach(prod -> {
                SupplyPriceBean supplyBean = new SupplyPriceBean() ;
                supplyBean.setSpuId(prod.getMpu());
                supplyBean.setSkuId(prod.getMpu());
                supplyBean.setPrice(prod.getSprice());
                supplyBean.setAdvisePrice(prod.getPrice());
                supplyBeanList.add(supplyBean) ;
            });
        }

        if (starSkus != null && starSkus.size() > 0) {
            starSkus.forEach(starSku -> {
                SupplyPriceBean supplyBean = new SupplyPriceBean() ;
                supplyBean.setSpuId(starSku.getSpuId());
                supplyBean.setSkuId(starSku.getCode());
                supplyBean.setPrice(new BigDecimal(starSku.getSprice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
                supplyBean.setAdvisePrice(new BigDecimal(starSku.getAdvisePrice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
                supplyBeanList.add(supplyBean) ;
            });
        }

        response.setData(supplyBeanList);
        return response;
    }

    @Override
    public OperaResponse batchFindSkuInventory(SupplyInventoryBean inventoryBean) {
        OperaResponse response = new OperaResponse() ;
        if (inventoryBean.getList() == null) {
            return response ;
        }
        if (inventoryBean.getList().size() > 50) {
            response.setCode(2000001);
            response.setMsg("列表数量 必须小于50");
            return response;
        }
        List<InventoryResponse> list = new ArrayList<>() ;
        List<AoyiProdIndexWithBLOBs> prodIndexWithBLOBs = productDao.selectProdBySpuIds(inventoryBean.getList()) ;
        prodIndexWithBLOBs.forEach(prod -> {
            InventoryResponse inventoryResponse = new InventoryResponse() ;
            inventoryResponse.setSpuId(prod.getMpu());
            inventoryResponse.setSkuId(prod.getMpu());
            inventoryResponse.setInventory(prod.getInventory().toString());
            inventoryResponse.setStatus(prod.getState());
            list.add(inventoryResponse) ;
        });
        List<StarInventoryBean> starInventoryBeans = aoyiClientRpcService.getStarInventory(inventoryBean);
        starInventoryBeans.forEach(starInventoryBean -> {
            InventoryResponse inventoryResponse = new InventoryResponse() ;
            inventoryResponse.setSpuId(starInventoryBean.getSkuId());
            inventoryResponse.setSkuId(starInventoryBean.getCode());
            inventoryResponse.setInventory(starInventoryBean.getInventoryCount());
            inventoryResponse.setStatus(starInventoryBean.getStatus());
            list.add(inventoryResponse) ;
        });
        response.setData(list);
        return response;
    }
}
