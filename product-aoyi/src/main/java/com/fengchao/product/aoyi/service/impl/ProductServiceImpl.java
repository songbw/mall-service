package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.CategoryService;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.CosUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private AoyiProdIndexXMapper mapper;
    @Autowired
    private AoyiClientService aoyiClientService;
    @Autowired
    private EquityService equityService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Cacheable(value = "aoyiProdIndex")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findList(ProductQueryBean queryBean) throws ProductException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        if(queryBean.getCategory()!=null&&!queryBean.getCategory().equals(""))
            map.put("category", queryBean.getCategory());
        if(queryBean.getBrand()!=null&&!queryBean.getBrand().equals(""))
            map.put("brand", queryBean.getBrand());
        List<AoyiProdIndexX> prodIndices = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            mapper.selectLimit(map).forEach(aoyiProdIndex -> {
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
                prodIndices.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prodIndices, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public OperaResult findPrice(PriceQueryBean queryBean) throws ProductException {
        List<PriceSkus> list = new ArrayList<>();
        QueryCityPrice cityPrice = new QueryCityPrice();
        cityPrice.setCityId(queryBean.getCityId());
        queryBean.getSkus().forEach(priceBean -> {
            PriceSkus priceSkus = new PriceSkus();
            priceSkus.setSkuId(priceBean.getSkuId());
            list.add(priceSkus) ;
        });
        cityPrice.setSkus(list);
        return aoyiClientService.price(cityPrice);
    }

    @Override
    public List<InventoryBean> findInventory(InventoryQueryBean queryBean) throws ProductException {
        List<InventoryBean> inventoryBeans = new ArrayList<>() ;
        for (InventoryBean sku : queryBean.getSkus()) {
            QueryInventory inventory = new QueryInventory();
            inventory.setCityId(queryBean.getCityId());
            inventory.setCountyId(queryBean.getCountyId());
            List<InventorySkus> ilist = new ArrayList<>();
            InventorySkus inventorySkus = new InventorySkus();
            inventorySkus.setNum(sku.getRemainNum());
            inventorySkus.setSkuId(sku.getSkuId());
            ilist.add(inventorySkus);
            inventory.setSkuIds(ilist);
            OperaResult operaResult = aoyiClientService.inventory(inventory);
            Map result = (Map) operaResult.getData().get("result");
            InventoryBean inventoryBean = new InventoryBean();
            inventoryBean.setSkuId((String) result.get("skuId"));
            inventoryBean.setState((String) result.get("state"));
            inventoryBean.setPrice((String) result.get("price"));
            inventoryBean.setRemainNum(sku.getRemainNum());
            inventoryBeans.add(inventoryBean);
        }
        return inventoryBeans;
    }

    @Override
    public List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) throws ProductException {
        List<FreightFareBean> freightFareBeans = new ArrayList<>();
        List<CarriageParam> params = queryBean.getCarriages();
        for (CarriageParam param : params) {
            FreightFareBean freightFareBean = new FreightFareBean();
            QueryCarriage carriage = new QueryCarriage();
            carriage.setAmount(param.getAmount());
            carriage.setMerchantNo(param.getMerchantNo());
            carriage.setOrderNo(queryBean.getOrderId());
            OperaResult operaResult = aoyiClientService.shipCarriage(carriage);
            Map result = (Map) operaResult.getData().get("result");
            freightFareBean.setFreightFare((String) result.get("freightFare"));
            freightFareBean.setMerchantNo((String) result.get("merchantNo"));
            freightFareBean.setOrderNo((String) result.get("orderNo"));
            freightFareBeans.add(freightFareBean);
        }
        return freightFareBeans;

    }

    @Cacheable(value = "aoyiProdIndex", key = "#id")
    @DataSource(DataSourceNames.TWO)
    @Override
    public AoyiProdIndexX find(String id) throws ProductException {
        AoyiProdIndexX aoyiProdIndexX = mapper.selectByMpu(id);
        if (aoyiProdIndexX.getImageExtend() != null) {
            aoyiProdIndexX.setImage(aoyiProdIndexX.getImageExtend());
        }
        if (aoyiProdIndexX.getImagesUrlExtend() != null) {
            aoyiProdIndexX.setImagesUrl(aoyiProdIndexX.getImagesUrlExtend());
        }
        if (aoyiProdIndexX.getIntroductionUrlExtend() != null) {
            aoyiProdIndexX.setIntroductionUrl(aoyiProdIndexX.getIntroductionUrlExtend());
        }
        return aoyiProdIndexX;
    }

    @Cacheable(value = "aoyiProdIndex")
    @Override
    public List<AoyiProdIndexX> findAll() throws ProductException {
        HashMap map = new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",1000);
        List<AoyiProdIndexX> prodIndices = new ArrayList<>();
        mapper.selectAll(map).forEach(aoyiProdIndex -> {
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
            prodIndices.add(aoyiProdIndex);
        });
        return prodIndices;
    }

    @Cacheable(value = "productInfoBean", key = "#mpu")
    @DataSource(DataSourceNames.TWO)
    @Override
    public ProductInfoBean findAndPromotion(String mpu) throws ProductException {
        ProductInfoBean infoBean = new ProductInfoBean();
        AoyiProdIndexX aoyiProdIndexX = mapper.selectByMpu(mpu);
        String imageUrl = aoyiProdIndexX.getImagesUrl();
        if (imageUrl != null && (!"".equals(imageUrl))) {
            String image = "";
            if (imageUrl.indexOf("/") == 0) {
                image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
            } else {
                image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
            }
            aoyiProdIndexX.setImage(image);
        }
        if (aoyiProdIndexX.getImageExtend() != null) {
            aoyiProdIndexX.setImage(aoyiProdIndexX.getImageExtend());
        }
        if (aoyiProdIndexX.getImagesUrlExtend() != null) {
            aoyiProdIndexX.setImagesUrl(aoyiProdIndexX.getImagesUrlExtend());
        }
        if (aoyiProdIndexX.getIntroductionUrlExtend() != null) {
            aoyiProdIndexX.setIntroductionUrl(aoyiProdIndexX.getIntroductionUrlExtend());
        }
        BeanUtils.copyProperties(aoyiProdIndexX, infoBean);
        List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndexX.getSkuid());
        infoBean.setPromotion(promotionInfoBeans);

        List<CouponBean> couponBeans =  selectCouponBySku(aoyiProdIndexX) ;
        infoBean.setCoupon(couponBeans);
        return infoBean;
    }

    @Cacheable(value = "productInfoBean")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<ProductInfoBean> queryProductListByMpuIdList(List<String> mpuIdList) throws Exception {
        // 1. 查询商品信息
        log.info("根据mup集合查询产品信息 数据库查询参数:{}", JSONUtil.toJsonString(mpuIdList));
        List<AoyiProdIndex> aoyiProdIndexList = productDao.selectAoyiProdIndexListByMpuIdList(mpuIdList);
        log.info("根据mup集合查询产品信息 数据库返回:{}", JSONUtil.toJsonString(aoyiProdIndexList));

        // 2. 查询商品品类信息
        List<Integer> categoryIdList =
                aoyiProdIndexList.stream().map(p -> Integer.valueOf(p.getCategory())).collect(Collectors.toList());
        List<CategoryBean> categoryBeanList =  categoryService.queryCategoryListByCategoryIdList(categoryIdList);

        // 转map key：categoryId, value: CategoryBean
        Map<Integer, CategoryBean> categoryBeanMap =
                categoryBeanList.stream().collect(Collectors.toMap(p -> p.getCategoryId(), p -> p));

        // 3. 组装结果dto
        List<ProductInfoBean> productInfoBeanList = new ArrayList<>();
        for (AoyiProdIndex aoyiProdIndex : aoyiProdIndexList) {
            ProductInfoBean productInfoBean = convertToProductInfoBean(aoyiProdIndex);

            Integer categoryId = Integer.valueOf(productInfoBean.getCategory());
            productInfoBean.setCategoryName(categoryBeanMap.get(categoryId) == null ?
                    "" : categoryBeanMap.get(categoryId).getCategoryName());

            productInfoBeanList.add(productInfoBean);
        }

        return productInfoBeanList;
    }

    private List<CouponBean> selectCouponBySku(AoyiProdIndexX bean) {
        OperaResult result = equityService.selectCouponBySku(bean);
        log.info(JSON.toJSONString(result));
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<CouponBean> couponBeans = JSONObject.parseArray(jsonString, CouponBean.class);
            return couponBeans;
        }
        return null;
    }

    private List<PromotionInfoBean> findPromotionBySku(String skuId) {
        OperaResult result = equityService.findPromotionBySkuId(skuId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionInfoBean> subOrderTS = JSONObject.parseArray(jsonString, PromotionInfoBean.class);
            return subOrderTS;
        }
        return null;
    }

    /**
     *
     *
     * @param aoyiProdIndex
     * @return
     */
    private ProductInfoBean convertToProductInfoBean(AoyiProdIndex aoyiProdIndex) {
        ProductInfoBean productInfoBean = new ProductInfoBean();

        productInfoBean.setId(aoyiProdIndex.getId());
        productInfoBean.setSkuid(aoyiProdIndex.getSkuid());
        productInfoBean.setBrand(aoyiProdIndex.getBrand());
        productInfoBean.setCategory(aoyiProdIndex.getCategory() == null ? "0" : aoyiProdIndex.getCategory());
        // productInfoBean.setCategoryName(aoyiProdIndex.getcate());
        productInfoBean.setImage(aoyiProdIndex.getImage());
        productInfoBean.setModel(aoyiProdIndex.getModel());
        productInfoBean.setName(aoyiProdIndex.getName());
        productInfoBean.setWeight(aoyiProdIndex.getWeight());
        productInfoBean.setUpc(aoyiProdIndex.getUpc());
        productInfoBean.setSaleunit(aoyiProdIndex.getSaleunit());
        productInfoBean.setState(aoyiProdIndex.getState()); // 上下架状态 1：已上架；0：已下架
        productInfoBean.setPrice(aoyiProdIndex.getPrice()); // 销售价-商城显示的价格
        productInfoBean.setSprice(aoyiProdIndex.getSprice()); // 原价，进货价格
        productInfoBean.setImagesUrl(aoyiProdIndex.getImagesUrl());
        productInfoBean.setIntroductionUrl(aoyiProdIndex.getIntroductionUrl());
//        productInfoBean.setImageExtend(aoyiProdIndex.getImageExtend());
//        productInfoBean.setImagesUrlExtend(aoyiProdIndex.getImagesUrlExtend());
//        productInfoBean.setIntroductionUrlExtend(aoyiProdIndex.getIntroductionUrlExtend());
        productInfoBean.setMerchantId(aoyiProdIndex.getMerchantId());
        productInfoBean.setInventory(aoyiProdIndex.getInventory());
        productInfoBean.setBrandId(aoyiProdIndex.getBrandId());
        productInfoBean.setMpu(aoyiProdIndex.getMpu());

        return productInfoBean;
    }

}
