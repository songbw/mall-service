package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.EnterpriseBrandMapper;
import com.fengchao.product.aoyi.model.EnterpriseBrand;
import com.fengchao.product.aoyi.model.EnterpriseBrandExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 15:03
 */
@Component
public class EnterpriseSyncBrandDao {
    private EnterpriseBrandMapper mapper ;

    @Autowired
    public EnterpriseSyncBrandDao(EnterpriseBrandMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据品牌ID批量查询同步品牌信息
     * @param brandIds
     * @return
     */
    public List<EnterpriseBrand> batchFindByBrandIds(List<Integer> brandIds) {
        EnterpriseBrandExample example = new EnterpriseBrandExample() ;
        EnterpriseBrandExample.Criteria criteria = example.createCriteria() ;
        criteria.andBrandIdIn(brandIds) ;
        List<EnterpriseBrand> list = mapper.selectByExample(example) ;
        return list ;
    }
}
