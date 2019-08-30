package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexExample;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author song
 * @Date 19-8-26 下午3:05
 */
@Component
public class ProductDao {

    private AoyiProdIndexMapper aoyiProdIndexMapper;

    @Autowired
    public ProductDao(AoyiProdIndexMapper aoyiProdIndexMapper) {
        this.aoyiProdIndexMapper = aoyiProdIndexMapper;
    }

    /**
     * 新增
     *
     * @param aoyiProdIndexWithBLOBs
     * @return
     */
    public Integer insert(AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs) {
        int count = aoyiProdIndexMapper.insertSelective(aoyiProdIndexWithBLOBs);

        return aoyiProdIndexWithBLOBs.getId();
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

    /**
     * 更新商品价格
     * @param bean
     */
    public void updatePrice(PriceBean bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andSkuidEqualTo(bean.getSkuId());
        criteria.andMerchantIdEqualTo(bean.getMerchantId());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        aoyiProdIndex.setPrice(bean.getPrice());
        aoyiProdIndex.setUpdatedAt(new Date());

        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 更新上下架状态
     * @param bean
     */
    public void updateState(StateBean bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andSkuidEqualTo(bean.getSkuId());
        criteria.andMerchantIdEqualTo(bean.getMerchantId());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        aoyiProdIndex.setState(bean.getState());
        aoyiProdIndex.setUpdatedAt(new Date());

        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 根据SKU获取商品列表
     * @param skuId
     * @param merchantId
     * @return
     */
    public List<AoyiProdIndex> findBySkuId(String skuId, int merchantId) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andSkuidEqualTo(skuId);
        criteria.andMerchantIdEqualTo(merchantId);
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample);
        return aoyiProdIndices;
    }

    /**
     * 添加商品数据
     * @param aoyiProdIndexX
     * @return
     */
    public Integer insertX(AoyiProdIndexX aoyiProdIndexX) {
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
        aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndexX.getSkuid());
        aoyiProdIndexWithBLOBs.setBrand(aoyiProdIndexX.getBrand());
        aoyiProdIndexWithBLOBs.setCategory(aoyiProdIndexX.getCategory());
        aoyiProdIndexWithBLOBs.setModel(aoyiProdIndexX.getModel());
        aoyiProdIndexWithBLOBs.setMpu(aoyiProdIndexX.getMpu());
        aoyiProdIndexWithBLOBs.setMerchantId(aoyiProdIndexX.getMerchantId());
        aoyiProdIndexWithBLOBs.setState(aoyiProdIndexX.getState());
        aoyiProdIndexWithBLOBs.setName(aoyiProdIndexX.getName());
        aoyiProdIndexWithBLOBs.setPrice(aoyiProdIndexX.getPrice());
        aoyiProdIndexWithBLOBs.setSprice(aoyiProdIndexX.getSprice());
        aoyiProdIndexWithBLOBs.setImagesUrl(aoyiProdIndexX.getImagesUrl());
        aoyiProdIndexWithBLOBs.setIntroductionUrl(aoyiProdIndexX.getIntroductionUrl());
        aoyiProdIndexWithBLOBs.setCreatedAt(aoyiProdIndexX.getCreatedAt());
        aoyiProdIndexWithBLOBs.setUpdatedAt(aoyiProdIndexX.getUpdatedAt());
        int count = aoyiProdIndexMapper.insertSelective(aoyiProdIndexWithBLOBs);
        return aoyiProdIndexWithBLOBs.getId();
    }

    /**
     * 更新产品信息
     * @param bean
     */
    public void update(AoyiProdIndexX bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andSkuidEqualTo(bean.getSkuid());
        criteria.andMerchantIdEqualTo(bean.getMerchantId());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(bean, aoyiProdIndex);
        aoyiProdIndex.setUpdatedAt(new Date());

        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }
}
