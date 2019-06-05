package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.mapper.ProdExtendMapper;
import com.fengchao.product.aoyi.model.ProdExtend;
import com.fengchao.product.aoyi.service.ProdExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdExtendServiceImpl implements ProdExtendService {

    @Autowired
    private ProdExtendMapper mapper;

    @Override
    public int updateProdExtend(ProdExtend bean) {
        ProdExtend prodExtend = mapper.selectByPrimaryKey(bean.getSkuid());
        if(prodExtend != null){
            return mapper.updateByPrimaryKeySelective(bean);
        }else {
            return mapper.insertSelective(bean);
        }
    }

    @Override
    public int deleteProdExtend(String skuid) {
        return mapper.deleteByPrimaryKey(skuid);
    }
}
