package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.EnterpriseSyncSkuNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.EnterpriseSyncSkuDao;
import com.fengchao.product.aoyi.mapper.EnterpriseSkuMapper;
import com.fengchao.product.aoyi.model.EnterpriseSku;
import com.fengchao.product.aoyi.service.EnterpriseSyncSkuService;
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
public class EnterpriseSyncSkuServiceImpl implements EnterpriseSyncSkuService {

    private EnterpriseSkuMapper mapper ;
    private EnterpriseSyncSkuDao dao ;

    @Autowired
    public EnterpriseSyncSkuServiceImpl(EnterpriseSkuMapper mapper, EnterpriseSyncSkuDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public OperaResponse notifyEnterpriseSku(EnterpriseSyncSkuNotifyBean notifyBean) {
        List<EnterpriseSku> list = dao.batchFindBySkuIds(notifyBean.getSkus()) ;
        if (list != null && list.size() > 0) {
            list.forEach(enterpriseSku -> {
                enterpriseSku.setUpdatedAt(new Date());
                mapper.updateByPrimaryKeySelective(enterpriseSku) ;
            });
        }
        List<String> updatedSkuIds = list.stream().map(enterpriseSku -> enterpriseSku.getSku()).collect(Collectors.toList());
        List<String> addBrandIds = notifyBean.getSkus().stream().filter(brandId -> !updatedSkuIds.contains(brandId)).collect(Collectors.toList()) ;
        addBrandIds.parallelStream().forEach(SkuId -> {
            EnterpriseSku Sku = new EnterpriseSku() ;
            Sku.setSku(SkuId);
            Date date = new Date();
            Sku.setAppId(notifyBean.getAppId());
            Sku.setCreatedAt(date);
            Sku.setUpdatedAt(date);
            mapper.insertSelective(Sku) ;
        });
        return new OperaResponse();
    }
}
