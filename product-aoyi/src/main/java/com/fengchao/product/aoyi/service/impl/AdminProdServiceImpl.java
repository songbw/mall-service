package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AquityService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.mapper.ProdExtendMapper;
import com.fengchao.product.aoyi.mapper.SkuCodeMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.utils.CosUtil;
import com.fengchao.product.aoyi.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AdminProdServiceImpl implements AdminProdService {

    @Autowired
    private AoyiProdIndexMapper prodMapper;
    @Autowired
    private ProdExtendMapper prodExtendMapper;
    @Autowired
    private SkuCodeMapper skuCodeMapper;
    @Autowired
    private AquityService aquityService;

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
        List<AoyiProdIndex> prods = new ArrayList<>();
        total = prodMapper.selectSearchCount(map);
        if (total > 0) {
            prodMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                String imageUrl = aoyiProdIndex.getImagesUrl();
                if (imageUrl != null && (!"".equals(imageUrl))) {
                    String image = "";
                    if (imageUrl.indexOf("/") == 0) {
                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                    } else {
                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                    }
                    aoyiProdIndex.setImage(image);
                }
                prods.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prods, total, offset, limit);
        return pageBean;
    }

    @Override
    public PageBean selectNameList(SerachBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("name", bean.getQuery());
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("categoryID",bean.getCategoryID());
        map.put("skuid",bean.getSkuid());
        map.put("state",bean.getState());
        map.put("brand",bean.getBrand());
        map.put("merchantId",bean.getMerchantId());
        List<AoyiProdIndex> prods = new ArrayList<>();
        total = prodMapper.selectSearchCount(map);
        if (total > 0) {
            prodMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                String imageUrl = aoyiProdIndex.getImagesUrl();
                if (imageUrl != null && (!"".equals(imageUrl))) {
                    String image = "";
                    if (imageUrl.indexOf("/") == 0) {
                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                    } else {
                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                    }
                    aoyiProdIndex.setImage(image);
                }
                prods.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int getProdListToRedis(){
        int num = 0;
        List<AoyiProdIndex> aoyiProdIndices = prodMapper.selectProdAll();
        if(aoyiProdIndices != null){
            num = 1;
        }
        aoyiProdIndices.forEach(aoyiProdIndex -> {
            String imageUrl = aoyiProdIndex.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image = "";
                if (imageUrl.indexOf("/") == 0) {
                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                } else {
                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                }
                aoyiProdIndex.setImage(image);
            }
            if (aoyiProdIndex.getImageExtend() != null) {
                aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
            }
            if (aoyiProdIndex.getImagesUrlExtend() != null) {
                aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
            }
            if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
                aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
            }
            String jsonObject = JSON.toJSONString(aoyiProdIndex) ;
            RedisUtil.putRedis(aoyiProdIndex.getSkuid(), jsonObject , RedisUtil.webexpire);
        });
        return num;
    }

    @Override
    public int add(AoyiProdIndex bean) throws ProductException {
        if (bean.getMerchantId() > 0) {
            // 获取商户信息
            SkuCode skuCode = skuCodeMapper.selectByMerchantId(bean.getMerchantId()) ;
            if (skuCode == null) {
                // 去多商户系统中获取
            }
            String merchantCode = skuCode.getMerchantCode();
            int skuValue = skuCode.getSkuValue() ;
            AtomicInteger atomicInteger= new AtomicInteger(skuValue);
            String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet()) ;
            bean.setSku(sku);
            bean.setCreatedAt(new Date());
            prodMapper.insertSelective(bean) ;
            skuCode.setSkuValue(atomicInteger.get());
            skuCodeMapper.updateSkuValueByPrimaryKey(skuCode) ;
        } else {
            throw new ProductException(200001, "merchantId 为null或等于0");
        }
        return bean.getId();
    }

    @Override
    public int update(AoyiProdIndex bean) throws ProductException {
        if (bean.getId() > 0) {
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

    @Override
    public PageBean findProdAll(QueryProdBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        List<ProductInfoBean> infoBeans = new ArrayList<>();
        total = prodMapper.selectSkuByCouponIdCount(bean);
        if (total > 0) {
            prodMapper.selectSkuByCouponIdLimit(bean).forEach(aoyiProdIndex -> {
                ProductInfoBean infoBean = new ProductInfoBean();
                String imageUrl = aoyiProdIndex.getImagesUrl();
                if (imageUrl != null && (!"".equals(imageUrl))) {
                    String image = "";
                    if (imageUrl.indexOf("/") == 0) {
                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                    } else {
                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                    }
                    aoyiProdIndex.setImage(image);
                }
                if (aoyiProdIndex.getImageExtend() != null) {
                    aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
                }
                if (aoyiProdIndex.getImagesUrlExtend() != null) {
                    aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
                }
                if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
                    aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
                }
                BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                OperaResult operaResult = aquityService.find(aoyiProdIndex.getSkuid());
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
}
