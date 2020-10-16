package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.RenterCategoryQueryBean;
import com.fengchao.product.aoyi.dao.RenterCategoryDao;
import com.fengchao.product.aoyi.mapper.RenterCategoryMapper;
import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.service.RenterCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2020/9/8 18:22
 */
@Service
@Slf4j
public class RenterCategoryServiceImpl implements RenterCategoryService {

    @Autowired
    private RenterCategoryMapper mapper ;
    @Autowired
    private RenterCategoryDao dao;

    @Override
    public List<CategoryBean> findListByRenterId(RenterCategoryQueryBean bean) {
        return dao.selectByRenterId(bean) ;
    }

    @Override
    public OperaResponse add(RenterCategory bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date() ;
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        mapper.insertSelective(bean) ;
        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse addBatch(List<RenterCategory> bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date() ;
        bean.forEach(renterCategory -> {
            //TODO 判断appid,categoryid是否为null
            renterCategory.setCreatedAt(date);
            renterCategory.setUpdatedAt(date);
            mapper.insertSelective(renterCategory) ;
        });
        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse update(RenterCategory bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date() ;
        bean.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(bean) ;
        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse updateBatch(List<RenterCategory> beans) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date() ;
        beans.forEach(bean -> {
            //TODO 判断id是否为null
            bean.setUpdatedAt(date);
            mapper.updateByPrimaryKeySelective(bean) ;
        });

        response.setData(beans);
        return response;
    }

    @Override
    public List<RenterCategory> findByIds(List<Integer> categoryIds) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id) ;
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        ids.forEach(id -> {
            delete(id);
        });
    }
}
