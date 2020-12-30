package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.EnterpriseCategoryMapper;
import com.fengchao.product.aoyi.model.EnterpriseCategory;
import com.fengchao.product.aoyi.model.EnterpriseCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 15:03
 */
@Component
public class EnterpriseSyncCategoryDao {
    private EnterpriseCategoryMapper mapper ;

    @Autowired
    public EnterpriseSyncCategoryDao(EnterpriseCategoryMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据类别ID批量查询同步类别信息
     * @param categoryIds
     * @return
     */
    public List<EnterpriseCategory> batchFindByCategoryIds(List<Integer> categoryIds) {
        EnterpriseCategoryExample example = new EnterpriseCategoryExample() ;
        EnterpriseCategoryExample.Criteria criteria = example.createCriteria() ;
        criteria.andCategoryIdIn(categoryIds) ;
        List<EnterpriseCategory> list = mapper.selectByExample(example) ;
        return list ;
    }
}
