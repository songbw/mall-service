package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.feign.VendorsService;
import com.fengchao.product.aoyi.mapper.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.service.AdminProdService;
//import com.fengchao.product.aoyi.utils.RedisUtil;
import com.fengchao.product.aoyi.utils.ProductHandle;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AdminProdServiceImpl implements AdminProdService {

    private static Logger logger = LoggerFactory.getLogger(AdminProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper prodMapper;
    @Autowired
    private ProdExtendMapper prodExtendMapper;
    @Autowired
    private SkuCodeXMapper skuCodeMapper;
    @Autowired
    private EquityService equityService;
    @Autowired
    private VendorsService vendorsService;
    @Autowired
    private AoyiBaseCategoryXMapper categoryMapper;
    @Autowired
    private AoyiBaseBrandMapper brandMapper;

    @Autowired
    private ProductDao productDao;

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findProdList(Integer offset, Integer limit, String state, Integer merchantId) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        map.put("state",state);
        map.put("merchantId",merchantId);
        List<AoyiProdIndexX> prods = new ArrayList<>();
        total = prodMapper.selectSearchCount(map);
        if (total > 0) {
            prodMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prods, total, offset, limit);
        return pageBean;
    }



    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectNameList(SerachBean bean) {
        PageBean pageBean = new PageBean();
        List<AoyiProdIndexX> prods = new ArrayList<>();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("id", bean.getId());
        map.put("name", bean.getQuery());
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("categoryID",bean.getCategoryID());
        map.put("skuid",bean.getSkuid());
        map.put("mpu",bean.getMpu());
        map.put("state",bean.getState());
        map.put("brand",bean.getBrand());
        map.put("order",bean.getOrder());
        if (bean.getMerchantHeader() == 0) {
            map.put("merchantId", bean.getMerchantId());
        } else if (bean.getMerchantHeader() == bean.getMerchantId()) {
            map.put("merchantId", bean.getMerchantId());
        } else {
            return PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        }
        total = prodMapper.selectSearchCount(map);
        if (total > 0) {
            prodMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }

        pageBean = PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @DataSource(DataSourceNames.TWO)
    public PageBean selectProductListPageable(SerachBean bean) {
        PageBean pageBean = new PageBean();
        List<AoyiProdIndexX> prods = new ArrayList<>();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("name", bean.getQuery());
        map.put("skuid",bean.getSkuid());
        map.put("state",bean.getState());
        if (bean.getMerchantHeader() == 0) {
            map.put("merchantId", bean.getMerchantId());
        } else if (bean.getMerchantHeader() == bean.getMerchantId()) {
            map.put("merchantId", bean.getMerchantId());
        } else {
            return PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        }
        total = prodMapper.selectSearchCount(map);
        if (total > 0) {
            prodMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }

        pageBean = PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public int getProdListToRedis(){
        int num = 0;
        List<AoyiProdIndexX> aoyiProdIndices = prodMapper.selectProdAll();
        if(aoyiProdIndices != null){
            num = 1;
        }
        aoyiProdIndices.forEach(aoyiProdIndex -> {
            aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
            String jsonObject = JSON.toJSONString(aoyiProdIndex) ;
//            RedisUtil.putRedis(aoyiProdIndex.getSkuid(), jsonObject , RedisUtil.webexpire);
        });
        return num;
    }

    @Override
    public int add(AoyiProdIndexX bean) throws ProductException {
        if (bean.getMerchantId() > 0) {
            // 1. 判断sku是否重复
            List<AoyiProdIndex> aoyiProdIndexList =
                    productDao.selectAoyiProdIndexListBySKUAndMerchant(bean.getSkuid(), bean.getMerchantId());

            if (CollectionUtils.isNotEmpty(aoyiProdIndexList)) {
                logger.warn("创建商品 sku:{},merchantId:{} 已存在!", bean.getSkuid(), bean.getMerchantId());

                throw new ProductException(200001, "重复的sku");
            }

            // 获取商户信息
            SkuCode skuCode = skuCodeMapper.selectByMerchantId(bean.getMerchantId()) ;
            if (skuCode == null) {
                // 查询商户信息
                VendorsProfileBean profileBean = findVendorInfo(bean.getMerchantId()) ;
                if (profileBean != null) {
                    // 获取最新code
                    SkuCode lastCode = skuCodeMapper.selectLast();
                    logger.info(bean.getMerchantId() + "");
                    int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                    // 添加商户信息
                    skuCode = new SkuCode();
                    skuCode.setMerchantId(bean.getMerchantId());
                    skuCode.setMerchantName(profileBean.getCompany().getName());
                    skuCode.setMerchantCode(code + "");
                    skuCode.setSkuValue(0);
                    skuCode.setCreatedAt(new Date());
                    skuCode.setUpdatedAt(new Date());
                    skuCodeMapper.insertSelective(skuCode) ;
                } else {
                    throw new ProductException(200001, "商户信息为null");
                }
            }
            String merchantCode = skuCode.getMerchantCode();
            int skuValue = skuCode.getSkuValue() ;
            AtomicInteger atomicInteger= new AtomicInteger(skuValue); // FIXME : 这个原子操作好像没用呀!
            String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet()) ;
            bean.setMpu(sku);
            bean.setCreatedAt(new Date());
            bean.setState("-1");
            if (bean.getBrandId() != null && bean.getBrandId() > 0) {
                AoyiBaseBrand baseBrand = brandMapper.selectByPrimaryKey(bean.getBrandId()) ;
                if (baseBrand == null) {
                    bean.setBrandId(0);
                }
            }
            if (bean.getCategory() != null && !"".equals(bean.getCategory())) {
                AoyiBaseCategoryX category = categoryMapper.selectByPrimaryKey(Integer.parseInt(bean.getCategory())) ;
                if (category == null) {
                    bean.setCategory("");
                }
            }
            prodMapper.insertSelective(bean) ;
            skuCode.setSkuValue(atomicInteger.get());
            skuCodeMapper.updateSkuValueByPrimaryKey(skuCode) ;
        } else {
            throw new ProductException(200001, "merchantId 为null或等于0");
        }
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
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            prodMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findProdAll(QueryProdBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        List<ProductInfoBean> infoBeans = new ArrayList<>();
        total = prodMapper.selectSkuByCouponIdCount(bean);
        if (total > 0) {
            prodMapper.selectSkuByCouponIdLimit(bean).forEach(aoyiProdIndex -> {
                ProductInfoBean infoBean = new ProductInfoBean();
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                OperaResult operaResult = equityService.findPromotionBySkuId(aoyiProdIndex.getSkuid());
                Object object = operaResult.getData().get("result");
                String objectString = JSON.toJSONString(object);
                List<PromotionInfoBean> promotionList = JSONObject.parseArray(objectString, PromotionInfoBean.class);
                infoBean.setPromotion(promotionList);
                infoBeans.add(infoBean);
            });
        }
        pageBean = PageBean.build(pageBean, infoBeans, total, bean.getOffset(), bean.getPageSize());
        return pageBean;
    }

    private VendorsProfileBean findVendorInfo(int id) {
        OperaResult result = vendorsService.vendorInfo(id) ;
        logger.info("vendor info : " + JSON.toJSONString(result));
        if (result.getCode() == 200) {
            Object object = result.getData() ;
            String jsonString = JSON.toJSONString(object);
            VendorsProfileBean profileBean = JSONObject.parseObject(jsonString, VendorsProfileBean.class);
            return profileBean;
        }
        return null;
    }
}
