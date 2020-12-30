package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.EnterpriseSyncProductNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.EnterpriseSyncSpuDao;
import com.fengchao.product.aoyi.mapper.EnterpriseSpuMapper;
import com.fengchao.product.aoyi.model.EnterpriseSpu;
import com.fengchao.product.aoyi.service.EnterpriseSyncProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songbw
 * @date 2020/12/30 16:26
 */
@Service
public class EnterpriseSyncProductServiceImpl implements EnterpriseSyncProductService {

    private EnterpriseSpuMapper mapper ;
    private EnterpriseSyncSpuDao dao ;

    @Autowired
    public EnterpriseSyncProductServiceImpl(EnterpriseSpuMapper mapper, EnterpriseSyncSpuDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public OperaResponse notifyEnterpriseSpu(EnterpriseSyncProductNotifyBean notifyBean) {
        List<EnterpriseSpu> list = dao.batchFindBySpuIds(notifyBean.getSpus()) ;
        if (list != null && list.size() > 0) {
            list.forEach(enterpriseProduct -> {
                enterpriseProduct.setUpdatedAt(new Date());
                mapper.updateByPrimaryKeySelective(enterpriseProduct) ;
            });
        }
        List<String> updatedProductIds = list.stream().map(enterpriseProduct -> enterpriseProduct.getSpu()).collect(Collectors.toList());
        List<String> addBrandIds = notifyBean.getSpus().stream().filter(spu -> !updatedProductIds.contains(spu)).collect(Collectors.toList()) ;
        addBrandIds.parallelStream().forEach(ProductId -> {
            EnterpriseSpu Product = new EnterpriseSpu() ;
            Product.setSpu(ProductId);
            Date date = new Date();
            Product.setAppId(notifyBean.getAppId());
            Product.setCreatedAt(date);
            Product.setUpdatedAt(date);
            mapper.insertSelective(Product) ;
        });
        return new OperaResponse();
    }
}
