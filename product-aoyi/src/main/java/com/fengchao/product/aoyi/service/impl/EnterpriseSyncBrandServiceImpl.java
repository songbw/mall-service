package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.EnterpriseSyncBrandNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.EnterpriseSyncBrandDao;
import com.fengchao.product.aoyi.mapper.EnterpriseBrandMapper;
import com.fengchao.product.aoyi.model.EnterpriseBrand;
import com.fengchao.product.aoyi.service.EnterpriseSyncBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songbw
 * @date 2020/12/30 15:01
 */
@Service
public class EnterpriseSyncBrandServiceImpl implements EnterpriseSyncBrandService {

    private EnterpriseBrandMapper mapper ;
    private EnterpriseSyncBrandDao dao ;

    @Autowired
    public EnterpriseSyncBrandServiceImpl(EnterpriseBrandMapper mapper, EnterpriseSyncBrandDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public OperaResponse notifyEnterpriseBrand(EnterpriseSyncBrandNotifyBean notifyBean) {
        List<EnterpriseBrand> list = dao.batchFindByBrandIds(notifyBean.getBrandIds()) ;
        if (list != null && list.size() > 0) {
            list.forEach(enterpriseBrand -> {
                enterpriseBrand.setUpdatedAt(new Date());
                mapper.updateByPrimaryKeySelective(enterpriseBrand) ;
            });
        }
        List<Integer> updatedBrandIds = list.stream().map(enterpriseBrand -> enterpriseBrand.getBrandId()).collect(Collectors.toList());
        List<Integer> addBrandIds = notifyBean.getBrandIds().stream().filter(brandId -> !updatedBrandIds.contains(brandId)).collect(Collectors.toList()) ;
        addBrandIds.parallelStream().forEach(brandId -> {
            EnterpriseBrand brand = new EnterpriseBrand() ;
            brand.setBrandId(brandId);
            Date date = new Date();
            brand.setAppId(notifyBean.getAppId());
            brand.setCreatedAt(date);
            brand.setUpdatedAt(date);
            mapper.insertSelective(brand) ;
        });
        return new OperaResponse();
    }
}
