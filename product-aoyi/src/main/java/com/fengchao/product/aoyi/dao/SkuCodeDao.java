package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.SkuCodeMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryExample;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.model.SkuCodeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-19 下午5:41
 */
@Component
public class SkuCodeDao {

    private SkuCodeMapper skuCodeMapper;

    @Autowired
    public SkuCodeDao(SkuCodeMapper skuCodeMapper) {
        this.skuCodeMapper = skuCodeMapper;
    }

    /**
     * 查询所有的商户信息
     *
     * @return
     */
    public List<SkuCode> selectAll() {
        SkuCodeExample skuCodeExample = new SkuCodeExample();

        SkuCodeExample.Criteria criteria = skuCodeExample.createCriteria();

        List<SkuCode> skuCodeList = skuCodeMapper.selectByExample(skuCodeExample);

        return skuCodeList;
    }
}
