package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.EnterpriseSpuMapper;
import com.fengchao.product.aoyi.mapper.EnterpriseSpuMapper;
import com.fengchao.product.aoyi.model.EnterpriseSpu;
import com.fengchao.product.aoyi.model.EnterpriseSpuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 15:03
 */
@Component
public class EnterpriseSyncSpuDao {
    private EnterpriseSpuMapper mapper ;

    @Autowired
    public EnterpriseSyncSpuDao(EnterpriseSpuMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spu批量查询同步spu信息
     * @param SpuIds
     * @return
     */
    public List<EnterpriseSpu> batchFindBySpuIds(List<String> SpuIds) {
        EnterpriseSpuExample example = new EnterpriseSpuExample() ;
        EnterpriseSpuExample.Criteria criteria = example.createCriteria() ;
        criteria.andSpuIn(SpuIds) ;
        List<EnterpriseSpu> list = mapper.selectByExample(example) ;
        return list ;
    }
}
