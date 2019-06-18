package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.CosUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private AoyiProdIndexMapper mapper;
    @Autowired
    private AoyiClientService aoyiClientService;
    @Autowired
    private EquityService equityService;

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
        List<AoyiProdIndex> prodIndices = new ArrayList<>();
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

    @DataSource(DataSourceNames.TWO)
    @Override
    public AoyiProdIndex find(String id) throws ProductException {
        AoyiProdIndex aoyiProdIndex = mapper.selectBySkuId(id);
        if (aoyiProdIndex.getImageExtend() != null) {
            aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
        }
        if (aoyiProdIndex.getImagesUrlExtend() != null) {
            aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
        }
        if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
            aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
        }
        return aoyiProdIndex;
    }

    @Override
    public List<AoyiProdIndex> findAll() throws ProductException {
        HashMap map = new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",1000);
        List<AoyiProdIndex> prodIndices = new ArrayList<>();
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

    @DataSource(DataSourceNames.TWO)
    @Override
    public ProductInfoBean findAndPromotion(String skuId) throws ProductException {
        ProductInfoBean infoBean = new ProductInfoBean();
        AoyiProdIndex aoyiProdIndex = mapper.selectBySkuId(skuId);
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
        List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getSkuid());
        infoBean.setPromotion(promotionInfoBeans);

        List<CouponBean> couponBeans =  new ArrayList<>();
//        couponMapper.selectCouponBySku(aoyiProdIndex).forEach(coupon -> {
//            couponBeans.add(couponToBean(coupon));
//        });
        infoBean.setCoupon(couponBeans);

        return infoBean;
    }

    private CouponBean couponToBean(Coupon coupon){
        CouponBean couponBean = new CouponBean();
        couponBean.setId(coupon.getId());
        couponBean.setName(coupon.getName());
        couponBean.setSupplierMerchantId(coupon.getSupplierMerchantId());
        couponBean.setSupplierMerchantName(coupon.getSupplierMerchantName());
        couponBean.setReleaseTotal(coupon.getReleaseTotal());
        couponBean.setReleaseNum(coupon.getReleaseNum());
        couponBean.setReleaseStartDate(coupon.getReleaseStartDate());
        couponBean.setReleaseEndDate(coupon.getReleaseEndDate());
        couponBean.setStatus(coupon.getStatus());
        couponBean.setEffectiveStartDate(coupon.getEffectiveStartDate());
        couponBean.setEffectiveEndDate(coupon.getEffectiveEndDate());
        couponBean.setDescription(coupon.getDescription());
        couponBean.setUserCollectNum(coupon.getUserCollectNum());
        if(coupon.getExcludeDates()!= null && !"".equals(coupon.getExcludeDates())) {
            couponBean.setExcludeDates(JSONArray.parseArray(coupon.getExcludeDates()));
        }
        couponBean.setUrl(coupon.getUrl());
        couponBean.setCreateDate(coupon.getCreateDate());
        couponBean.setCategory(coupon.getCategory());
        if(coupon.getTags() != null && !"".equals(coupon.getTags())){
            String[] tagsStr = coupon.getTags().split(", ");
            int[] tagsNum = new int[tagsStr.length];
            for (int i = 0; i < tagsStr.length; i++) {
                tagsNum[i] = Integer.parseInt(tagsStr[i]);
            }
            couponBean.setTags(tagsNum);
        }
        couponBean.setImageUrl(coupon.getImageUrl());
        Rules rules = new Rules();
        Scenario scenario = new Scenario();
        rules.setScenario(scenario);
        Collect collect = new Collect();
        rules.setCollect(collect);
        Customer customer = new Customer();
        rules.setCustomer(customer);
        couponBean.setRules(rules);
        if(couponBean.getRules() != null){
            couponBean.getRules().setCode(coupon.getCode());
            couponBean.getRules().setRulesDescription(coupon.getRulesDescription());
            couponBean.getRules().setPerLimited(coupon.getPerLimited());
            if(coupon.getScopes() != null){
                couponBean.getRules().setScopes(coupon.getScopes().split(","));
            }
            couponBean.getRules().getScenario().setType(coupon.getScenarioType());
            if(coupon.getCouponSkus() != null){
                couponBean.getRules().getScenario().setCouponSkus(coupon.getCouponSkus().split(","));
            }
            if(coupon.getExcludeSkus() != null){
                couponBean.getRules().getScenario().setExcludeSkus(coupon.getExcludeSkus().split(","));
            }
            if(coupon.getCategories() != null){
                couponBean.getRules().getScenario().setCategories(coupon.getCategories().split(","));
            }
            if(coupon.getBrands() != null){
                couponBean.getRules().getScenario().setBrands(coupon.getBrands().split(","));
            }
            couponBean.getRules().getCollect().setType(coupon.getCollectType());
            if(coupon.getPoints() != null){
                couponBean.getRules().getCollect().setPoints(coupon.getPoints());
            }
            couponBean.getRules().getCustomer().setType(coupon.getCustomerType());
            if(coupon.getUsers() != null){
                couponBean.getRules().getCustomer().setUsers(coupon.getUsers().split(","));
            }
            if(coupon.getCouponRules()!= null && !"".equals(coupon.getCouponRules())) {
                couponBean.getRules().setCouponRules(JSONArray.parseObject(coupon.getCouponRules()));
            }
        }

        return couponBean;
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
}
