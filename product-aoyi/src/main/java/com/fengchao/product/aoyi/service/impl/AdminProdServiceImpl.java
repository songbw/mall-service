package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.constants.CategoryClassEnum;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.exception.ExportProuctOverRangeException;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.RenterCompany;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.service.AdminProdService;
//import com.fengchao.product.aoyi.utils.RedisUtil;
import com.fengchao.product.aoyi.utils.DateUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.fengchao.product.aoyi.utils.ProductHandle;
import com.github.pagehelper.PageInfo;
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
    private AoyiProdIndexMapper mapper ;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductExtendDao productExtendDao;
    @Autowired
    private VendorsRpcService vendorsRpcService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private StarSkuMapper starSkuMapper ;
    @Autowired
    private StarSkuDao starSkuDao ;
    @Autowired
    private StarDetailImgDao starDetailImgDao ;

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
                aoyiProdIndex = ProductHandle.updateImageWithBLOBS(aoyiProdIndex) ;
                prods.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prods, total, offset, limit);
        return pageBean;
    }

    @Override
    public PageInfo<AoyiProdIndex> findProdListV2(ProductQueryBean queryBean) {
        if (queryBean.getMerchantId() == 0) {
            // TODO 获取租户下的所有商户信息
            List<Integer> merchantIds = new ArrayList<>() ;
            queryBean.setMerchantIds(merchantIds);
        } else {
            queryBean.setMerchantId(queryBean.getMerchantId());
        }
        PageInfo<AoyiProdIndex> pageInfoBean = new PageInfo<>() ;
        List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>() ;
        PageInfo<AoyiProdIndex> pageInfo = productDao.selectPageable(queryBean) ;
        BeanUtils.copyProperties(pageInfo, pageInfoBean);
        List<AoyiProdIndex> list = pageInfo.getList();
        if (list != null) {
            list.forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImageExample(aoyiProdIndex) ;
                aoyiProdIndices.add(aoyiProdIndex) ;
            });
        }
        pageInfoBean.setList(aoyiProdIndices);
        return pageInfoBean;
    }

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
        map.put("minPrice",bean.getMinPrice());
        map.put("maxPrice",bean.getMaxPrice());
        if (StringUtils.isNotBlank(bean.getMpuPrefix())) {
            map.put("mpuPrefix",bean.getMpuPrefix() + "%");
        }

        if (bean.getMerchantHeader() == 0) {
            map.put("merchantId", bean.getMerchantId());
        } else if (bean.getMerchantHeader().equals(bean.getMerchantId())) {
            map.put("merchantId", bean.getMerchantId());
        } else {
            return PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        }
        List<String> spus = new ArrayList<>() ;
        total = aoyiProdIndexXMapper.selectSearchCount(map);
        if (total > 0) {
            aoyiProdIndexXMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
                spus.add(aoyiProdIndex.getSkuid()) ;
                aoyiProdIndex = ProductHandle.updateImageWithBLOBS(aoyiProdIndex) ;
                List<StarSku> starSkus = starSkuDao.selectBySpuId(aoyiProdIndex.getSkuid()) ;
                List<StarSkuBean> starSkuBeans = new ArrayList<>() ;
                starSkus.forEach(starSku -> {
                    StarSkuBean starSkuBean = new StarSkuBean() ;
                    BeanUtils.copyProperties(starSku, starSkuBean);
                    starSkuBeans.add(starSkuBean) ;
                });
                aoyiProdIndex.setSkuList(starSkuBeans);
                prods.add(aoyiProdIndex);
            });
        }

        pageBean = PageBean.build(pageBean, prods, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public PageInfo<AoyiProdIndexX> selectNameListV2(ProductQueryBean queryBean) {
        if ("0".equals(queryBean.getRenterHeader())) {
            // 平台管理员
            // 获取所有租户下的所有商户信息
            List<RenterCompany> renterCompanyList = vendorsRpcService.queryRenterMerhantList(1,10000, null) ;
            List<Integer> merchantIds = renterCompanyList.stream().map(x ->x.getCompanyId()).collect(Collectors.toList());
            //  判断商户中是否存在merchantId
            if (merchantIds.contains(queryBean.getMerchantId()))  {
                queryBean.setMerchantIds(null);
            } else {
                queryBean.setMerchantIds(merchantIds);
            }
        } else {
            // 租户
            if (queryBean.getMerchantHeader() == 0) {
                // 获取当前租户下的所有商户信息
                List<RenterCompany> renterCompanyList = vendorsRpcService.queryRenterMerhantList(1,10000, queryBean.getRenterHeader()) ;
                List<Integer> merchantIds = renterCompanyList.stream().map(x ->x.getCompanyId()).collect(Collectors.toList());
                queryBean.setMerchantIds(merchantIds);
            }
        }
        PageInfo<AoyiProdIndexX> pageInfoBean = new PageInfo<>() ;
        List<AoyiProdIndexX> aoyiProdIndices = new ArrayList<>() ;
        PageInfo<AoyiProdIndex> pageInfo = productDao.selectPageable(queryBean) ;
        BeanUtils.copyProperties(pageInfo, pageInfoBean);
        List<AoyiProdIndex> list = pageInfo.getList();
        if (list != null) {
            list.forEach(aoyiProdIndex -> {
                aoyiProdIndex = ProductHandle.updateImageExample(aoyiProdIndex) ;
                AoyiProdIndexX aoyiProdIndexX = new AoyiProdIndexX() ;
                BeanUtils.copyProperties(aoyiProdIndex, aoyiProdIndexX);
                List<StarSku> starSkus = starSkuDao.selectBySpuId(aoyiProdIndex.getSkuid()) ;
                List<StarSkuBean> starSkuBeans = new ArrayList<>() ;
                starSkus.forEach(starSku -> {
                    StarSkuBean starSkuBean = new StarSkuBean() ;
                    BeanUtils.copyProperties(starSku, starSkuBean);
                    starSkuBeans.add(starSkuBean) ;
                });
                aoyiProdIndexX.setSkuList(starSkuBeans);
                aoyiProdIndices.add(aoyiProdIndexX) ;
            });
        }
        pageInfoBean.setList(aoyiProdIndices);
        return pageInfoBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectProductListPageable(SerachBean bean) {
        // 1.组装数据库查询参数
        HashMap sqlParamMap = new HashMap();
        sqlParamMap.put("name", bean.getQuery());
        sqlParamMap.put("skuid",bean.getSkuid());
        sqlParamMap.put("state",bean.getState());
        sqlParamMap.put("brand", bean.getBrand());
        sqlParamMap.put("mpu", bean.getMpu());
        if (bean.getMerchantHeader() > 0) { // 表示该次查询的操作方是商户
            sqlParamMap.put("merchantId", bean.getMerchantHeader());
        } else if (bean.getMerchantHeader() == 0) { // 表示该次查询的操作方是平台
            sqlParamMap.put("merchantId", bean.getMerchantId());
        }
        sqlParamMap.put("categoryID", bean.getCategoryID());

        int offset = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        sqlParamMap.put("offset", offset);
        sqlParamMap.put("pageSize", bean.getLimit());

        // 2.查询数据
        // 2.1 查询条数
        int totalCount = aoyiProdIndexXMapper.selectSearchCount(sqlParamMap);

        // 2.2 查询商品表
        List<AoyiProdIndexX> aoyiProdIndexXList = new ArrayList<>(); // 结果数据
        if (totalCount > 0) { // 如果存在数据
            // 查询
            aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductListPageable(sqlParamMap);

            // 组装数据
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
        // 1. 判断sku是否重复
        List<AoyiProdIndex> aoyiProdIndexList =
                productDao.selectAoyiProdIndexListBySKUAndMerchant(requestProdParams.getSkuid(), requestProdParams.getMerchantId());
        if (CollectionUtils.isNotEmpty(aoyiProdIndexList)) {
            logger.warn("创建商品 sku:{},merchantId:{} 已存在!", requestProdParams.getSkuid(), requestProdParams.getMerchantId());

            throw new ProductException(200001, "重复的sku");
        }
        // 2. 处理商户信息，如果SkuCode表中不存在该商户信息，那么为该商户分配(创建)一个merchantCode
        SkuCode skuCode = skuCodeMapper.selectByMerchantId(requestProdParams.getMerchantId());
        if (skuCode == null) {
            OperaResponse<SysCompany> vendorResponse = vendorsService.vendorInfo(requestProdParams.getMerchantId());
            if (vendorResponse.getCode() == 200) {
                SysCompany profileBean = vendorResponse.getData() ;
                if (profileBean != null) {
                    // 获取最新code
                    SkuCode lastCode = skuCodeMapper.selectLast();
                    // logger.info(aoyiProdIndexX.getMerchantId() + "");
                    int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                    // 添加商户信息
                    skuCode = new SkuCode();
                    skuCode.setMerchantId(requestProdParams.getMerchantId());
                    skuCode.setMerchantName(profileBean.getName());
                    skuCode.setMerchantCode(code + "");
                    skuCode.setSkuValue(0);
                    skuCode.setCreatedAt(new Date());
                    skuCode.setUpdatedAt(new Date());
                    skuCodeMapper.insertSelective(skuCode);
                } else {
                    logger.warn("未获取到商户信息 merchantId:{}信息为:{}", requestProdParams.getMerchantId());
                    throw new ProductException(200001, "vendor 服务失败 " + vendorResponse.getMsg());
                }
            } else {
                logger.warn("未获取到商户信息 merchantId:{}信息为:{}", requestProdParams.getMerchantId());
                throw new ProductException(200001, "商户信息为null");
            }
        }
        // 3. 创建商品
        // 转数据库实体
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
        BeanUtils.copyProperties(requestProdParams, aoyiProdIndexWithBLOBs);
//        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(requestProdParams);
        // 设置mpu
        String merchantCode = skuCode.getMerchantCode();
        int skuValue = skuCode.getSkuValue();
        AtomicInteger atomicInteger = new AtomicInteger(skuValue); // FIXME : 这个原子操作好像没用呀!
        String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet());
        aoyiProdIndexWithBLOBs.setMpu(sku);
        aoyiProdIndexWithBLOBs.setMerchantCode(merchantCode);
        // 如果没有传SKU则将MPU赋值给SKU
        if (aoyiProdIndexWithBLOBs.getSkuid() == null || "".equals(aoyiProdIndexWithBLOBs.getSkuid())) {
            aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndexWithBLOBs.getMpu());
        }

        // 设置状态
        aoyiProdIndexWithBLOBs.setState(String.valueOf(ProductStatusEnum.INIT.getValue()));
        // 设置品牌
        if (requestProdParams.getBrandId() != null && requestProdParams.getBrandId() > 0) {
            AoyiBaseBrand baseBrand = brandMapper.selectByPrimaryKey(requestProdParams.getBrandId());
            if (baseBrand == null) {
                aoyiProdIndexWithBLOBs.setBrandId(0);
            }
        }
        // 设置类别
        if (requestProdParams.getCategory() != null && !"".equals(requestProdParams.getCategory())) {
            AoyiBaseCategoryX category = categoryMapper.selectByPrimaryKey(Integer.parseInt(requestProdParams.getCategory()));
            if (category == null) {
                aoyiProdIndexWithBLOBs.setCategory("");
            }
        }
        // 设置价格
        aoyiProdIndexWithBLOBs.setIprice(0);
        if (StringUtils.isNotBlank(requestProdParams.getPrice())) {
            BigDecimal bprice = new BigDecimal(requestProdParams.getPrice());
            int iprice = bprice.multiply(new BigDecimal(100)).intValue(); // 价格，单位：分
            aoyiProdIndexWithBLOBs.setIprice(iprice);
        }
        // 执行插入
        productDao.insert(aoyiProdIndexWithBLOBs);

        skuCode.setSkuValue(atomicInteger.get());
        skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);

        return aoyiProdIndexWithBLOBs.getMpu();
    }

    @Override
    public int update(AoyiProdIndex bean) throws ProductException {
        if (bean.getId() > 0) {
            bean.setUpdatedAt(new Date());
            AoyiProdIndexWithBLOBs temp = mapper.selectByPrimaryKey(bean.getId()) ;
            List<StarSku> starSkus = starSkuDao.selectBySpuId(temp.getSkuid()) ;
            if (starSkus != null && starSkus.size() > 0) {
                StarSku starSku = starSkus.get(0) ;
                if (StringUtils.isNotBlank(bean.getPrice())) {
                    BigDecimal bigDecimalPrice = new BigDecimal(bean.getPrice()) ;
                    int price = bigDecimalPrice.multiply(new BigDecimal("100")).intValue() ;
                    starSku.setPrice(price);
                }
                if (StringUtils.isNotBlank(bean.getState())) {
                    starSku.setStatus(Integer.valueOf(bean.getState()));
                }
                starSkuMapper.updateByPrimaryKeySelective(starSku) ;
            }
            productDao.updateAoyiProduct(bean) ;
//            aoyiProdIndexXMapper.updateByPrimaryKeySelective(bean);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
        return bean.getId();
    }

    @Override
    public OperaResponse updateBatchPriceAndState(List<AoyiProdIndex> bean) throws ProductException {
        logger.info("批量更新价格状态，入参：{}", JSONUtil.toJsonString(bean));
        OperaResponse operaResponse = new OperaResponse() ;
        if (bean == null || bean.size() <= 0) {
            operaResponse.setData(200003); ;
            operaResponse.setMsg("批量更新列表不能为空");
            return operaResponse ;
        }
        List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>() ;
        bean.forEach(aoyiProdIndex -> {
            if (StringUtils.isEmpty(aoyiProdIndex.getMpu())) {
                aoyiProdIndices.add(aoyiProdIndex) ;
            } else {
                productDao.updatePriceAndState(aoyiProdIndex);
            }
        });
        if (aoyiProdIndices.size() > 0) {
            operaResponse.setData(200004); ;
            operaResponse.setMsg("存在未更新成功的数据。");
            operaResponse.setData(aoyiProdIndices);
        }
        logger.info("批量更新价格状态，返回结果：{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            aoyiProdIndexXMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findProdAll(QueryProdBean bean, String appId) {
        PageBean pageBean = new PageBean();
        int total = 0;
        List<ProductInfoBean> infoBeans = new ArrayList<>();
        total = aoyiProdIndexXMapper.selectSkuByCouponIdCount(bean);
        if (total > 0) {
            aoyiProdIndexXMapper.selectSkuByCouponIdLimit(bean).forEach(aoyiProdIndex -> {
                ProductInfoBean infoBean = new ProductInfoBean();
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex) ;
                BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                OperaResult operaResult = equityService.findPromotionBySkuId(aoyiProdIndex.getSkuid(), appId);
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
        // 0. 返回值
        List<ProductExportResVo> productExportResVoList = new ArrayList<>();

        try {
            // 1.组装数据库查询参数
            HashMap sqlParamMap = new HashMap();
            sqlParamMap.put("name", bean.getQuery());
            sqlParamMap.put("skuid", bean.getSkuid());
            sqlParamMap.put("state", bean.getState());
            sqlParamMap.put("brand", bean.getBrand());
            sqlParamMap.put("mpu", bean.getMpu());
            if (bean.getMerchantHeader() > 0) { // 表示该次查询的操作方是商户
                sqlParamMap.put("merchantId", bean.getMerchantHeader());
            } else if (bean.getMerchantHeader() == 0) { // 表示该次查询的操作方是平台
                sqlParamMap.put("merchantId", bean.getMerchantId());
            }
            sqlParamMap.put("categoryID", bean.getCategoryID());
            logger.info("导出商品列表 查询条件为:{}", JSONUtil.toJsonString(sqlParamMap));

            // 2. 查询导出需要的相关数据
            // 2.1 查询所有商户信息
            List<SysCompany> sysCompanyList = vendorsRpcService.queryAllMerchantList();
            // 转map key:id, value:SysCompany
            Map<Long, SysCompany> sysCompanyMap = sysCompanyList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

            // 2.2 查询所有的分类信息
            // 2.2.1 查询一级品类名称
            List<AoyiBaseCategory> categorysWithClass1
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL1.getValue()));

            logger.info("导出商品列表 获取一级品类信息集合List<AoyiBaseCategory>:{}", JSONUtil.toJsonString(categorysWithClass1));

            // 转map key:categoryId, value:AoyiBaseCategory
            Map<Integer, AoyiBaseCategory> categoryMapWithClass1 =
                    categorysWithClass1.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            // 2.2.2 查询二级品类名称
            List<AoyiBaseCategory> categorysWithClass2
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL2.getValue()));
            // 将二级品类名称加入一级品类名称
            for (AoyiBaseCategory categoryWithClass2 : categorysWithClass2) {
                // 获取该二级品类的一级品类
                AoyiBaseCategory categoryWithClass1 = categoryMapWithClass1.get(categoryWithClass2.getParentId());

                String _categoryName = "--";
                if (categoryWithClass1 == null) {
                    logger.warn("导出商品列表 二级品类code:{} 没有找到对应的一级品类", categoryWithClass2.getCategoryId());
                } else {
                    _categoryName = categoryWithClass1.getCategoryName() + "/" + categoryWithClass2.getCategoryName();
                }

                categoryWithClass2.setCategoryName(_categoryName);
            }

            // 转map
            Map<Integer, AoyiBaseCategory> categoryMapWithClass2 =
                    categorysWithClass2.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            // 2.2.3 查询三级品类名称
            List<AoyiBaseCategory> categorysWithClass3
                    = categoryDao.selectByCategoryClass(String.valueOf(CategoryClassEnum.LEVEL3.getValue()));

            // 将三级品类名称加入二级品类名称
            for (AoyiBaseCategory categoryWithClass3 : categorysWithClass3) {
                // 获取该三级品类的二级品类
                AoyiBaseCategory categoryWithClass2 = categoryMapWithClass2.get(categoryWithClass3.getParentId());

                String _categoryName = "--";
                if (categoryWithClass2 == null) {
                    logger.warn("导出商品列表 三级品类code:{} 没有找到对应的二级品类", categoryWithClass3.getCategoryId());
                } else {
                    _categoryName = categoryWithClass2.getCategoryName() + "/" + categoryWithClass3.getCategoryName();
                }

                categoryWithClass3.setCategoryName(_categoryName);
            }

            // 转map
            Map<Integer, AoyiBaseCategory> categoryMapWithClass3 =
                    categorysWithClass3.stream().collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

            logger.info("导出商品列表 获取三级品类共:{}个", categoryMapWithClass3.size());

            // 3.查询导出数据
            // 3.1 分页查询商品表-批量查询，避免一次返回数据太多，并且还要利用返回的数据批量查询其他表，这样也避免查询时的入参太大
            // 获取记录数
            int totalCount = aoyiProdIndexXMapper.selectSearchCount(sqlParamMap);
            if (totalCount > 50000) { // 如果导出的记录数大于5w则终止导出,excel表的上线是65535
                logger.error("导出商品列表 导出数据过大");
                throw new ExportProuctOverRangeException();
            }
            // 遍历查询
            if (totalCount > 0) {
                PageBean pageBean = new PageBean();
                int pageSize = 500;
                int totalPage = pageBean.getPages(totalCount, pageSize); // 总页数

                for (int currentPage = 1; currentPage <= totalPage; currentPage++) {
                    int offset = pageBean.getOffset(currentPage, pageSize);

                    sqlParamMap.put("offset", offset);
                    sqlParamMap.put("pageSize", 500);

                    // 执行数据查询
                    List<AoyiProdIndexX> aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductListPageable(sqlParamMap);

                    // 转exportVo
                    for (AoyiProdIndexX aoyiProdIndexX : aoyiProdIndexXList) {
                        ProductExportResVo productExportResVo = convertToProductExportResVo(aoyiProdIndexX);

                        // 处理商品供应商名称
                        SysCompany sysCompany = sysCompanyMap.get(aoyiProdIndexX.getMerchantId().longValue());
                        productExportResVo.setMerchantName(sysCompany == null ? "/" : sysCompany.getName());

                        // 处理商品类别
                        AoyiBaseCategory aoyiBaseCategory = null;
                        if (StringUtils.isNotBlank(aoyiProdIndexX.getCategory())) {
                            aoyiBaseCategory = categoryMapWithClass3.get(Integer.valueOf(aoyiProdIndexX.getCategory()));
                        }
                        productExportResVo.setCategory(aoyiBaseCategory == null ? "/" : aoyiBaseCategory.getCategoryName());

                        //
                        productExportResVoList.add(productExportResVo);

                        aoyiProdIndexX = null; // 释放
                    }

                }
            }
        } catch (ExportProuctOverRangeException e) {
            logger.error("导出商品列表 异常了:{}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            logger.error("导出商品列表 异常:{}", e.getMessage(), e);

            throw e;
        }

        logger.info("导出商品列表 导出数据列表个数List<ProductExportResVo> size:{}", productExportResVoList.size());

        return productExportResVoList;
    }

    @Override
    public PageBean exportProductPriceList(float floorPriceRate, int pageNo, int pageSize) throws Exception {
        PageBean pageBean = new PageBean();
        // 0. 返回值
        List<ProductExportResVo> productExportResVoList = new ArrayList<>();

        try {
            // 1.组装数据库查询参数
            HashMap sqlParamMap = new HashMap();
            sqlParamMap.put("floorPriceRate", floorPriceRate);

            logger.info("导出商品价格列表 查询条件为:{}", JSONUtil.toJsonString(sqlParamMap));

            // 2. 查询导出需要的相关数据
            // 2.1 查询所有商户信息
            List<SysCompany> sysCompanyList = vendorsRpcService.queryAllMerchantList();
            // 转map key:id, value:SysCompany
            Map<Long, SysCompany> sysCompanyMap = sysCompanyList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

            // 3.查询导出数据
            // 3.1 分页查询商品表-批量查询，避免一次返回数据太多，并且还要利用返回的数据批量查询其他表，这样也避免查询时的入参太大
            // TODO 获取记录数
            int totalCount = aoyiProdIndexXMapper.selectPriceCount(sqlParamMap);
//            if (totalCount > 50000) { // 如果导出的记录数大于5w则终止导出,excel表的上线是65535
//                logger.error("导出商品价格列表 导出数据过大");
//                throw new ExportProuctOverRangeException();
//            }
            // 遍历查询
            if (totalCount > 0) {
                int totalPage = pageBean.getPages(totalCount, pageSize); // 总页数
                pageBean.setPageNo(pageNo);
                pageBean.setPages(totalPage);
                pageBean.setPageSize(pageSize);
                pageBean.setTotal(totalCount);
                int offset = (pageNo - 1) * pageSize ;
                sqlParamMap.put("offset", offset);
                sqlParamMap.put("pageSize", pageSize);

                // TODO 执行数据查询
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
//                    // TODO 执行数据查询
//                    List<ProductExportResVo> aoyiProdIndexXList = aoyiProdIndexXMapper.selectProductPriceListPageable(sqlParamMap);
//                    aoyiProdIndexXList.forEach(productExportResVo -> {
//                        productExportResVoList.add(productExportResVo);
//                    });
//
//                    // 转exportVo
////                    for (AoyiProdIndexX aoyiProdIndexX : aoyiProdIndexXList) {
////                        ProductExportResVo productExportResVo = convertToProductExportResVo(aoyiProdIndexX);
////
////                        // 处理商品供应商名称
////                        SysCompany sysCompany = sysCompanyMap.get(aoyiProdIndexX.getMerchantId().longValue());
////                        productExportResVo.setMerchantName(sysCompany == null ? "/" : sysCompany.getName());
////
////                        //
////                        productExportResVoList.add(productExportResVo);
////
////                        aoyiProdIndexX = null; // 释放
////                    }
//
//                }
            }
        } catch (ExportProuctOverRangeException e) {
            logger.error("导出商品价格列表 异常了:{}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            logger.error("导出商品价格列表 异常:{}", e.getMessage(), e);

            throw e;
        }

        logger.info("导出商品价格列表 导出数据列表个数List<ProductExportResVo> size:{}", productExportResVoList.size());

        return pageBean;
    }

    // =============================== private ======================================================

    /**
     * 转数据库实体 AoyiProdIndex
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

        // productExportResVo.setMerchantName(aoyiProdIndexX.); // 商品供应商
        productExportResVo.setSku(aoyiProdIndexX.getSkuid()); // 商品SKU
        productExportResVo.setMpu(aoyiProdIndexX.getMpu()); // 商品MPU

        // 商品状态
        Integer state = Integer.valueOf(aoyiProdIndexX.getState()); // -1:初始状态；0：下架；1：上架
        ProductStatusEnum productStatusEnum = ProductStatusEnum.getProductStatusEnum(state);
        productExportResVo.setState(productStatusEnum.getDesc()); // 商品状态

        productExportResVo.setProductName(aoyiProdIndexX.getName()); // 商品名称
        productExportResVo.setBrand(aoyiProdIndexX.getBrand()); // 商品品牌
        // productExportResVo.setCategory(); // 商品类别
        productExportResVo.setModel(aoyiProdIndexX.getModel()); // 商品型号
        productExportResVo.setWeight(aoyiProdIndexX.getWeight()); // 商品重量
        productExportResVo.setUpc(aoyiProdIndexX.getUpc()); // 商品条形码
        productExportResVo.setSellPrice(aoyiProdIndexX.getPrice()); // 销售价格(元)
        productExportResVo.setCostPrice(aoyiProdIndexX.getSprice()); // 进货价格(元)  成本价格
        productExportResVo.setInventory(aoyiProdIndexX.getInventory() == null ? "无" : String.valueOf(aoyiProdIndexX.getInventory())); // 库存

        // 创建时间
        productExportResVo.setCreateTime(aoyiProdIndexX.getCreatedAt() == null ?
                "--" : DateUtil.dateTimeFormat(aoyiProdIndexX.getCreatedAt(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));

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
                        // 获取最新code
                        SkuCode lastCode = skuCodeMapper.selectLast();
                        // logger.info(aoyiProdIndexX.getMerchantId() + "");
                        int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                        // 添加商户信息
                        skuCode = new SkuCode();
                        skuCode.setMerchantId(aoyiProdIndex.getMerchantId());
                        skuCode.setMerchantName(profileBean.getName());
                        skuCode.setMerchantCode(code + "");
                        skuCode.setSkuValue(0);
                        skuCode.setCreatedAt(new Date());
                        skuCode.setUpdatedAt(new Date());
                        skuCodeMapper.insertSelective(skuCode);
                    } else {
                        logger.warn("未获取到商户信息 merchantId:{}信息为:{}", aoyiProdIndex.getMerchantId());
                        throw new ProductException(200001, "vendor 服务失败 " + vendorResponse.getMsg());
                    }
                } else {
                    logger.warn("未获取到商户信息 merchantId:{}信息为:{}", aoyiProdIndex.getMerchantId());
                    throw new ProductException(200001, "商户信息为null");
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

            // 执行插入
            productDao.updateFix(aoyiProdIndexWithBLOBs);

            skuCode.setSkuValue(atomicInteger.get());
            skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);
        });
    }

    @Override
    public OperaResponse updateSkuPriceAndState(StarSku bean) {
        OperaResponse operaResponse = new OperaResponse() ;
        if (bean == null || bean.getId() == null || bean.getId() <= 0) {
            operaResponse.setCode(200200);
            operaResponse.setMsg("ID不能为空");
            return operaResponse ;
        }
        starSkuMapper.updateByPrimaryKeySelective(bean) ;
        operaResponse.setData(bean);
        return operaResponse;
    }

    @Override
    public OperaResponse batchUpdateSkuPriceAndState(List<StarSku> beans) {
        OperaResponse operaResponse = new OperaResponse() ;
        if (beans == null || beans.size() == 0) {
            operaResponse.setCode(200200);
            operaResponse.setMsg("数据不能为空");
            return operaResponse ;
        }
        beans.forEach(bean -> {
            starSkuMapper.updateByPrimaryKeySelective(bean) ;
            operaResponse.setData(bean);
        });
        operaResponse.setData(beans);
        return operaResponse;
    }

    @Override
    public OperaResponse updateSpuState(List<AoyiProdIndex> beans) {
        OperaResponse operaResponse = new OperaResponse() ;
        List<Integer> ids = new ArrayList<>() ;
        for (int i = 0; i < beans.size(); i++) {
            AoyiProdIndex bean = beans.get(i) ;
            if (bean == null || bean.getId() == null || bean.getId() <= 0) {
                continue;
            }
            if (bean.getState() == null) {
                ids.add(bean.getId()) ;
                continue;
            }
            AoyiProdIndexWithBLOBs aoyiProdIndex = mapper.selectByPrimaryKey(bean.getId()) ;
            List<StarDetailImg> starDetailImgs = starDetailImgDao.selectBySpuId(aoyiProdIndex.getId()) ;
            if ("1".equals(bean.getState())) {
                if (StringUtils.isEmpty(aoyiProdIndex.getCategory())) {
                    ids.add(bean.getId()) ;
                    logger.info("category is {}", bean.getId());
                    continue;
                }
                if (StringUtils.isEmpty(aoyiProdIndex.getPrice())) {
                    logger.info("price is {}", bean.getId());
                    ids.add(bean.getId()) ;
                    continue;
                }
                if (StringUtils.isEmpty(aoyiProdIndex.getSprice()) && aoyiProdIndex.getMerchantId() != 4) {
                    logger.info("sprice is {}", bean.getId());
                    ids.add(bean.getId()) ;
                    continue;
                }
                if (StringUtils.isEmpty(aoyiProdIndex.getImagesUrl())) {
                    if (starDetailImgs == null || starDetailImgs.size() == 0) {
                        logger.info("image url is {}", bean.getId());
                        ids.add(bean.getId()) ;
                        continue;
                    }
                }
                if (StringUtils.isEmpty(aoyiProdIndex.getImage())) {
                    if ((starDetailImgs == null || starDetailImgs.size() == 0) && StringUtils.isEmpty(aoyiProdIndex.getImagesUrl())) {
                        logger.info("image is {}", bean.getId());
                        ids.add(bean.getId());
                        continue;
                    }
                }
                if (StringUtils.isEmpty(aoyiProdIndex.getIntroductionUrl()) && StringUtils.isEmpty(aoyiProdIndex.getIntroduction())) {
                    logger.info("introduction  url is {}", bean.getId());
                    ids.add(bean.getId()) ;
                    continue;
                }
            }
            productDao.updateStateById(bean) ;
            // 查询是否有sku
            List<StarSku> starSkus = starSkuDao.selectBySpuId(aoyiProdIndex.getSkuid()) ;
            if (starSkus != null && starSkus.size() > 0) {
                starSkus.forEach(starSku -> {
                    if (starSku.getPrice() != 0) {
                        starSku.setStatus(Integer.valueOf(bean.getState()));
                        starSku.setUpdateTime(new Date());
                        starSkuMapper.updateByPrimaryKeySelective(starSku) ;
                    }
                });
            }
        }
        if (ids != null && ids.size() > 0) {
            operaResponse.setCode(200104);
            operaResponse.setMsg("存在有问题的id列表");
            operaResponse.setData(ids);
            return operaResponse ;
        }
        return operaResponse;
    }

    @Override
    public OperaResponse updateBatch(List<AoyiProdIndex> beans) {
        OperaResponse response = new OperaResponse() ;
        beans.forEach(bean -> {
            if (bean.getId() != null) {
                AoyiProdIndexWithBLOBs temp = mapper.selectByPrimaryKey(bean.getId()) ;
                List<StarSku> starSkus = starSkuDao.selectBySpuId(temp.getSkuid()) ;
                if (starSkus != null && starSkus.size() > 0) {
                    StarSku starSku = starSkus.get(0) ;
                    if (StringUtils.isNotBlank(bean.getPrice())) {
                        BigDecimal bigDecimalPrice = new BigDecimal(bean.getPrice()) ;
                        int price = bigDecimalPrice.multiply(new BigDecimal("100")).intValue() ;
                        starSku.setPrice(price);
                    }
                    if (StringUtils.isNotBlank(bean.getState())) {
                        starSku.setStatus(Integer.valueOf(bean.getState()));
                    }
                    starSkuMapper.updateByPrimaryKeySelective(starSku) ;
                }
                AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs() ;

                BeanUtils.copyProperties(bean, aoyiProdIndexWithBLOBs);
                mapper.updateByPrimaryKeySelective(aoyiProdIndexWithBLOBs) ;
            }
        });
        response.setData(beans);
        return response;
    }
}