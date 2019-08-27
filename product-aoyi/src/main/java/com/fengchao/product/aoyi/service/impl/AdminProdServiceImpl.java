package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.ProductExtendDao;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AdminProdServiceImpl implements AdminProdService {

    private static Logger logger = LoggerFactory.getLogger(AdminProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper prodXMapper;
    @Autowired
    private ProdExtendXMapper prodExtendMapper;
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

    @Autowired
    private ProductExtendDao productExtendDao;

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
        total = prodXMapper.selectSearchCount(map);
        if (total > 0) {
            prodXMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
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
        total = prodXMapper.selectSearchCount(map);
        if (total > 0) {
            prodXMapper.selectSearchLimit(map).forEach(aoyiProdIndex -> {
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
        // 1.组装数据库查询参数
        HashMap sqlParamMap = new HashMap();
        sqlParamMap.put("name", bean.getQuery());
        sqlParamMap.put("skuid",bean.getSkuid());
        sqlParamMap.put("state",bean.getState());
        if (bean.getMerchantHeader() > 0) {
            sqlParamMap.put("merchantId", bean.getMerchantId());
        }
        int offset = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        sqlParamMap.put("offset", offset);
        sqlParamMap.put("pageSize", bean.getLimit());

        // 2.查询数据
        // 2.1 查询条数
        int totalCount = prodXMapper.selectSearchCount(sqlParamMap);

        // 2.2 查询商品表
        List<AoyiProdIndexX> aoyiProdIndexXList = new ArrayList<>(); // 结果数据
        if (totalCount > 0) { // 如果存在数据
            // 查询
            aoyiProdIndexXList = prodXMapper.selectProductListPageable(sqlParamMap);

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
        List<AoyiProdIndexX> aoyiProdIndices = prodXMapper.selectProdAll();
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
    public int add(AoyiProdIndexX requestProdParams) throws ProductException {
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
            // 查询商户信息
            VendorsProfileBean profileBean = findVendorInfo(requestProdParams.getMerchantId());
            if (profileBean != null) {
                // 获取最新code
                SkuCode lastCode = skuCodeMapper.selectLast();
                // logger.info(aoyiProdIndexX.getMerchantId() + "");
                int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
                // 添加商户信息
                skuCode = new SkuCode();
                skuCode.setMerchantId(requestProdParams.getMerchantId());
                skuCode.setMerchantName(profileBean.getCompany().getName());
                skuCode.setMerchantCode(code + "");
                skuCode.setSkuValue(0);
                skuCode.setCreatedAt(new Date());
                skuCode.setUpdatedAt(new Date());

                skuCodeMapper.insertSelective(skuCode);
            } else {
                logger.warn("未获取到商户信息 merchantId:{}信息为:{}", requestProdParams.getMerchantId());
                throw new ProductException(200001, "商户信息为null");
            }
        }

        // 3. 创建商品
        // 转数据库实体
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(requestProdParams);

        // 设置mpu
        String merchantCode = skuCode.getMerchantCode();
        int skuValue = skuCode.getSkuValue();
        AtomicInteger atomicInteger = new AtomicInteger(skuValue); // FIXME : 这个原子操作好像没用呀!
        String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet());
        aoyiProdIndexWithBLOBs.setMpu(sku);

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
        BigDecimal bprice = new BigDecimal(requestProdParams.getPrice());
        int iprice = bprice.multiply(new BigDecimal(100)).intValue(); // 价格，单位：分
        aoyiProdIndexWithBLOBs.setIprice(iprice);

        // 执行插入
        int result = productDao.insert(aoyiProdIndexWithBLOBs);

        skuCode.setSkuValue(atomicInteger.get());
        skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);

        return result;
    }

    @Override
    public int update(AoyiProdIndexX bean) throws ProductException {
        if (bean.getId() > 0) {
            bean.setUpdatedAt(new Date());
            prodXMapper.updateByPrimaryKeySelective(bean);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
        return bean.getId();
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            prodXMapper.deleteByPrimaryKey(id);
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
        total = prodXMapper.selectSkuByCouponIdCount(bean);
        if (total > 0) {
            prodXMapper.selectSkuByCouponIdLimit(bean).forEach(aoyiProdIndex -> {
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

    // =============================== private ======================================================

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
}
