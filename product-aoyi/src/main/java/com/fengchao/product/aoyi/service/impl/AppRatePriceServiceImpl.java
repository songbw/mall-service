package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.AppRatePriceDao;
import com.fengchao.product.aoyi.mapper.AppRatePriceMapper;
import com.fengchao.product.aoyi.model.AppRatePrice;
import com.fengchao.product.aoyi.service.AppRatePriceService;
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
public class AppRatePriceServiceImpl implements AppRatePriceService {

    @Autowired
    private AppRatePriceMapper mapper ;
    @Autowired
    private AppRatePriceDao dao;


    @Override
    public List<AppRatePrice> findListById(int id) {
        return null;
    }

    @Override
    public OperaResponse add(AppRatePrice bean) {
        return null;
    }

    @Override
    public OperaResponse update(AppRatePrice bean) {
        return null;
    }

    @Override
    public List<AppRatePrice> findByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
