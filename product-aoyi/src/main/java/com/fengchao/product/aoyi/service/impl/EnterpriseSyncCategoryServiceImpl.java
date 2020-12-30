package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.EnterpriseSyncCategoryNotifyBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.EnterpriseSyncCategoryDao;
import com.fengchao.product.aoyi.mapper.EnterpriseCategoryMapper;
import com.fengchao.product.aoyi.model.EnterpriseCategory;
import com.fengchao.product.aoyi.service.EnterpriseSyncCategoryService;
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
public class EnterpriseSyncCategoryServiceImpl implements EnterpriseSyncCategoryService {

    private EnterpriseCategoryMapper mapper ;
    private EnterpriseSyncCategoryDao dao ;

    @Autowired
    public EnterpriseSyncCategoryServiceImpl(EnterpriseCategoryMapper mapper, EnterpriseSyncCategoryDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public OperaResponse notifyEnterpriseCategory(EnterpriseSyncCategoryNotifyBean notifyBean) {
        List<EnterpriseCategory> list = dao.batchFindByCategoryIds(notifyBean.getCategoryIds()) ;
        if (list != null && list.size() > 0) {
            list.forEach(enterpriseCategory -> {
                enterpriseCategory.setUpdatedAt(new Date());
                mapper.updateByPrimaryKeySelective(enterpriseCategory) ;
            });
        }
        List<Integer> updatedCategoryIds = list.stream().map(enterpriseCategory -> enterpriseCategory.getCategoryId()).collect(Collectors.toList());
        List<Integer> addBrandIds = notifyBean.getCategoryIds().stream().filter(brandId -> !updatedCategoryIds.contains(brandId)).collect(Collectors.toList()) ;
        addBrandIds.parallelStream().forEach(categoryId -> {
            EnterpriseCategory category = new EnterpriseCategory() ;
            category.setCategoryId(categoryId);
            Date date = new Date();
            category.setAppId(notifyBean.getAppId());
            category.setCreatedAt(date);
            category.setUpdatedAt(date);
            mapper.insertSelective(category) ;
        });
        return new OperaResponse();
    }
}
