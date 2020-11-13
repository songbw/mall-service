package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.constants.ProductConstant;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.ESService;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.product.aoyi.service.CategoryService;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.fengchao.product.aoyi.utils.ProductHandle;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private AoyiProdIndexXMapper mapper;
    private AoyiClientService aoyiClientService;
    private EquityService equityService;
    private ProductDao productDao;
    private CategoryService categoryService;
    private ESService esService;
    private InventoryDao inventoryDao ;
    private StarDetailImgDao starDetailImgDao ;
    private StarPropertyDao starPropertyDao ;
    private StarSkuDao starSkuDao ;
    private AoyiClientRpcService aoyiClientRpcService;
    private WeipinhuiAddressDao weipinhuiAddressDao;
    private AppSkuPriceDao appSkuPriceDao ;
    private VendorsRpcService vendorsRpcService;
    private ProductHandle productHandle ;

    @Autowired
    public ProductServiceImpl(AoyiProdIndexXMapper mapper, AoyiClientService aoyiClientService, EquityService equityService, ProductDao productDao, CategoryService categoryService, ESService esService, InventoryDao inventoryDao, StarDetailImgDao starDetailImgDao, StarPropertyDao starPropertyDao, StarSkuDao starSkuDao, AoyiClientRpcService aoyiClientRpcService, WeipinhuiAddressDao weipinhuiAddressDao, AppSkuPriceDao appSkuPriceDao, VendorsRpcService vendorsRpcService, ProductHandle productHandle) {
        this.mapper = mapper;
        this.aoyiClientService = aoyiClientService;
        this.equityService = equityService;
        this.productDao = productDao;
        this.categoryService = categoryService;
        this.esService = esService;
        this.inventoryDao = inventoryDao;
        this.starDetailImgDao = starDetailImgDao;
        this.starPropertyDao = starPropertyDao;
        this.starSkuDao = starSkuDao;
        this.aoyiClientRpcService = aoyiClientRpcService;
        this.weipinhuiAddressDao = weipinhuiAddressDao;
        this.appSkuPriceDao = appSkuPriceDao;
        this.vendorsRpcService = vendorsRpcService;
        this.productHandle = productHandle;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findList(ProductQueryBean queryBean) throws ProductException {
        // 根据APPID查询renterId
        productHandle.setClientProductQueryBean(queryBean);
        PageInfo<AoyiProdIndex> prodIndexPageInfo = productDao.selectListByCategories(queryBean, queryBean.getMerchantCodes());
        PageBean pageBean = new PageBean();
        int total = (int) prodIndexPageInfo.getTotal();
        List<AoyiProdIndex> aoyiProdIndices = prodIndexPageInfo.getList() ;
        List<ProductInfoBean> prodIndices = new ArrayList<>();
        aoyiProdIndices.forEach(prodIndex -> {
            ProductInfoBean infoBean = new ProductInfoBean();
            AoyiProdIndexX prodIndexX = new AoyiProdIndexX();
            BeanUtils.copyProperties(prodIndex, prodIndexX);
            productHandle.setProductXClient(prodIndexX, queryBean.getRenterId());
            if ("1".equals(prodIndexX.getState())) {
                BeanUtils.copyProperties(prodIndexX, infoBean);
                List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(prodIndexX.getMpu(), queryBean.getAppId());
                infoBean.setPromotion(promotionInfoBeans);
                prodIndices.add(infoBean);
            }

        });
        pageBean = PageBean.build(pageBean, prodIndices, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public PageInfo<ProductInfoBean> findListV2(ProductQueryBean queryBean) throws ProductException {
        // TODO 根据renderId获取商户ID列表，并设置queryBean
        PageInfo<ProductInfoBean> productInfoBeanPageInfo = new PageInfo<>() ;
        // 获取可读取的商户配置
        productHandle.setClientProductQueryBean(queryBean);
        PageInfo<AoyiProdIndex> prodIndexPageInfo = productDao.selectPageable(queryBean);
        log.debug("prodIndexPageInfo: {}", JSONUtil.toJsonString(prodIndexPageInfo));
        productInfoBeanPageInfo.setTotal(prodIndexPageInfo.getTotal());
        productInfoBeanPageInfo.setPageNum(prodIndexPageInfo.getPageNum());
        productInfoBeanPageInfo.setPageSize(prodIndexPageInfo.getPageSize());
        productInfoBeanPageInfo.setSize(prodIndexPageInfo.getSize());
        productInfoBeanPageInfo.setStartRow(prodIndexPageInfo.getStartRow());
        productInfoBeanPageInfo.setPages(prodIndexPageInfo.getPages());
        productInfoBeanPageInfo.setEndRow(prodIndexPageInfo.getEndRow());

        List<AoyiProdIndex> aoyiProdIndices = prodIndexPageInfo.getList() ;
        List<ProductInfoBean> productInfoBeans = new ArrayList<>() ;
        aoyiProdIndices.forEach(aoyiProdIndex -> {
            aoyiProdIndex = productHandle.updateImageExample(aoyiProdIndex) ;
            ProductInfoBean infoBean = new ProductInfoBean() ;
            BeanUtils.copyProperties(aoyiProdIndex, infoBean);
            List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getMpu(), queryBean.getAppId());
            infoBean.setPromotion(promotionInfoBeans);
            productInfoBeans.add(infoBean) ;
        });
        productInfoBeanPageInfo.setList(productInfoBeans);
        return productInfoBeanPageInfo ;
    }

    @Override
    public PageInfo<ProductInfoBean> findListByCategories(ProductQueryBean queryBean) throws ProductException {
        // 根据APPID查询renterId
        productHandle.setClientProductQueryBean(queryBean);
        PageInfo<ProductInfoBean> productInfoBeanPageInfo = new PageInfo<>() ;
        PageInfo<AoyiProdIndex> prodIndexPageInfo = productDao.selectListByCategories(queryBean, queryBean.getMerchantCodes());
        log.debug("prodIndexPageInfo: {}", JSONUtil.toJsonString(prodIndexPageInfo));
        productInfoBeanPageInfo.setTotal(prodIndexPageInfo.getTotal());
        productInfoBeanPageInfo.setPageNum(prodIndexPageInfo.getPageNum());
        productInfoBeanPageInfo.setPageSize(prodIndexPageInfo.getPageSize());
        productInfoBeanPageInfo.setSize(prodIndexPageInfo.getSize());
        productInfoBeanPageInfo.setStartRow(prodIndexPageInfo.getStartRow());
        productInfoBeanPageInfo.setPages(prodIndexPageInfo.getPages());
        productInfoBeanPageInfo.setEndRow(prodIndexPageInfo.getEndRow());

        List<AoyiProdIndex> aoyiProdIndices = prodIndexPageInfo.getList() ;
        List<ProductInfoBean> productInfoBeans = new ArrayList<>() ;
        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
        appSkuPrice.setRenterId(queryBean.getRenterId());
        aoyiProdIndices.forEach(aoyiProdIndex -> {
            // 查询star_sku表
            appSkuPrice.setMpu(aoyiProdIndex.getMpu());
            appSkuPrice.setSkuId(appSkuPrice.getSkuId());
            List<AppSkuPrice> appSkuPrices = appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
            if (appSkuPrices != null && appSkuPrices.size() >0) {
                aoyiProdIndex.setPrice(appSkuPrices.get(0).getPrice().toString());
            }

            aoyiProdIndex = productHandle.updateImageExample(aoyiProdIndex) ;
            ProductInfoBean infoBean = new ProductInfoBean() ;
            BeanUtils.copyProperties(aoyiProdIndex, infoBean);
            List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getMpu(), queryBean.getAppId());
            infoBean.setPromotion(promotionInfoBeans);
            productInfoBeans.add(infoBean) ;
        });
        productInfoBeanPageInfo.setList(productInfoBeans);
        return productInfoBeanPageInfo ;
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
        // 1. 首先将InventoryQueryBean 按照供应商分开，目前供应商先按照分为 唯品会 和 其他
        List<InventoryBean> weipinhuiSkuList = new ArrayList<>(); // 唯品会
        List<InventoryBean> otherSkuList = new ArrayList<>(); // 其他

        List<InventoryBean> inventoryBeanList = queryBean.getSkus();
        for (InventoryBean _inventroyBean : inventoryBeanList) {
            if (_inventroyBean.getSkuId().startsWith(ProductConstant.MERCHANTNO_WEIPINHUI) == true) { // 唯品会的sku
                weipinhuiSkuList.add(_inventroyBean);
            } else { // 其他供应商
                otherSkuList.add(_inventroyBean);
            }
        }

        // 2. 组装唯品会  和 其他 供应商的信息
        // 2.1 唯品会
        InventoryQueryBean weipinhuiInventoryQueryBean = new InventoryQueryBean();
        weipinhuiInventoryQueryBean.setCityId(queryBean.getCityId());
        weipinhuiInventoryQueryBean.setCountyId(queryBean.getCountyId());
        weipinhuiInventoryQueryBean.setSkus(weipinhuiSkuList);

        // 2.2 其他
        InventoryQueryBean otherInventoryQueryBean = new InventoryQueryBean();
        otherInventoryQueryBean.setCityId(queryBean.getCityId());
        otherInventoryQueryBean.setCountyId(queryBean.getCountyId());
        otherInventoryQueryBean.setSkus(otherSkuList);

        log.info("查询库存  按照供应商分开商品 weipinhuiInventoryQueryBean:{}, otherInventoryQueryBean:{}",
                JSONUtil.toJsonString(weipinhuiInventoryQueryBean), JSONUtil.toJsonString(otherInventoryQueryBean));

        // 4. 分别进行库存查询
        List<InventoryBean> inventoryBeans = new ArrayList<>() ;

        // 4.1 唯品会库存查询
        for (InventoryBean sku : weipinhuiInventoryQueryBean.getSkus()) {
            // 需要将库存信息处理成的对象
            InventoryBean inventoryBean = new InventoryBean() ;

            // 翻译地址
            WeipinhuiAddress weipinhuiAddress =
                    weipinhuiAddressDao.selectBySuningAddress(queryBean.getCountyId(), queryBean.getCityId(), 3);
            if (weipinhuiAddress == null) {
                log.warn("查询库存 未获取到唯品会的对应地址信息 countryId:{}, cityId:{}",
                        queryBean.getCountyId(), queryBean.getCityId());
            } else {
                // 查询spu信息
                StarSku starSku =  starSkuDao.selectBySkuId(sku.getSkuId());

                // 执行rpc查询库存
                AoyiQueryInventoryResDto aoyiQueryInventoryResDto =
                        aoyiClientRpcService.weipinhuiQueryItemInventory(starSku.getSpuId(), sku.getSkuId(),
                                Integer.valueOf(sku.getRemainNum()), weipinhuiAddress.getWphCode());

                if (aoyiQueryInventoryResDto != null && aoyiQueryInventoryResDto.getInventoy()) {
                    inventoryBean.setRemainNum(sku.getRemainNum());
                    inventoryBean.setState("1");
                } else {
                    inventoryBean = new InventoryBean() ;
                }

                }

            inventoryBean.setSkuId(sku.getSkuId());
            inventoryBeans.add(inventoryBean);
        }

        // 4.2 其他供应商的库存查询
        for (InventoryBean sku : otherInventoryQueryBean.getSkus()) {
            QueryInventory inventory = new QueryInventory();
            inventory.setCityId(queryBean.getCityId());
            inventory.setCountyId(queryBean.getCountyId());

            List<InventorySkus> ilist = new ArrayList<>();
            InventorySkus inventorySkus = new InventorySkus();
            inventorySkus.setNum(sku.getRemainNum());
            inventorySkus.setSkuId(sku.getSkuId());
            inventorySkus.setProdPrice(sku.getPrice());
            ilist.add(inventorySkus);
            inventory.setSkuIds(ilist);

            InventoryBean inventoryBean = new InventoryBean() ;
            AoyiProdIndex aoyiProdIndexX =  productDao.selectByMpu(sku.getSkuId());
            if (aoyiProdIndexX != null && "1".equals(aoyiProdIndexX.getState())) {
                OperaResponse<InventoryBean> operaResponse = aoyiClientService.inventory(inventory);
                inventoryBean = operaResponse.getData();
                if (inventoryBean != null) {
                    inventoryBean.setRemainNum(sku.getRemainNum());
                } else {
                    inventoryBean = new InventoryBean() ;
                }
            }

            inventoryBean.setSkuId(sku.getSkuId());
            inventoryBeans.add(inventoryBean);
        }

        operaResult.getData().put("result", inventoryBeans);
        log.info("查询库存 ProductServiceImpl#findInventory 返回:{}", JSONUtil.toJsonString(operaResult));

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
            aoyiProdIndex = productHandle.updateImage(aoyiProdIndex) ;
            prodIndices.add(aoyiProdIndex);
        });
        return prodIndices;
    }

    @Override
    public ProductInfoBean findAndPromotion(String mpu, String appId) throws ProductException {
        ProductInfoBean infoBean = new ProductInfoBean();
        AoyiProdIndexXWithBLOBs aoyiProdIndexX = findByMpu(mpu, appId) ;
        if (aoyiProdIndexX != null) {
            BeanUtils.copyProperties(aoyiProdIndexX, infoBean);
            List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndexX.getMpu(), appId);
            infoBean.setPromotion(promotionInfoBeans);
            List<CouponBean> couponBeans =  selectCouponBySku(aoyiProdIndexX, appId) ;
            infoBean.setCoupon(couponBeans);
        }
        return infoBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public AoyiProdIndexXWithBLOBs findByMpu(String mpu, String appId) {
        String renterId = vendorsRpcService.queryRenterId(appId) ;
        AoyiProdIndexXWithBLOBs aoyiProdIndexX = mapper.selectByMpu(mpu);
        productHandle.setProductXClient(aoyiProdIndexX, renterId);
//        if (aoyiProdIndexX != null) {
//            aoyiProdIndexX = productHandle.updateImageWithBLOBS(aoyiProdIndexX) ;
//        }
//        // 查询spu图片
//        if (StringUtils.isEmpty(aoyiProdIndexX.getImagesUrl())) {
//            List<StarDetailImg> starDetailImgs = starDetailImgDao.selectBySpuId(aoyiProdIndexX.getId()) ;
//            String imageUrl = "" ;
//            if (starDetailImgs != null && starDetailImgs.size() > 0) {
//                for (int i = 0; i < starDetailImgs.size(); i++) {
//                    if (i == 0) {
//                        imageUrl = starDetailImgs.get(i).getImgUrl() ;
//                    } else {
//                        imageUrl = imageUrl + ";" + starDetailImgs.get(i).getImgUrl() ;
//                    }
//                }
//                aoyiProdIndexX.setImagesUrl(imageUrl);
//            }
//        }
//        // 查询spu属性
//        List<StarProperty> starProperties = starPropertyDao.selectByProductIdAndType(aoyiProdIndexX.getId(), 0) ;
//        aoyiProdIndexX.setProperties(starProperties);
//        // 查询sku
//        List<StarSkuBean> starSkuBeans = new ArrayList<>() ;
//        List<StarSku> starSkus = starSkuDao.selectBySpuId(aoyiProdIndexX.getSkuid()) ;
//        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
//        appSkuPrice.setRenterId(renterId);
//        if (starSkus != null && starSkus.size() > 0) {
//            starSkus.forEach(starSku -> {
//                StarSkuBean starSkuBean = new StarSkuBean() ;
//                BeanUtils.copyProperties(starSku, starSkuBean);
//                // 查询sku属性
//                List<StarProperty> skuProperties = starPropertyDao.selectByProductIdAndType(starSku.getId(), 1) ;
//                starSkuBean.setPropertyList(skuProperties);
//                // 租户商品价格
//                appSkuPrice.setMpu(starSku.getSpuId());
//                appSkuPrice.setSkuId(starSku.getCode());
//                List<AppSkuPrice> appSkuPrices = appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
//                if (appSkuPrices != null && appSkuPrices.size() > 0) {
//                    starSkuBean.setPrice(appSkuPrices.get(0).getPrice().intValue());
//                }
//                starSkuBeans.add(starSkuBean) ;
//            });
//        } else {
//            appSkuPrice.setMpu(aoyiProdIndexX.getMpu());
//            appSkuPrice.setSkuId(aoyiProdIndexX.getSkuid());
//            List<AppSkuPrice> appSkuPrices = appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
//            if (appSkuPrices != null && appSkuPrices.size() > 0) {
//                BigDecimal bigDecimalPrice = appSkuPrice.getPrice().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP) ;
//                aoyiProdIndexX.setPrice(bigDecimalPrice.toString());
//            }
//            aoyiProdIndexX.setAppSkuPriceList(appSkuPrices);
//        }
//
//        aoyiProdIndexX.setSkuList(starSkuBeans);
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
        List<Integer> categoryIdList = new ArrayList<>();
        for (AoyiProdIndex aoyiProdIndex : aoyiProdIndexList) {
            String category = aoyiProdIndex.getCategory();

            if (StringUtils.isNotBlank(category)) {
                categoryIdList.add(Integer.valueOf(category));
            }
        }

        List<CategoryBean> categoryBeanList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(categoryIdList)) {
            categoryBeanList = categoryService.queryCategoryListByCategoryIdList(categoryIdList);
        }

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

    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiProdIndexX> selectProductListByMpuIdList(List<String> mpuIdList, String renterId) throws Exception {
        // 1. 查询商品信息
        log.info("根据mup集合查询产品信息 数据库查询参数:{}", JSONUtil.toJsonString(mpuIdList));
        List<AoyiProdIndexX> aoyiProdIndexList = new ArrayList<>();
        productDao.selectAoyiProdIndexListByMpuIdList(mpuIdList).forEach(aoyiProdIndex -> {
            AoyiProdIndexX aoyiProdIndexX = productHandle.convertProductXByProd(aoyiProdIndex, renterId) ;
            aoyiProdIndexList.add(aoyiProdIndexX);
        });
        log.info("根据mup集合查询产品信息 数据库返回:{}", JSONUtil.toJsonString(aoyiProdIndexList));
        return aoyiProdIndexList;
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
    public List<AoyiProdIndexX> getProdsByMpus(List<String> mpuIdList, String appId, String type) {
        // 1. 查询商品信息
        String renterId = vendorsRpcService.queryRenterId(appId) ;
        log.debug("根据mup集合查询产品信息 数据库查询参数:{}", JSONUtil.toJsonString(mpuIdList));
        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
        appSkuPrice.setRenterId(renterId);
        List<AoyiProdIndex> aoyiProdIndexList = productDao.selectAoyiProdIndexListByMpuIdList(mpuIdList);
        List<AoyiProdIndexX> prodIndexXList = aoyiProdIndexList.stream().map(prodIndex -> {
            AoyiProdIndexX prodIndexX = new AoyiProdIndexX();
            BeanUtils.copyProperties(prodIndex, prodIndexX);
            // 图片全路径
            productHandle.updateImage(prodIndexX) ;
            return prodIndexX;
        }).collect(Collectors.toList());
        productHandle.batchGetStarSkuListByMpuForClient(prodIndexXList, renterId, type) ;
        log.debug("根据mup集合查询产品信息 数据库返回:{}", JSONUtil.toJsonString(prodIndexXList));
        return prodIndexXList;
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
                    if (aoyiProdIndex.getInventory() != null && aoyiProdIndex.getInventory() >= inventory.getRemainNum() && "1".equals(aoyiProdIndex.getState())) {
                        inventory.setState("1");
                    }
                    inventories.add(inventory) ;
                }
            }
        });
        result.getData().put("result", inventories) ;
        log.info("根据mup集合查询库存信息 数据库查询返回结果:{}", JSONUtil.toJsonString(result));
        return result;
    }

    @Transactional
    @Override
    public OperaResult inventorySub(List<InventoryMpus> inventories) {
        log.info("扣减库存，入参{}", JSONUtil.toJsonString(inventories));
        OperaResult result = new OperaResult();
        for (InventoryMpus inventoryMpus : inventories) {
            try {
                result = inventoryDao.inventorySub(inventoryMpus) ;
            } catch (SQLException e) {
                log.info("扣减库存，异常{}", JSONUtil.toJsonString(inventories));
                log.info(e.getMessage());
                result.setCode(2000000);
                result.setMsg("扣减库存失败。");
                return result;
            }
        }
        log.info("扣减库存，返回值{}", JSONUtil.toJsonString(result));
        return result;
    }

    @Transactional
    @Override
    public OperaResult inventoryAdd(List<InventoryMpus> inventories) {
        log.info("添加库存，入参{}", JSONUtil.toJsonString(inventories));
        OperaResult result = new OperaResult();
        for (InventoryMpus inventoryMpus : inventories) {
            try {
                result = inventoryDao.inventoryAdd(inventoryMpus) ;
            } catch (SQLException e) {
                log.info("添加库存，异常{}", JSONUtil.toJsonString(inventories));
                e.printStackTrace();
            }
        }
        log.info("添加库存，返回值{}", JSONUtil.toJsonString(result));
        return result;
    }

    @Override
    public List<AoyiProdIndexX> selectProductListByMpuIdListAndCode(List<AoyiProdIndex> bean, String appId) {
        // 根据APPID查询renterId
        String renterId = vendorsRpcService.queryRenterId(appId) ;
        // 根据MPU查询商品表
        List<AoyiProdIndexX> aoyiProdIndexList = new ArrayList<>();
        bean.forEach(aoyiProdIndex -> {
            AoyiProdIndex aoyiProdIndex1 = productDao.selectByMpu(aoyiProdIndex.getMpu()) ;
            if (aoyiProdIndex1 != null) {
                AoyiProdIndexX aoyiProdIndexX = new AoyiProdIndexX() ;
                BeanUtils.copyProperties(aoyiProdIndex1, aoyiProdIndexX);
                productHandle.getProductXClientBySkuCode(aoyiProdIndexX, renterId, aoyiProdIndex.getSkuid()) ;
                aoyiProdIndexList.add(aoyiProdIndexX);
            }

        });

        log.debug("批量查询 selectProductListByMpuIdListAndCode 返回结果：{}", JSON.toJSONString(aoyiProdIndexList));

        return aoyiProdIndexList;
    }

    @Override
    public OperaResponse findSpuAndSku(String mpu, String code) {
        OperaResponse response = new OperaResponse() ;
        AoyiProdIndex aoyiProdIndex = productDao.selectByMpu(mpu) ;
        aoyiProdIndex = productHandle.updateImageExample(aoyiProdIndex) ;
        AoyiProdIndexX aoyiProdIndexX = new AoyiProdIndexX() ;
        BeanUtils.copyProperties(aoyiProdIndex, aoyiProdIndexX);
        List<String> codes = new ArrayList<>();
        codes.add(code);
        List<StarSku> starSkus = starSkuDao.selectByCodeList(codes) ;
        if (starSkus != null && starSkus.size() > 0) {
            StarSkuBean starSkuBean = new StarSkuBean();
            BeanUtils.copyProperties(starSkus.get(0), starSkuBean);
            List<StarProperty> starProperties = starPropertyDao.selectByProductIdAndType(starSkuBean.getId(), 1);
            starSkuBean.setPropertyList(starProperties);
            aoyiProdIndexX.setStarSku(starSkuBean);
        }
        response.setData(aoyiProdIndexX);
        log.debug("findSpuAndSku 返回结果：{}", JSON.toJSONString(aoyiProdIndexX));
        return response;
    }

    private List<CouponBean> selectCouponBySku(AoyiProdIndexX bean, String appId) {
        OperaResult result = equityService.selectCouponBySku(bean, appId);
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

    private List<PromotionInfoBean> findPromotionBySku(String skuId, String appId) {
        OperaResult result = equityService.findPromotionBySkuId(skuId, appId);
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
        productInfoBean.setTaxRate(aoyiProdIndex.getTaxRate());

        return productInfoBean;
    }

}
