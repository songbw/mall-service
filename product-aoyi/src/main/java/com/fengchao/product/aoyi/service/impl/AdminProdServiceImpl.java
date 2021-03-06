package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.constants.CategoryClassEnum;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.dao.CategoryDao;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.ProductExtendDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ExportProuctOverRangeException;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.service.AdminProdService;
//import com.fengchao.product.aoyi.utils.RedisUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.fengchao.product.aoyi.utils.ProductHandle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AdminProdServiceImpl implements AdminProdService {

    private static Logger logger = LoggerFactory.getLogger(AdminProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper aoyiProdIndexXMapper;
    @Autowired
    private ProdExtendXMapper prodExtendMapper;
    @Autowired
    private SkuCodeXMapper skuCodeMapper;
    @Autowired
    private EquityService equityService;
    @Autowired
    private VendorsServiceClient vendorsService;
    @Autowired
    private AoyiBaseCategoryXMapper categoryMapper;
    @Autowired
    private AoyiBaseBrandMapper brandMapper;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductExtendDao productExtendDao;

    @Autowired
    private VendorsRpcService vendorsRpcService;

    @Autowired
    private CategoryDao categoryDao;

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
        total = aoyiProdIndexXMapper.selectSearchCount(map);
        if (total > 0) {
            aoyiProdIndexXMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prods, total, offset, limit);
        return pageBean;
    }



    @DataSource(DataSourceNames.TWO)
    @Override
    @Deprecated
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
        total = aoyiProdIndexXMapper.selectSearchCount(map);
        if (total > 0) {
            aoyiProdIndexXMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }

        pageBean = PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectProductListPageable(SerachBean bean) {
        // 1.???????????????????????????
        HashMap sqlParamMap = new HashMap();
        sqlParamMap.put("name", bean.getQuery());
        sqlParamMap.put("skuid",bean.getSkuid());
        sqlParamMap.put("state",bean.getState());
        sqlParamMap.put("brand", bean.getBrand());
        sqlParamMap.put("mpu", bean.getMpu());
        if (bean.getMerchantHeader() > 0) { // ???????????????????????????????????????
            sqlParamMap.put("merchantId", bean.getMerchantHeader());
        } else if (bean.getMerchantHeader() == 0) { // ???????????????????????????????????????
            sqlParamMap.put("merchantId", bean.getMerchantId());
        }
        sqlParamMap.put("categoryID", bean.getCategoryID());

        int offset = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        sqlParamMap.put("offset", offset);
        sqlParamMap.put("pageSize", bean.getLimit());

        // 2.????????????
        // 2.1 ????????????
        int totalCount = aoyiProdIndexXMapper.selectSearchCount(sqlParamMap);

        // 2.2 ???????????????
        List<AoyiProdIndexX> aoyiProdIndexXList = new ArrayList<>(); // ????????????
        if (totalCount > 0) { // ??????????????????
            // ??????
            aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductListPageable(sqlParamMap);

            // ????????????
            for (AoyiProdIndexX aoyiProdIndexX : aoyiProdIndexXList) {
                ProductHandle.updateImage(aoyiProdIndexX);
            }
        }

        PageBean pageBean = new PageBean();
        PageBean.build(pageBean, aoyiProdIndexXList, totalCount, bean.getOffset(), bean.getLimit());

        return pageBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public int getProdListToRedis(){
        int num = 0;
        List<AoyiProdIndexX> aoyiProdIndices = aoyiProdIndexXMapper.selectProdAll();
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
    public String add(AoyiProdIndex requestProdParams) throws ProductException {
        // 1. ??????sku????????????
        List<AoyiProdIndex> aoyiProdIndexList =
                productDao.selectAoyiProdIndexListBySKUAndMerchant(requestProdParams.getSkuid(), requestProdParams.getMerchantId());
        if (CollectionUtils.isNotEmpty(aoyiProdIndexList)) {
            logger.warn("???????????? sku:{},merchantId:{} ?????????!", requestProdParams.getSkuid(), requestProdParams.getMerchantId());

            throw new ProductException(200001, "?????????sku");
        }
        // 2. ???????????????????????????SkuCode?????????????????????????????????????????????????????????(??????)??????merchantCode
        SkuCode skuCode = skuCodeMapper.selectByMerchantId(requestProdParams.getMerchantId());
        if (skuCode == null) {
            OperaResponse<SysCompany> vendorResponse = vendorsService.vendorInfo(requestProdParams.getMerchantId());
            if (vendorResponse.getCode() == 200) {
                SysCompany profileBean = vendorResponse.getData() ;
                if (profileBean != null) {
                    // ????????????code
                    SkuCode lastCode = skuCodeMapper.selectLast();
                    // logger.info(aoyiProdIndexX.getMerchantId() + "");
                    int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                    // ??????????????????
                    skuCode = new SkuCode();
                    skuCode.setMerchantId(requestProdParams.getMerchantId());
                    skuCode.setMerchantName(profileBean.getName());
                    skuCode.setMerchantCode(code + "");
                    skuCode.setSkuValue(0);
                    skuCode.setCreatedAt(new Date());
                    skuCode.setUpdatedAt(new Date());
                    skuCodeMapper.insertSelective(skuCode);
                } else {
                    logger.warn("???????????????????????? merchantId:{}?????????:{}", requestProdParams.getMerchantId());
                    throw new ProductException(200001, "vendor ???????????? " + vendorResponse.getMsg());
                }
            } else {
                logger.warn("???????????????????????? merchantId:{}?????????:{}", requestProdParams.getMerchantId());
                throw new ProductException(200001, "???????????????null");
            }
        }
        // 3. ????????????
        // ??????????????????
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(requestProdParams, aoyiProdIndexWithBLOBs);
//        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(requestProdParams);
        // ??????mpu
        String merchantCode = skuCode.getMerchantCode();
        int skuValue = skuCode.getSkuValue();
        AtomicInteger atomicInteger = new AtomicInteger(skuValue); // FIXME : ?????????????????????????????????!
        String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet());
        aoyiProdIndexWithBLOBs.setMpu(sku);
        // ???????????????SKU??????MPU?????????SKU
        if (aoyiProdIndexWithBLOBs.getSkuid() == null || "".equals(aoyiProdIndexWithBLOBs.getSkuid())) {
            aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndexWithBLOBs.getMpu());
        }

        // ????????????
        aoyiProdIndexWithBLOBs.setState(String.valueOf(ProductStatusEnum.INIT.getValue()));
        // ????????????
        if (requestProdParams.getBrandId() != null && requestProdParams.getBrandId() > 0) {
            AoyiBaseBrand baseBrand = brandMapper.selectByPrimaryKey(requestProdParams.getBrandId());
            if (baseBrand == null) {
                aoyiProdIndexWithBLOBs.setBrandId(0);
            }
        }
        // ????????????
        if (requestProdParams.getCategory() != null && !"".equals(requestProdParams.getCategory())) {
            AoyiBaseCategoryX category = categoryMapper.selectByPrimaryKey(Integer.parseInt(requestProdParams.getCategory()));
            if (category == null) {
                aoyiProdIndexWithBLOBs.setCategory("");
            }
        }
        // ????????????
        aoyiProdIndexWithBLOBs.setIprice(0);
        if (StringUtils.isNotBlank(requestProdParams.getPrice())) {
            BigDecimal bprice = new BigDecimal(requestProdParams.getPrice());
            int iprice = bprice.multiply(new BigDecimal(100)).intValue(); // ?????????????????????
            aoyiProdIndexWithBLOBs.setIprice(iprice);
        }
        // ????????????
        productDao.insert(aoyiProdIndexWithBLOBs);

        skuCode.setSkuValue(atomicInteger.get());
        skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);

        return aoyiProdIndexWithBLOBs.getMpu();
    }

    @Override
    public int update(AoyiProdIndex bean) throws ProductException {
        if (bean.getId() > 0) {
            bean.setUpdatedAt(new Date());
            productDao.updateAoyiProduct(bean) ;
//            aoyiProdIndexXMapper.updateByPrimaryKeySelective(bean);
        } else {
            throw new ProductException(200002, "id???null?????????0");
        }
        return bean.getId();
    }

    @Override
    public OperaResponse updateBatchPriceAndState(List<AoyiProdIndex> bean) throws ProductException {
        logger.info("????????????????????????????????????{}", JSONUtil.toJsonString(bean));
        OperaResponse operaResponse = new OperaResponse() ;
        if (bean == null || bean.size() <= 0) {
            operaResponse.setData(200003); ;
            operaResponse.setMsg("??????????????????????????????");
            return operaResponse ;
        }
        List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>() ;
        bean.forEach(aoyiProdIndex -> {
            if (StringUtils.isEmpty(aoyiProdIndex.getMpu()) || StringUtils.isEmpty(aoyiProdIndex.getPrice()) || StringUtils.isEmpty(aoyiProdIndex.getState())) {
                aoyiProdIndices.add(aoyiProdIndex) ;
            } else {
                productDao.updatePriceAndState(aoyiProdIndex);
            }
        });
        if (aoyiProdIndices.size() > 0) {
            operaResponse.setData(200004); ;
            operaResponse.setMsg("?????????????????????????????????");
            operaResponse.setData(aoyiProdIndices);
        }
        logger.info("??????????????????????????????????????????{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            aoyiProdIndexXMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id???null?????????0");
        }
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findProdAll(QueryProdBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        List<ProductInfoBean> infoBeans = new ArrayList<>();
        total = aoyiProdIndexXMapper.selectSkuByCouponIdCount(bean);
        if (total > 0) {
            aoyiProdIndexXMapper.selectSkuByCouponIdLimit(bean).forEach(aoyiProdIndex -> {
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


    @DataSource(DataSourceNames.TWO)
    @Override
    public List<ProductExportResVo> exportProductList(SerachBean bean) throws Exception {
        // 0. ?????????
        List<ProductExportResVo> productExportResVoList = new ArrayList<>();

        try {
            // 1.???????????????????????????
            HashMap sqlParamMap = new HashMap();
            sqlParamMap.put("name", bean.getQuery());
            sqlParamMap.put("skuid", bean.getSkuid());
            sqlParamMap.put("state", bean.getState());
            sqlParamMap.put("brand", bean.getBrand());
            sqlParamMap.put("mpu", bean.getMpu());
            if (bean.getMerchantHeader() > 0) { // ???????????????????????????????????????
                sqlParamMap.put("merchantId", bean.getMerchantHeader());
            } else if (bean.getMerchantHeader() == 0) { // ???????????????????????????????????????
                sqlParamMap.put("merchantId", bean.getMerchantId());
            }
            sqlParamMap.put("categoryID", bean.getCategoryID());
            logger.info("?????????????????? ???????????????:{}", JSONUtil.toJsonString(sqlParamMap));

            // 2. ?????????????????????????????????
            // 2.1 ????????????????????????
            List<SysCompany> sysCompanyList = vendorsRpcService.queryAllMerchantList();
            // ???map key:id, value:SysCompany
            Map<Long, SysCompany> sysCompanyMap = sysCompanyList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

            // 2.2 ???????????????????????????
            // 2.2.1 ????????????????????????
            List<AoyiBaseCategory> categorysWithClass1
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL1.getValue()));

            logger.info("?????????????????? ??????????????????????????????List<AoyiBaseCategory>:{}", JSONUtil.toJsonString(categorysWithClass1));

            // ???map key:categoryId, value:AoyiBaseCategory
            Map<Integer, AoyiBaseCategory> categoryMapWithClass1 =
                    categorysWithClass1.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            // 2.2.2 ????????????????????????
            List<AoyiBaseCategory> categorysWithClass2
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL2.getValue()));
            // ?????????????????????????????????????????????
            for (AoyiBaseCategory categoryWithClass2 : categorysWithClass2) {
                // ????????????????????????????????????
                AoyiBaseCategory categoryWithClass1 = categoryMapWithClass1.get(categoryWithClass2.getParentId());

                String _categoryName = "--";
                if (categoryWithClass1 == null) {
                    logger.warn("?????????????????? ????????????code:{} ?????????????????????????????????", categoryWithClass2.getCategoryId());
                } else {
                    _categoryName = categoryWithClass1.getCategoryName() + "/" + categoryWithClass2.getCategoryName();
                }

                categoryWithClass2.setCategoryName(_categoryName);
            }

            // ???map
            Map<Integer, AoyiBaseCategory> categoryMapWithClass2 =
                    categorysWithClass2.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            // 2.2.3 ????????????????????????
            List<AoyiBaseCategory> categorysWithClass3
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL3.getValue()));

            // ?????????????????????????????????????????????
            for (AoyiBaseCategory categoryWithClass3 : categorysWithClass3) {
                // ????????????????????????????????????
                AoyiBaseCategory categoryWithClass2 = categoryMapWithClass2.get(categoryWithClass3.getParentId());

                String _categoryName = "--";
                if (categoryWithClass2 == null) {
                    logger.warn("?????????????????? ????????????code:{} ?????????????????????????????????", categoryWithClass3.getCategoryId());
                } else {
                    _categoryName = categoryWithClass2.getCategoryName() + "/" + categoryWithClass3.getCategoryName();
                }

                categoryWithClass3.setCategoryName(_categoryName);
            }

            // ???map
            Map<Integer, AoyiBaseCategory> categoryMapWithClass3 =
                    categorysWithClass3.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            logger.info("?????????????????? ?????????????????????:{}???", categoryMapWithClass3.size());

            // 3.??????????????????
            // 3.1 ?????????????????????-????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ???????????????
            int totalCount = aoyiProdIndexXMapper.selectSearchCount(sqlParamMap);
            if (totalCount > 50000) { // ??????????????????????????????5w???????????????,excel???????????????65535
                logger.error("?????????????????? ??????????????????");
                throw new ExportProuctOverRangeException();
            }
            // ????????????
            if (totalCount > 0) {
                PageBean pageBean = new PageBean();
                int pageSize = 500;
                int totalPage = pageBean.getPages(totalCount, pageSize); // ?????????

                for (int currentPage = 1; currentPage <= totalPage; currentPage++) {
                    int offset = pageBean.getOffset(currentPage, pageSize);

                    sqlParamMap.put("offset", offset);
                    sqlParamMap.put("pageSize", 500);

                    // ??????????????????
                    List<AoyiProdIndexX> aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductListPageable(sqlParamMap);

                    // ???exportVo
                    for (AoyiProdIndexX aoyiProdIndexX : aoyiProdIndexXList) {
                        ProductExportResVo productExportResVo = convertToProductExportResVo(aoyiProdIndexX);

                        // ???????????????????????????
                        SysCompany sysCompany = sysCompanyMap.get(aoyiProdIndexX.getMerchantId().longValue());
                        productExportResVo.setMerchantName(sysCompany == null ? "/" : sysCompany.getName());

                        // ??????????????????
                        AoyiBaseCategory aoyiBaseCategory = null;
                        if (StringUtils.isNotBlank(aoyiProdIndexX.getCategory())) {
                            aoyiBaseCategory = categoryMapWithClass3.get(Integer.valueOf(aoyiProdIndexX.getCategory()));
                        }
                        productExportResVo.setCategory(aoyiBaseCategory == null ? "/" : aoyiBaseCategory.getCategoryName());

                        //
                        productExportResVoList.add(productExportResVo);

                        aoyiProdIndexX = null; // ??????
                    }

                }
            }
        } catch (ExportProuctOverRangeException e) {
            logger.error("?????????????????? ?????????:{}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            logger.error("?????????????????? ??????:{}", e.getMessage(), e);

            throw e;
        }

        logger.info("?????????????????? ????????????????????????List<ProductExportResVo> size:{}", productExportResVoList.size());

        return productExportResVoList;
    }

    @Override
    public PageBean exportProductPriceList(float floorPriceRate, int pageNo, int pageSize) throws Exception {
        PageBean pageBean = new PageBean();
        // 0. ?????????
        List<ProductExportResVo> productExportResVoList = new ArrayList<>();

        try {
            // 1.???????????????????????????
            HashMap sqlParamMap = new HashMap();
            sqlParamMap.put("floorPriceRate", floorPriceRate);

            logger.info("???????????????????????? ???????????????:{}", JSONUtil.toJsonString(sqlParamMap));

            // 2. ?????????????????????????????????
            // 2.1 ????????????????????????
            List<SysCompany> sysCompanyList = vendorsRpcService.queryAllMerchantList();
            // ???map key:id, value:SysCompany
            Map<Long, SysCompany> sysCompanyMap = sysCompanyList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

            // 3.??????????????????
            // 3.1 ?????????????????????-????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // TODO ???????????????
            int totalCount = aoyiProdIndexXMapper.selectPriceCount(sqlParamMap);
//            if (totalCount > 50000) { // ??????????????????????????????5w???????????????,excel???????????????65535
//                logger.error("???????????????????????? ??????????????????");
//                throw new ExportProuctOverRangeException();
//            }
            // ????????????
            if (totalCount > 0) {
                int totalPage = pageBean.getPages(totalCount, pageSize); // ?????????
                pageBean.setPageNo(pageNo);
                pageBean.setPages(totalPage);
                pageBean.setPageSize(pageSize);
                pageBean.setTotal(totalCount);
                int offset = (pageNo - 1) * pageSize ;
                sqlParamMap.put("offset", offset);
                sqlParamMap.put("pageSize", pageSize);

                // TODO ??????????????????
                List<ProductExportResVo> aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductPriceListPageable(sqlParamMap);
                aoyiProdIndexXList.forEach(productExportResVo -> {
                    productExportResVoList.add(productExportResVo);
                });
                pageBean.setList(productExportResVoList);

//                for (int currentPage = 1; currentPage <= totalPage; currentPage++) {
//                    int offset = pageBean.getOffset(currentPage, pageSize);
//
//                    sqlParamMap.put("offset", offset);
//                    sqlParamMap.put("pageSize", pageSize);
//
//                    // TODO ??????????????????
//                    List<ProductExportResVo> aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductPriceListPageable(sqlParamMap);
//                    aoyiProdIndexXList.forEach(productExportResVo -> {
//                        productExportResVoList.add(productExportResVo);
//                    });
//
//                    // ???exportVo
////                    for (AoyiProdIndexX aoyiProdIndexX : aoyiProdIndexXList) {
////                        ProductExportResVo productExportResVo = convertToProductExportResVo(aoyiProdIndexX);
////
////                        // ???????????????????????????
////                        SysCompany sysCompany = sysCompanyMap.get(aoyiProdIndexX.getMerchantId().longValue());
////                        productExportResVo.setMerchantName(sysCompany == null ? "/" : sysCompany.getName());
////
////                        //
////                        productExportResVoList.add(productExportResVo);
////
////                        aoyiProdIndexX = null; // ??????
////                    }
//
//                }
            }
        } catch (ExportProuctOverRangeException e) {
            logger.error("???????????????????????? ?????????:{}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            logger.error("???????????????????????? ??????:{}", e.getMessage(), e);

            throw e;
        }

        logger.info("???????????????????????? ????????????????????????List<ProductExportResVo> size:{}", productExportResVoList.size());

        return pageBean;
    }

    // =============================== private ======================================================

    /**
     * ?????????????????? AoyiProdIndex
     *
     * @param aoyiProdIndexX
     * @return
     */
    private AoyiProdIndexWithBLOBs convertToAoyiProdIndexWithBLOBs(AoyiProdIndexX aoyiProdIndexX) {
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();

        aoyiProdIndexWithBLOBs.setId(aoyiProdIndexX.getId());
        aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndexX.getSkuid());
        aoyiProdIndexWithBLOBs.setBrand(aoyiProdIndexX.getBrand());
        aoyiProdIndexWithBLOBs.setCategory(aoyiProdIndexX.getCategory());
        aoyiProdIndexWithBLOBs.setImage(aoyiProdIndexX.getImage());
        aoyiProdIndexWithBLOBs.setModel(aoyiProdIndexX.getModel());
        aoyiProdIndexWithBLOBs.setName(aoyiProdIndexX.getName());
        aoyiProdIndexWithBLOBs.setWeight(aoyiProdIndexX.getWeight());
        aoyiProdIndexWithBLOBs.setUpc(aoyiProdIndexX.getUpc());
        aoyiProdIndexWithBLOBs.setSaleunit(aoyiProdIndexX.getSaleunit());
        aoyiProdIndexWithBLOBs.setState(aoyiProdIndexX.getState());
        aoyiProdIndexWithBLOBs.setPrice(aoyiProdIndexX.getPrice());
        aoyiProdIndexWithBLOBs.setSprice(aoyiProdIndexX.getSprice());
        aoyiProdIndexWithBLOBs.setImagesUrl(aoyiProdIndexX.getImagesUrl());
        aoyiProdIndexWithBLOBs.setIntroductionUrl(aoyiProdIndexX.getIntroductionUrl());
        aoyiProdIndexWithBLOBs.setMerchantId(aoyiProdIndexX.getMerchantId());
        aoyiProdIndexWithBLOBs.setInventory(aoyiProdIndexX.getInventory());
        aoyiProdIndexWithBLOBs.setBrandId(aoyiProdIndexX.getBrandId());
        aoyiProdIndexWithBLOBs.setMpu(aoyiProdIndexX.getMpu());

        return aoyiProdIndexWithBLOBs;
    }

    private ProductExportResVo convertToProductExportResVo(AoyiProdIndexX aoyiProdIndexX) {
        ProductExportResVo productExportResVo = new ProductExportResVo();

        // productExportResVo.setMerchantName(aoyiProdIndexX.); // ???????????????
        productExportResVo.setSku(aoyiProdIndexX.getSkuid()); // ??????SKU

        // ????????????
        Integer state = Integer.valueOf(aoyiProdIndexX.getState()); // -1:???????????????0????????????1?????????
        ProductStatusEnum productStatusEnum = ProductStatusEnum.getProductStatusEnum(state);
        productExportResVo.setState(productStatusEnum.getDesc()); // ????????????

        productExportResVo.setProductName(aoyiProdIndexX.getName()); // ????????????
        productExportResVo.setBrand(aoyiProdIndexX.getBrand()); // ????????????
        // productExportResVo.setCategory(); // ????????????
        productExportResVo.setModel(aoyiProdIndexX.getModel()); // ????????????
        productExportResVo.setWeight(aoyiProdIndexX.getWeight()); // ????????????
        productExportResVo.setUpc(aoyiProdIndexX.getUpc()); // ???????????????
        productExportResVo.setSellPrice(aoyiProdIndexX.getPrice()); // ????????????(???)
        productExportResVo.setCostPrice(aoyiProdIndexX.getSprice()); // ????????????(???)  ????????????
        productExportResVo.setInventory(aoyiProdIndexX.getInventory() == null ? "???" : String.valueOf(aoyiProdIndexX.getInventory())); // ??????

        return productExportResVo;
    }

    @Override
    public OperaResult inventoryUpdate(InventoryMpus inventory) {
        OperaResult result = new OperaResult();
        AoyiProdIndexX prodIndexX = aoyiProdIndexXMapper.selectForUpdateByMpu(inventory.getMpu()) ;
        if (prodIndexX != null) {
            AoyiProdIndexX temp = new AoyiProdIndexX();
            temp.setMpu(prodIndexX.getMpu());
            temp.setUpdatedAt(new Date());
            temp.setInventory(prodIndexX.getInventory() - inventory.getRemainNum());
            aoyiProdIndexXMapper.updateByPrimaryKeySelective(temp) ;
            result.getData().put("id", prodIndexX.getId()) ;
        }
        return result;
    }

    @Override
    public void fix() {
        List<AoyiProdIndex> aoyiProdIndexList =  productDao.selectFix() ;
        aoyiProdIndexList.forEach(aoyiProdIndex -> {
            SkuCode skuCode = skuCodeMapper.selectByMerchantId(aoyiProdIndex.getMerchantId());
            if (skuCode == null) {
                OperaResponse<SysCompany> vendorResponse = vendorsService.vendorInfo(aoyiProdIndex.getMerchantId());
                if (vendorResponse.getCode() == 200) {
                    SysCompany profileBean = vendorResponse.getData() ;
                    if (profileBean != null) {
                        // ????????????code
                        SkuCode lastCode = skuCodeMapper.selectLast();
                        // logger.info(aoyiProdIndexX.getMerchantId() + "");
                        int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                        // ??????????????????
                        skuCode = new SkuCode();
                        skuCode.setMerchantId(aoyiProdIndex.getMerchantId());
                        skuCode.setMerchantName(profileBean.getName());
                        skuCode.setMerchantCode(code + "");
                        skuCode.setSkuValue(0);
                        skuCode.setCreatedAt(new Date());
                        skuCode.setUpdatedAt(new Date());
                        skuCodeMapper.insertSelective(skuCode);
                    } else {
                        logger.warn("???????????????????????? merchantId:{}?????????:{}", aoyiProdIndex.getMerchantId());
                        throw new ProductException(200001, "vendor ???????????? " + vendorResponse.getMsg());
                    }
                } else {
                    logger.warn("???????????????????????? merchantId:{}?????????:{}", aoyiProdIndex.getMerchantId());
                    throw new ProductException(200001, "???????????????null");
                }
            }
            AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
            BeanUtils.copyProperties(aoyiProdIndex, aoyiProdIndexWithBLOBs);
            String merchantCode = skuCode.getMerchantCode();
            int skuValue = skuCode.getSkuValue();
            AtomicInteger atomicInteger = new AtomicInteger(skuValue);
            String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet());
            aoyiProdIndexWithBLOBs.setMpu(sku);
            aoyiProdIndexWithBLOBs.setSkuid(sku);

            // ????????????
            productDao.updateFix(aoyiProdIndexWithBLOBs);

            skuCode.setSkuValue(atomicInteger.get());
            skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);
        });
    }
}
