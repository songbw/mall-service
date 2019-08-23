package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-19 下午4:36
 */
@Component
public class ProductDao {

    private AoyiProdIndexMapper aoyiProdIndexMapper;

    @Autowired
    public ProductDao(AoyiProdIndexMapper aoyiProdIndexMapper) {
        this.aoyiProdIndexMapper = aoyiProdIndexMapper;
    }

    /**
     * 根据mpuId集合 查询product列表
     *
     * @param mpuIdList
     * @return
     */
    public List<AoyiProdIndex> selectAoyiProdIndexListByMpuIdList(List<String> mpuIdList) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();

        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuIn(mpuIdList);

        List<AoyiProdIndex> aoyiProdIndexList = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample);

        return aoyiProdIndexList;
    }

    /**
     * 根据skuid和merchantid查询 商品
     *
     * @param skuId
     * @param merchantId
     * @return
     */
    public List<AoyiProdIndex> selectAoyiProdIndexListBySKUAndMerchant(String skuId, Integer merchantId) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();

        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andSkuidEqualTo(skuId);
        criteria.andMerchantIdEqualTo(merchantId);

        List<AoyiProdIndex> aoyiProdIndexList = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample);

        return aoyiProdIndexList;
    }
}
