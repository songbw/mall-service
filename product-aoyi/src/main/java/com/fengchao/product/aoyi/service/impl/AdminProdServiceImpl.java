package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.SerachBean;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.mapper.ProdExtendMapper;
import com.fengchao.product.aoyi.mapper.SkuCodeMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.utils.CosUtil;
import com.fengchao.product.aoyi.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            String sku = merchantCode + String.format("%06d", atomicInteger.getAndIncrement()) ;
            bean.setSku(sku);
            // 获取sku值
            prodMapper.insertSelective(bean) ;
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
}
