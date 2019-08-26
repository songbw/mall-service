package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.ThirdProdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ThirdProdServiceImpl implements ThirdProdService {

    private static Logger logger = LoggerFactory.getLogger(ThirdProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper prodMapper;
    @Autowired
    private ProductDao productDao;

    @Override
    public int add(AoyiProdIndexX bean) throws ProductException {
        return bean.getId();
    }

    @Override
    public int update(AoyiProdIndexX bean) throws ProductException {
        if (bean.getId() > 0) {
            bean.setUpdatedAt(new Date());
            prodMapper.updateByPrimaryKeySelective(bean);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
        return bean.getId();
    }

    @Override
    public void updatePrice(PriceBean bean) {
        productDao.updatePrice(bean);
    }

    @Override
    public void updateState(StateBean bean) {
        productDao.updateState(bean);
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            prodMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
    }

}
