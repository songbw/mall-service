package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.feign.ProdService;
import com.fengchao.equity.mapper.*;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponTags;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.service.CouponService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper mapper;
    @Autowired
    private CouponUseInfoMapper useInfoMapper;
    @Autowired
    private CouponTagsMapper tagsMapper;
    @Autowired
    private ProdService productService;

    @Override
    public int createCoupon(CouponBean bean) {
        Coupon coupon = beanToCoupon(bean);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        if(coupon.getCode() == null || "".equals(coupon.getCode())){
            coupon.setCode(uuid);
        }
        mapper.insertSelective(coupon);
        return coupon.getId();
    }

    @Override
    public PageBean findCoupon(Integer offset, Integer limit) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<CouponResultBean> coupons = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            coupons = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, coupons, total, offset, limit);
        return pageBean;
    }

    @Override
    public int updateCoupon(CouponBean bean) {
        Coupon coupon = beanToCoupon(bean);
        coupon.setId(bean.getId());
        return mapper.updateByPrimaryKeySelective(coupon);
    }

    @Override
    public int deleteCoupon(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public CouponBean findByCouponId(Integer id) {
        Coupon coupon = mapper.selectByPrimaryKey(id);
        CouponBean couponBean = couponToBean(coupon);
        return couponBean;
    }

    @Override
    public PageBean activeCoupon(CouponUseInfoBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("userOpenId", bean.getUserOpenId());
        map.put("categoryId", bean.getCategoryId());
        map.put("categoryName", bean.getCategoryName());
        map.put("tagId", bean.getTagId());
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        if(bean.getTagName() != null && !"".equals(bean.getTagName())){
            CouponTags tags = tagsMapper.selectByName(bean.getTagName());
            map.put("tagId", tags.getId());
        }
        List<CouponBean> couponBeans = new ArrayList<>();
        total = mapper.selectActiveCouponCount(map);
        if (total > 0) {
            List<Coupon> coupons = mapper.selectActiveCouponLimit(map);
            coupons.forEach(coupon -> {
                map.put("couponId",coupon.getId());
                int num = useInfoMapper.selectCollectCount(map);
                coupon.setUserCollectNum(num);
                couponBeans.add(couponToBean(coupon));
            });
        }
        pageBean = PageBean.build(pageBean, couponBeans, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public CategoryCouponBean activeCategories() {
        CategoryCouponBean categoryCouponBean = new CategoryCouponBean();
        List<String> tags = mapper.selectTags();
        List<CouponTags> tagList = tagsMapper.selectTags(tags);
        List<String> categories = mapper.selectActiveCategories();

        OperaResult result = productService.findCategoryList(categories);
        Object object = result.getData().get("result");
        String objectString = JSON.toJSONString(object);
        List<Category> categoryList = JSONObject.parseArray(objectString, Category.class);

        categoryCouponBean.setTags(tagList);
        categoryCouponBean.setCategorys(categoryList);
        return categoryCouponBean;
    }

    @Override
    public CouponBean selectSkuByCouponId(CouponUseInfoBean bean) {
        QueryProdBean queryProdBean = new QueryProdBean();
        Coupon coupon = mapper.selectByPrimaryKey(bean.getId());
        if(coupon == null){
            return null;
        }
        CouponBean couponBean = couponToBean(coupon);
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        queryProdBean.setOffset(bean.getOffset());
        queryProdBean.setPageNo(pageNo);
        queryProdBean.setPageSize(bean.getLimit());
        if(coupon.getScenarioType() == 1){
            queryProdBean.setCouponSkus(Arrays.asList(coupon.getCouponSkus().split(",")));
        }else if(coupon.getScenarioType() == 2){
            queryProdBean.setExcludeSkus(coupon.getExcludeSkus());
        }else if(coupon.getScenarioType() == 3){
            queryProdBean.setExcludeSkus(coupon.getExcludeSkus());
            queryProdBean.setCategories(coupon.getCategories());
//            map.put("brands",coupon.getBrands());
        }
        OperaResult operaResult = productService.findProdList(queryProdBean);
        Object object = operaResult.getData().get("result");
        String objectString = JSON.toJSONString(object);
        PageBean pageBean = JSONObject.parseObject(objectString, PageBean.class);
        couponBean.setCouponSkus(pageBean);
        return couponBean;
    }

    @Override
    public Coupon consumeCoupon(CouponUseInfoBean bean) {
        CouponUseInfo couponUseInfo = useInfoMapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            return null;
        }
        useInfoMapper.updateStatusByUserCode(bean.getUserCouponCode());
        Coupon coupon = mapper.selectByPrimaryKey(couponUseInfo.getCouponId());
        return coupon;
    }

    @Override
    public List<CouponBean> selectCouponBySku(AoyiProdBean bean) {
        List<CouponBean> couponBeans =  new ArrayList<>();
        mapper.selectCouponBySku(bean).forEach(coupon -> {
            couponBeans.add(couponToBean(coupon));
        });
        return couponBeans;
    }

    @Override
    public PageBean serachCoupon(CouponSearchBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("name",bean.getName());
        map.put("status",bean.getStatus());
        map.put("releaseTotal",bean.getReleaseTotal());
        map.put("releaseStartDate",bean.getReleaseStartDate());
        map.put("releaseEndDate",bean.getReleaseEndDate());
        map.put("couponType", bean.getCouponType());
        map.put("scenarioType",bean.getScenarioType());
        List<CouponResultBean> coupons = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            coupons = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, coupons, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    private Coupon beanToCoupon(CouponBean bean){

        Coupon coupon = new Coupon();

        coupon.setName(bean.getName());
        coupon.setSupplierMerchantId(bean.getSupplierMerchantId());
        coupon.setSupplierMerchantName(bean.getSupplierMerchantName());
        coupon.setReleaseTotal(bean.getReleaseTotal());
        coupon.setReleaseNum(bean.getReleaseNum());
        coupon.setReleaseStartDate(bean.getReleaseStartDate());
        coupon.setReleaseEndDate(bean.getReleaseEndDate());
        coupon.setStatus(bean.getStatus());
        coupon.setEffectiveStartDate(bean.getEffectiveStartDate());
        coupon.setEffectiveEndDate(bean.getEffectiveEndDate());
        coupon.setDescription(bean.getDescription());
        if(bean.getExcludeDates()!= null && !"".equals(bean.getExcludeDates())){
            List<Object> excludeDates = bean.getExcludeDates();
            JSONArray json = new JSONArray();
            for(Object object : excludeDates){
                json.add(object);
            }
            coupon.setExcludeDates(json.toJSONString());
        }
        coupon.setUrl(bean.getUrl());
        coupon.setCreateDate(new Date());
        coupon.setCategory(bean.getCategory());
        if(bean.getTags() != null){
            String tagStr = Arrays.toString(bean.getTags());
            coupon.setTags(tagStr.substring(1, tagStr.length()-1));
        }
        coupon.setImageUrl(bean.getImageUrl());
        if(bean.getRules() != null){
            if(bean.getRules().getCode() != null && !"".equals(bean.getRules().getCode())){
                coupon.setCode(bean.getRules().getCode());
            }
            coupon.setRulesDescription(bean.getRules().getRulesDescription());
            coupon.setPerLimited(bean.getRules().getPerLimited());
            coupon.setScopes(StringUtils.join(bean.getRules().getScopes(),","));
            if(bean.getRules().getScenario() != null){
                coupon.setScenarioType(bean.getRules().getScenario().getType());
                coupon.setCouponSkus(StringUtils.join(bean.getRules().getScenario().getCouponSkus(),","));
                coupon.setExcludeSkus(StringUtils.join(bean.getRules().getScenario().getExcludeSkus(),","));
                coupon.setCategories(StringUtils.join(bean.getRules().getScenario().getCategories(),","));
                coupon.setBrands(StringUtils.join(bean.getRules().getScenario().getBrands(),","));
            }
            if(bean.getRules().getCollect() != null){
                coupon.setCollectType(bean.getRules().getCollect().getType());
                coupon.setPoints(bean.getRules().getCollect().getPoints());
            }
            if(bean.getRules().getCustomer() != null){
                coupon.setCustomerType(bean.getRules().getCustomer().getType());
                coupon.setUsers( StringUtils.join(bean.getRules().getCustomer().getUsers(),","));
            }
            if(bean.getRules().getCouponRules() != null){
                coupon.setCouponType(bean.getRules().getCouponRules().getInteger("type"));
                coupon.setCouponRules(bean.getRules().getCouponRules().toJSONString());
            }
        }
        return coupon;
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
}
