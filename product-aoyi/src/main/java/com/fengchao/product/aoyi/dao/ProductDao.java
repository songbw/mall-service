package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexExample;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author song
 * @Date 19-8-26 下午3:05
 */
@Component
public class ProductDao {

    private AoyiProdIndexMapper aoyiProdIndexMapper;

    private AoyiProdIndexXMapper aoyiProdIndexXMapper;

    @Autowired
    public ProductDao(AoyiProdIndexMapper aoyiProdIndexMapper,
                      AoyiProdIndexXMapper aoyiProdIndexXMapper) {
        this.aoyiProdIndexMapper = aoyiProdIndexMapper;
        this.aoyiProdIndexXMapper = aoyiProdIndexXMapper;
    }

    /**
     * 新增
     *
     * @param aoyiProdIndexWithBLOBs
     * @return
     */
    public Integer insert(AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs) {
        aoyiProdIndexMapper.insertSelective(aoyiProdIndexWithBLOBs);
        return aoyiProdIndexWithBLOBs.getId();
    }

    /**
     * 批量插入
     *
     * @param aoyiProdIndexList
     */
    public void batchInsert(List<AoyiProdIndex> aoyiProdIndexList) {
        aoyiProdIndexXMapper.batchInsert(aoyiProdIndexList);
    }

    /**
     * 根据mpuId集合 查询product列表
     *
     * @param mpuIdList
     * @return
     */
    public List<AoyiProdIndexWithBLOBs> selectAoyiProdIndexListByMpuIdList(List<String> mpuIdList) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();

        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuIn(mpuIdList);

