package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.RenterCategoryDao;
import com.fengchao.product.aoyi.mapper.RenterCategoryMapper;
import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.service.RenterCategoryService;
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
public class RenterCategoryServiceImpl implements RenterCategoryService {

    @Autowired
    private RenterCategoryMapper mapper ;
    @Autowired
    private RenterCategoryDao dao;

    @Override
    public List<RenterCategory> findListById(int id) {
        return null;
    }

    @Override
    public OperaResponse add(RenterCategory bean) {
        return null;
    }

    @Override
    public OperaResponse update(RenterCategory bean) {
        return null;
    }

    @Override
    public List<RenterCategory> findByIds(List<Integer> categoryIds) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
