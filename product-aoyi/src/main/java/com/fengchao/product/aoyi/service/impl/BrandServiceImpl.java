package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.mapper.AoyiBaseBrandMapper;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private AoyiBaseBrandMapper mapper ;

    @Override
    public OperaResponse insertOrUpdate(AoyiBaseBrand bean) {
        OperaResponse response = new OperaResponse();
        AoyiBaseBrand aoyiBaseBrand = mapper.selectByPrimaryKey(bean.getBrandId()) ;
        if (aoyiBaseBrand != null) {
            mapper.updateByPrimaryKey(bean) ;
        } else {
            bean.setAddTime(new Date());
            mapper.insertSelective(bean) ;
        }
        response.setData(bean.getBrandId());
        return response;
    }
}
