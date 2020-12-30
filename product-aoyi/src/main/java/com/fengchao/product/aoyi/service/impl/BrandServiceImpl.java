package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.dao.AoyiBaseBrandDao;
import com.fengchao.product.aoyi.mapper.AoyiBaseBrandXMapper;
import com.fengchao.product.aoyi.model.AoyiBaseBrandX;
import com.fengchao.product.aoyi.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private AoyiBaseBrandXMapper mapper ;
    @Autowired
    private AoyiBaseBrandDao dao ;

    @Override
    public OperaResponse insertOrUpdate(AoyiBaseBrandX bean) {
        OperaResponse response = new OperaResponse();
        AoyiBaseBrandX aoyiBaseBrand = mapper.selectByPrimaryKey(bean.getBrandId()) ;
        if (aoyiBaseBrand != null) {
            mapper.updateByPrimaryKey(bean) ;
        } else {
            bean.setAddTime(new Date());
            mapper.insertSelective(bean) ;
        }
        response.setData(bean.getBrandId());
        return response;
    }

    @Override
    public OperaResponse selectBrandPageable(QueryBean queryBean) {
        OperaResponse response = new OperaResponse() ;
        response.setData(dao.selectPageable(queryBean));
        return response;
    }
}