        List<AoyiProdIndexWithBLOBs> aoyiProdIndexList = aoyiProdIndexMapper.selectByExampleWithBLOBs(aoyiProdIndexExample);

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
        aoyiProdIndex.setSprice(bean.getSPrice());
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
     * 根据SKU获取商品列表
     * @param mpu
     * @param merchantId
     * @return
     */
    public List<AoyiProdIndex> findByMpu(String mpu, int merchantId) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuEqualTo(mpu);
        criteria.andMerchantIdEqualTo(merchantId);
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample);
        return aoyiProdIndices;
    }

    /**
     * 根据SKU获取商品列表
     * @param mpu
     * @return
     */
    public AoyiProdIndexWithBLOBs selectByMpu(String mpu) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuEqualTo(mpu);
        List<AoyiProdIndexWithBLOBs> aoyiProdIndices = aoyiProdIndexMapper.selectByExampleWithBLOBs(aoyiProdIndexExample);
        if (aoyiProdIndices != null && aoyiProdIndices.size() > 0) {
            return aoyiProdIndices.get(0) ;
        }
        return null;
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
        aoyiProdIndexWithBLOBs.setBrandId(aoyiProdIndexX.getBrandId());
        aoyiProdIndexWithBLOBs.setCategory(aoyiProdIndexX.getCategory());
        aoyiProdIndexWithBLOBs.setModel(aoyiProdIndexX.getModel());
        aoyiProdIndexWithBLOBs.setMpu(aoyiProdIndexX.getMpu());
        aoyiProdIndexWithBLOBs.setMerchantId(aoyiProdIndexX.getMerchantId());
        aoyiProdIndexWithBLOBs.setMerchantCode(aoyiProdIndexX.getMerchantCode());
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
        aoyiProdIndex.setState(null);
        aoyiProdIndex.setUpdatedAt(new Date());

        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 根据MPU更新产品信息
     * @param bean
     */
    public void updateByMpu(AoyiProdIndex bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuEqualTo(bean.getMpu());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(bean, aoyiProdIndex);
        aoyiProdIndex.setId(null);
        aoyiProdIndex.setUpdatedAt(new Date());
        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 更新产品信息
     * @param bean
     */
    public int updateAoyiProduct(AoyiProdIndex bean) {
        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(bean, aoyiProdIndex);
        aoyiProdIndex.setMerchantId(null);
        aoyiProdIndexMapper.updateByPrimaryKeySelective(aoyiProdIndex) ;
        return aoyiProdIndex.getId() ;
    }

    /**
     * 更新产品信息
     *
     * @param aoyiProdIndexWithBLOBs
     */
    public int updateByPrimaryKeySelective(AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs) {

        int count = aoyiProdIndexMapper.updateByPrimaryKeySelective(aoyiProdIndexWithBLOBs);

        return count;
    }

    /**
     * 根据MPU添加产品信息
     * @param bean
     */
    public void insertByMpu(AoyiProdIndex bean) {
        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(bean, aoyiProdIndex);
        Date date = new Date();
        aoyiProdIndex.setUpdatedAt(date);
        aoyiProdIndex.setCreatedAt(date);
        aoyiProdIndexMapper.insertSelective(aoyiProdIndex) ;
    }

    /**
     * 查询product列表
     *
     * @param queryBean
     * @return
     */
    public PageInfo<AoyiProdIndexWithBLOBs> selectListByCategories(ProductQueryBean queryBean, List<String> codes) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        aoyiProdIndexExample.setOrderByClause("merchant_sort desc");
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        if (queryBean.getBrand() != null && !queryBean.getBrand().equals(""))
            criteria.andBrandEqualTo(queryBean.getBrand());
        if (StringUtils.isNotBlank(queryBean.getCategory())) {
            criteria.andCategoryEqualTo(queryBean.getCategory()) ;
        }
        // 租户
//        if (queryBean.getRenterId() != null && !queryBean.getRenterId().equals(""))
//            criteria.andRenterIdEqualTo(queryBean.getRenterId());
        if (null != queryBean.getMerchantIds() && queryBean.getMerchantIds().size() > 0) {
            criteria.andMerchantIdIn(queryBean.getMerchantIds()) ;
        }
        if (null != queryBean.getMerchantCodes() && queryBean.getMerchantCodes().size() > 0) {
            criteria.andMerchantCodeIn(queryBean.getMerchantCodes()) ;
        }
        if (queryBean.getPriceOrder() != null && !queryBean.getPriceOrder().equals(""))
            aoyiProdIndexExample.setOrderByClause("merchant_sort desc, CAST(price AS DECIMAL) " + queryBean.getPriceOrder());
        if (queryBean.getCategories() != null && queryBean.getCategories().size() > 0)
            criteria.andCategoryIn(queryBean.getCategories());
        criteria.andStateEqualTo("1");
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<AoyiProdIndexWithBLOBs> aoyiProdIndexList = aoyiProdIndexMapper.selectByExampleWithBLOBs(aoyiProdIndexExample);
        PageInfo<AoyiProdIndexWithBLOBs> pageInfo = new PageInfo(aoyiProdIndexList);

        return pageInfo;
    }

    /**
     * 分页查询product信息
     *
     * @param queryBean
     * @return
     */
    public PageInfo<AoyiProdIndexWithBLOBs> selectPageable(ProductQueryBean queryBean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        String sortValue = "";
        if (StringUtils.isNotBlank(queryBean.getSortValue())) {
            sortValue = sortValue + queryBean.getSortValue() + " desc" ;
        }
        if (!StringUtils.isBlank(queryBean.getPriceOrder())) {
            if (StringUtils.isNotBlank(sortValue)) {
                sortValue = sortValue + ", price " + queryBean.getPriceOrder() ;
            } else {
                sortValue = sortValue + "price " + queryBean.getPriceOrder() ;
            }
        }
        if (StringUtils.isNotBlank(sortValue)) {
            aoyiProdIndexExample.setOrderByClause(sortValue);
        }
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();

        if (queryBean.getMerchantId() != null && queryBean.getMerchantId() != 0) {
            criteria.andMerchantIdEqualTo(queryBean.getMerchantId());
        }
        if (StringUtils.isNotBlank(queryBean.getSkuProfix())) {
            criteria.andSkuidLike(queryBean.getSkuProfix() + "%");
        }
        if (StringUtils.isNotBlank(queryBean.getCategory())) {
            criteria.andCategoryEqualTo(queryBean.getCategory()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getBrand())) {
            criteria.andBrandEqualTo(queryBean.getBrand()) ;
        }
        if (null != queryBean.getMerchantIds() && queryBean.getMerchantIds().size() > 0) {
            criteria.andMerchantIdIn(queryBean.getMerchantIds()) ;
        }
        if (null != queryBean.getMerchantCodes() && queryBean.getMerchantCodes().size() > 0) {
            criteria.andMerchantCodeIn(queryBean.getMerchantCodes()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getState())) {
            criteria.andStateEqualTo(queryBean.getState()) ;
        }
        if (null!=queryBean.getId() && queryBean.getId() !=0) {
            criteria.andIdEqualTo(queryBean.getId()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getName())) {
            criteria.andNameLike("%" + queryBean.getName() + "%") ;
        }
        if (StringUtils.isNotBlank(queryBean.getCategoryId())) {
            criteria.andCategoryLike(queryBean.getCategoryId() + "%") ;
        }
        if (StringUtils.isNotBlank(queryBean.getSkuid())) {
            criteria.andSkuidEqualTo(queryBean.getSkuid()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getMpu())) {
            criteria.andMpuEqualTo(queryBean.getMpu()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getMpuPrefix())) {
            criteria.andMpuLike(queryBean.getMpuPrefix() + "%") ;
        }
        if (StringUtils.isNotBlank(queryBean.getMinPrice()) && StringUtils.isNotBlank(queryBean.getMaxPrice())) {
            criteria.andPriceBetween(queryBean.getMinPrice(), queryBean.getMaxPrice()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getOrder())) {
            if ("asc".equals(queryBean.getOrder())) {
                aoyiProdIndexExample.setOrderByClause("created_at asc");
            } else if ("desc".equals(queryBean.getOrder())){
                aoyiProdIndexExample.setOrderByClause("created_at desc");
            }
        }
        if (queryBean.getCategories() != null && queryBean.getCategories().size() > 0) {
            criteria.andCategoryIn(queryBean.getCategories()) ;
        }

        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());

        List<AoyiProdIndexWithBLOBs> aoyiProdIndexList = aoyiProdIndexMapper.selectByExampleWithBLOBs(aoyiProdIndexExample);

        PageInfo<AoyiProdIndexWithBLOBs> pageInfo = new PageInfo(aoyiProdIndexList);

        return pageInfo;
    }

    /**
     * 根据品牌获取商品列表
     * @param brands
     * @return
     */
    public List<AoyiProdIndex> selectByBrand(List<Integer> brands) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andBrandIdIn(brands) ;
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample) ;
        return aoyiProdIndices;
    }

    /**
     * 根据类目获取商品列表
     * @param category
     * @return
     */
    public List<AoyiProdIndex> selectByCategory(List<String> category) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andCategoryIn(category) ;
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample) ;
        return aoyiProdIndices;
    }

    /**
     * 根据商户获取商品列表
     * @param merchant
     * @return
     */
    public List<AoyiProdIndex> selectByMerchant(List<Integer> merchant) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMerchantIdIn(merchant) ;
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample) ;
        return aoyiProdIndices;
    }

    /**
     * 根据MPU获取商品列表
     * @param mpus
     * @return
     */
    public List<AoyiProdIndex> selectByMpu(List<String> mpus) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuIn(mpus) ;
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample) ;
        return aoyiProdIndices;
    }

    /**
     * 更新同步时间字段
     * @param id
     */
    public void updateSyncAt(int id) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andIdEqualTo(id);

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        aoyiProdIndex.setSyncAt(new Date());

        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    public List<AoyiProdIndex> selectFix() {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuLike("64%");
        criteria.andMerchantIdNotEqualTo(1) ;
        List<AoyiProdIndex> aoyiProdIndices = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample) ;
        return aoyiProdIndices;
    }

    public void updateFix(AoyiProdIndexWithBLOBs aoyiProdIndex) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andIdEqualTo(aoyiProdIndex.getId());
        aoyiProdIndex.setSyncAt(new Date());
        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 更新商品价格和状态
     * @param bean
     */
    public void updatePriceAndState(AoyiProdIndex bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMpuEqualTo(bean.getMpu());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        if (!StringUtils.isEmpty(bean.getPrice())) {
            aoyiProdIndex.setPrice(bean.getPrice());
        }
        if (!StringUtils.isEmpty(bean.getState())) {
            aoyiProdIndex.setState(bean.getState());
        }
        aoyiProdIndex.setUpdatedAt(new Date());
        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 更新上下架状态
     * @param bean
     */
    public void updateStateById(AoyiProdIndex bean) {
        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        aoyiProdIndex.setId(bean.getId());
        aoyiProdIndex.setState(bean.getState());
        aoyiProdIndex.setUpdatedAt(new Date());
        aoyiProdIndexMapper.updateByPrimaryKeySelective(aoyiProdIndex);
    }

    /**
     * 根据merchantid查询 商品
     *
     * @param merchantId
     * @return
     */
    public List<AoyiProdIndex> selectAoyiProdIndexListByMerchant(Integer merchantId) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();

        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMerchantIdEqualTo(merchantId);
        List<AoyiProdIndex> aoyiProdIndexList = aoyiProdIndexMapper.selectByExample(aoyiProdIndexExample);

        return aoyiProdIndexList;
    }

    /**
     * 根据merchantId 批量上下架商品
     * @param bean
     */
    public void updateByMerchantIdAndState(AoyiProdIndex bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMerchantIdEqualTo(bean.getMerchantId());

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        if (!StringUtils.isEmpty(bean.getState())) {
            aoyiProdIndex.setState(bean.getState());
        }
        aoyiProdIndex.setUpdatedAt(new Date());
        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

    /**
     * 更新商品价格和状态
     * @param bean
     */
    @Async
    public void updateByMerchantId(AoyiProdIndex bean) {
        AoyiProdIndexExample aoyiProdIndexExample = new AoyiProdIndexExample();
        AoyiProdIndexExample.Criteria criteria = aoyiProdIndexExample.createCriteria();
        criteria.andMerchantIdEqualTo(bean.getMerchantId()) ;

        AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs();
        aoyiProdIndex.setMerchantSort(bean.getMerchantSort());
        aoyiProdIndex.setUpdatedAt(new Date());
        aoyiProdIndexMapper.updateByExampleSelective(aoyiProdIndex, aoyiProdIndexExample);
    }

}
