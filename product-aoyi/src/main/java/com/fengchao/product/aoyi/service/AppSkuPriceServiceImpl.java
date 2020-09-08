package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.AppRatePriceDao;
import com.fengchao.product.aoyi.dao.AppSkuPriceDao;
import com.fengchao.product.aoyi.mapper.AppRatePriceMapper;
import com.fengchao.product.aoyi.mapper.AppSkuPriceMapper;
import com.fengchao.product.aoyi.model.AppRatePrice;
import com.fengchao.product.aoyi.model.AppSkuPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public OperaResponse update(AppSkuPrice bean) {
        return null;
    }

    @Override
    public List<AppSkuPrice> findByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
