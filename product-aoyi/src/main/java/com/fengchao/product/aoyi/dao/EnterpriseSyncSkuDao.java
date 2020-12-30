package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.EnterpriseSkuMapper;
import com.fengchao.product.aoyi.model.EnterpriseSku;
import com.fengchao.product.aoyi.model.EnterpriseSkuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 15:03
 */
@Component
public class EnterpriseSyncSkuDao {
    private EnterpriseSkuMapper mapper ;

    @Autowired
    public EnterpriseSyncSkuDao(EnterpriseSkuMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据Sku批量查询同步Sku信息
     * @param SkuIds
     * @return
     */
    public List<EnterpriseSku> batchFindBySkuIds(List<String> SkuIds) {
        EnterpriseSkuExample example = new EnterpriseSkuExample() ;
        EnterpriseSkuExample.Criteria criteria = example.createCriteria() ;
        criteria.andSkuIn(SkuIds) ;
        List<EnterpriseSku> list = mapper.selectByExample(example) ;
        return list ;
    }
}
