package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.AppRatePriceDao;
import com.fengchao.product.aoyi.dao.AppSkuPriceDao;
import com.fengchao.product.aoyi.mapper.AppRatePriceMapper;
import com.fengchao.product.aoyi.mapper.AppSkuPriceMapper;
import com.fengchao.product.aoyi.model.AppRatePrice;
import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.service.AppSkuPriceService;
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
public class AppSkuPriceServiceImpl implements AppSkuPriceService {

    @Autowired
    private AppSkuPriceMapper mapper ;
    @Autowired
    private AppSkuPriceDao dao;


    @Override
    public List<AppSkuPrice> findListById(int id) {
        return null;
    }

    @Override
    public OperaResponse add(AppSkuPrice bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        List<AppSkuPrice> list = dao.selectByRenterIdAndMpuAndSku(bean) ;
        if (list != null && list.size() > 0) {
            bean.setId(list.get(0).getId());
            bean.setUpdatedAt(date);
            mapper.updateByPrimaryKeySelective(bean) ;
        } else {
            bean.setUpdatedAt(date);
            bean.setCreatedAt(date);
            mapper.insertSelective(bean) ;
        }

        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse addBatch(List<AppSkuPrice> beans) {
        OperaResponse response = new OperaResponse() ;
        beans.forEach(bean -> {
            Date date = new Date();
            List<AppSkuPrice> list = dao.selectByRenterIdAndMpuAndSku(bean) ;
            if (list != null && list.size() > 0) {
                bean.setId(list.get(0).getId());
                bean.setUpdatedAt(date);
                mapper.updateByPrimaryKeySelective(bean) ;
            } else {
                bean.setUpdatedAt(date);
                bean.setCreatedAt(date);
                mapper.insertSelective(bean) ;
            }
        });
        response.setData(beans);
        return response;
    }

    @Override
    public OperaResponse update(AppSkuPrice bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        bean.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(bean) ;
        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse updateBatch(List<AppSkuPrice> beans) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        beans.forEach(bean -> {
            bean.setUpdatedAt(date);
            mapper.updateByPrimaryKeySelective(bean) ;
        });
        response.setData(beans);
        return response;
    }

    @Override
    public List<AppSkuPrice> findByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id) ;
    }
}
