package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.dao.SkuCodeDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.mapper.SkuCodeXMapper;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.service.SkuCodeService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SkuCodeServiceImpl implements SkuCodeService {

    @Autowired
    private SkuCodeXMapper mapper;

    @Autowired
    private SkuCodeDao skuCodeDao;

    @Override
    public Integer add(SkuCode bean) throws ProductException {
        if (bean.getMerchantId() == null || bean.getMerchantId() <= 0) {
            throw new ProductException(200011, "商户ID不能为空");
        }
        if (bean.getMerchantCode() == null) {
            throw new ProductException(200012, "商户CODE不能为空");
        }
        bean.setCreatedAt(new Date());
        mapper.insertSelective(bean) ;
        return bean.getId();
    }

    @Override
    public Integer update(SkuCode bean) throws ProductException {
        if (bean.getId() == null || bean.getId() <= 0) {
            throw new ProductException(200013, "ID不能为空");
        }
        bean.setUpdatedAt(new Date());
        mapper.updateByPrimaryKeySelective(bean);
        return bean.getId();
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public SkuCode findByMerchantId(Integer merchantId) throws ProductException {
        if (merchantId == null || merchantId <= 0) {
            throw new ProductException(200011, "商户ID不能为空");
        }
        return mapper.selectByMerchantId(merchantId);
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public SkuCode find(Integer id) throws ProductException {
        if (id == null || id <= 0) {
            throw new ProductException(200013, "ID不能为空");
        }
        return mapper.selectByPrimaryKey(id) ;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public List<SkuCode> findAll() throws ProductException {
        List<SkuCode> skuCodeList = skuCodeDao.selectAll();

        log.info("获取所有商户信息 数据库返回:{}", JSONUtil.toJsonString(skuCodeList));
        return skuCodeList;
    }

    @Override
    public Integer updateSkuValueAddById(SkuCode bean) throws ProductException {
        if (bean.getId() == null || bean.getId() <= 0) {
            throw new ProductException(200013, "ID不能为空");
        }
        if (bean.getSkuValue() == null || bean.getSkuValue() <= 0) {
            throw new ProductException(200014, "mpu value不能为空");
        }
        bean.setUpdatedAt(new Date());
        bean.setSkuValue(bean.getSkuValue() + 1);
        mapper.updateSkuValueByPrimaryKey(bean) ;
        return bean.getId();
    }
}
