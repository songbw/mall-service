package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.ESService;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.CategoryService;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.CosUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.fengchao.product.aoyi.utils.ProductHandle;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
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
    @Autowired
    private ESService esService;

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
        if(queryBean.getPriceOrder()!=null&&!queryBean.getPriceOrder().equals(""))
            map.put("priceOrder", queryBean.getPriceOrder());
        List<AoyiProdIndexX> prodIndices = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            mapper.selectLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prodIndices.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prodIndices, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public PageInfo<AoyiProdIndex> findListByCategories(ProductQueryBean queryBean) throws ProductException {
        return productDao.selectListByCategories(queryBean);
    }

    @Override
    public OperaResult findPrice(PriceQueryBean queryBean) throws ProductException {
        OperaResult operaResult = new OperaResult();
        List<PriceSkus> list = new ArrayList<>();
        QueryCityPrice cityPrice = new QueryCityPrice();
        cityPrice.setCityId(queryBean.getCityId());
        queryBean.getSkus().forEach(priceBean -> {
            PriceSkus priceSkus = new PriceSkus();
            priceSkus.setSkuId(priceBean.getSkuId());
            list.add(priceSkus) ;
        });
        cityPrice.setSkus(list);
        OperaResponse operaResponse = aoyiClientService.price(cityPrice);
        operaResult.setCode(operaResponse.getCode());
        operaResult.setMsg(operaResponse.getMsg());
        operaResult.getData().put("result", operaResponse.getData()) ;
        return operaResult;
    }

    @Override
    public OperaResult findInventory(InventoryQueryBean queryBean) {
        OperaResult operaResult = new OperaResult();
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
            OperaResponse<InventoryBean> operaResponse = aoyiClientService.inventory(inventory);
            InventoryBean inventoryBean = operaResponse.getData();
            if (inventoryBean != null) {
                inventoryBean.setSkuId(sku.getSkuId());
                inventoryBean.setRemainNum(sku.getRemainNum());
                inventoryBeans.add(inventoryBean);
            }
        }
        operaResult.getData().put("result", inventoryBeans) ;
        return operaResult;
    }

    @Override
    public List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) throws ProductException {
        OperaResult operaResult = new OperaResult();
        List<FreightFareBean> freightFareBeans = new ArrayList<>();
        List<CarriageParam> params = queryBean.getCarriages();
        for (CarriageParam param : params) {
            QueryCarriage carriage = new QueryCarriage();
            carriage.setAmount(param.getAmount());
            carriage.setMerchantNo(param.getMerchantNo());
            carriage.setOrderNo(queryBean.getOrderId());
            OperaResponse<FreightFareBean> operaResponse = aoyiClientService.shipCarriage(carriage);
            FreightFareBean freightFareBean = operaResponse.getData();
            if (freightFareBean != null) {
                freightFareBeans.add(freightFareBean);
            }
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

    @Override
    public List<AoyiProdIndexX> findAll() throws ProductException {
        HashMap map = new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",1000);
        List<AoyiProdIndexX> prodIndices = new ArrayList<>();
        mapper.selectAll(map).forEach(aoyiProdIndex -> {
            aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
            prodIndices.add(aoyiProdIndex);
        });
        return prodIndices;
    }



    @Override
    public ProductInfoBean findAndPromotion(String mpu) throws ProductException {
        ProductInfoBean infoBean = new ProductInfoBean();
        AoyiProdIndexX aoyiProdIndexX = findByMpu(mpu) ;
        if (aoyiProdIndexX != null) {
            BeanUtils.copyProperties(aoyiProdIndexX, infoBean);
            List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndexX.getMpu());
            infoBean.setPromotion(promotionInfoBeans);
            List<CouponBean> couponBeans =  selectCouponBySku(aoyiProdIndexX) ;
            infoBean.setCoupon(couponBeans);
        }
        return infoBean;
    }

    @Cacheable(value = "AoyiProdIndexX", key = "#mpu")
    @DataSource(DataSourceNames.TWO)
    private AoyiProdIndexX findByMpu(String mpu) {
        AoyiProdIndexX aoyiProdIndexX = mapper.selectByMpu(mpu);
        if (aoyiProdIndexX != null) {
            aoyiProdIndexX = ProductHandle.updateImage(aoyiProdIndexX) ;
        }
        return aoyiProdIndexX;
    }

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

    @Override
    public OperaResult findPriceGAT(PriceQueryBean queryBean) throws ProductException {
        OperaResult operaResult = new OperaResult();
        List<PriceSkus> list = new ArrayList<>();
        QueryCityPrice cityPrice = new QueryCityPrice();
        cityPrice.setCityId(queryBean.getCityId());
        queryBean.getSkus().forEach(priceBean -> {
            PriceSkus priceSkus = new PriceSkus();
            priceSkus.setSkuId(priceBean.getSkuId());
            list.add(priceSkus) ;
        });
        cityPrice.setSkus(list);
        OperaResponse operaResponse = aoyiClientService.priceGAT(cityPrice);
        operaResult.setCode(operaResponse.getCode());
        operaResult.setMsg(operaResponse.getMsg());
        operaResult.getData().put("result", operaResponse.getData()) ;
        return operaResult;
    }

    @Override
    public OperaResponse search(ProductQueryBean queryBean){
        // TODO 驼峰修改
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
        return esService.search(queryBean) ;
    }

    @Override
    public List<AoyiProdIndex> getProdsByMpus(List<String> mpuIdList) {
        // 1. 查询商品信息
        log.info("根据mup集合查询产品信息 数据库查询参数:{}", JSONUtil.toJsonString(mpuIdList));
        List<AoyiProdIndex> aoyiProdIndexList = productDao.selectAoyiProdIndexListByMpuIdList(mpuIdList);
        log.info("根据mup集合查询产品信息 数据库返回:{}", JSONUtil.toJsonString(aoyiProdIndexList));
        return aoyiProdIndexList;
    }

    @Override
    public OperaResult findInventorySelf(InventorySelfQueryBean queryBean) {
        OperaResult result = new OperaResult();
        log.info("根据mup集合查询库存信息 数据库查询参数:{}", JSONUtil.toJsonString(queryBean));
        List<String> mpuList = new ArrayList<>() ;
        queryBean.getInventories().forEach(inventoryMpus -> {
            mpuList.add(inventoryMpus.getMpu()) ;
        });
        List<InventoryMpus> inventories = new ArrayList<>() ;
        List<AoyiProdIndex> aoyiProdIndexList = productDao.selectAoyiProdIndexListByMpuIdList(mpuList);
        aoyiProdIndexList.forEach(aoyiProdIndex -> {
            for (InventoryMpus inventory: queryBean.getInventories()) {
                if (aoyiProdIndex.getMpu().equals(inventory.getMpu())){
                    if (aoyiProdIndex.getInventory() >= inventory.getRemainNum()) {
                        inventory.setState("1");
                    }
                    inventories.add(inventory) ;
                }
            }
        });
        result.getData().put("result", inventories) ;
        return result;
    }

    @Transactional
    @Override
    public OperaResult inventorySub(List<InventoryMpus> inventories) {
        OperaResult result = new OperaResult();
        for (InventoryMpus inventoryMpus : inventories) {
            AoyiProdIndexX prodIndexX = mapper.selectForUpdateByMpu(inventoryMpus.getMpu()) ;
            if (prodIndexX == null) {
                result.setCode(200010);
                result.setMsg("商品 " + prodIndexX.getMpu() + " 不存在。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            if (prodIndexX.getInventory() <= 0 || prodIndexX.getInventory() < inventoryMpus.getRemainNum()) {
                result.setCode(200010);
                result.setMsg("商品 " + prodIndexX.getName() + " 库存不足。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            AoyiProdIndexX temp = new AoyiProdIndexX();
            temp.setMpu(prodIndexX.getMpu());
            temp.setUpdatedAt(new Date());
            temp.setInventory(prodIndexX.getInventory() - inventoryMpus.getRemainNum());
            mapper.updateByPrimaryKeySelective(temp) ;
        }
        return result;
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
