package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AoyiBaseBrandMapper;
import com.fengchao.product.aoyi.mapper.AoyiBaseBrandXMapper;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class AoyiBaseBrandDao {

    private AoyiBaseBrandMapper aoyiBaseBrandMapper;

    private AoyiBaseBrandXMapper aoyiBaseBrandXMapper;

    @Autowired
    public AoyiBaseBrandDao(AoyiBaseBrandMapper aoyiBaseBrandMapper,
                            AoyiBaseBrandXMapper aoyiBaseBrandXMapper) {
        this.aoyiBaseBrandMapper = aoyiBaseBrandMapper;
        this.aoyiBaseBrandXMapper = aoyiBaseBrandXMapper;
    }

    /**
     * 新增
     * @param aoyiBaseBrand
     * @return
     */
    public Integer insert(AoyiBaseBrand aoyiBaseBrand) {
        aoyiBaseBrandMapper.insertSelective(aoyiBaseBrand);
        return aoyiBaseBrand.getBrandId();
    }

    /**
     * 批量插入
     *
     * @param aoyiBaseBrandList
     */
    public void batchInsert(List<AoyiBaseBrand> aoyiBaseBrandList) {
        aoyiBaseBrandXMapper.batchInsert(aoyiBaseBrandList);
    }

    /**
     * 根据brandId查询
     *
     * @param brandId
     * @return
     */
    public AoyiBaseBrand selectByBrandId(Integer brandId) {
        AoyiBaseBrand aoyiBaseBrand = aoyiBaseBrandMapper.selectByPrimaryKey(brandId);
        return aoyiBaseBrand;
    }



}
