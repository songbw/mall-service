package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.ProdExtendMapper;
import com.fengchao.product.aoyi.model.ProdExtend;
import com.fengchao.product.aoyi.model.ProdExtendExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 */
@Component
public class ProductExtendDao {

    private ProdExtendMapper prodExtendMapper;

    @Autowired
    public ProductExtendDao(ProdExtendMapper prodExtendMapper) {
        this.prodExtendMapper = prodExtendMapper;
    }

    /**
     * 根据mpuId集合 查询product列表
     *
     * @param mpuIdList
     * @return
     */
    public List<ProdExtend> selectProdExtendsByMpuIdList(List<String> mpuIdList) {
        ProdExtendExample prodExtendExample = new ProdExtendExample();

        ProdExtendExample.Criteria criteria = prodExtendExample.createCriteria();
        criteria.andMpuIn(mpuIdList);

        List<ProdExtend> prodExtendList = prodExtendMapper.selectByExample(prodExtendExample);

        return prodExtendList;
    }

}
