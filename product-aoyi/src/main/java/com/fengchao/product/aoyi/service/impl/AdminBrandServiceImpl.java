package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.mapper.AoyiBaseBrandMapper;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.service.AdminBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminBrandServiceImpl implements AdminBrandService {

    @Autowired
    private AoyiBaseBrandMapper brandMapper;

    @Cacheable(value = "brand")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findBrandList(Integer offset, Integer limit) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseBrand> brands = new ArrayList<>();
        total = brandMapper.selectLimitCount(map);
        if (total > 0) {
            brands = brandMapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, brands, total, offset, limit);
        return pageBean;
    }

    @CachePut(value = "brand", key = "#bean.brandId")
    @Override
    public Integer updateBrandbyId(AoyiBaseBrand bean) {
        brandMapper.updateByPrimaryKeySelective(bean);
        return bean.getBrandId();
    }

    @CachePut(value = "brand", key = "#bean.brandId")
    @Override
    public Integer create(AoyiBaseBrand bean) {
        brandMapper.insertSelective(bean);
        return bean.getBrandId();
    }

    @CacheEvict(value = "brand", key = "#id")
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Cacheable(value = "brand")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectNameList(Integer offset, Integer limit, String query) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("brandName", query);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseBrand> brands = new ArrayList<>();
        total = brandMapper.selectLimitCount(map);
        if (total > 0) {
            brands = brandMapper.selectNameList(map);
        }
        pageBean = PageBean.build(pageBean, brands, total, offset, limit);
        return pageBean;
    }
}
