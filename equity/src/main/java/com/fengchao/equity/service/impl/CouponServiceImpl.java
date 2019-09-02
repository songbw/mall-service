package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CouponDao;
import com.fengchao.equity.feign.ProdService;
import com.fengchao.equity.feign.SSOService;
import com.fengchao.equity.mapper.*;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.JSONUtil;
import com.fengchao.equity.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponXMapper mapper;
    @Autowired
    private CouponUseInfoXMapper useInfoMapper;
    @Autowired
    private CouponTagsMapper tagsMapper;
    @Autowired
    private ProdService productService;
    @Autowired
    private JobClient jobClient;

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private SSOService ssoService;

    @Override
    public int createCoupon(CouponBean bean) {
        CouponX couponx = beanToCoupon(bean);
        couponx.setCreateDate(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        if(couponx.getCode() == null || "".equals(couponx.getCode())){
            couponx.setCode(uuid);
        }
        int num = mapper.insertSelective(couponx);
        return couponx.getId();
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
        CouponX coupon = beanToCoupon(bean);
        coupon.setId(bean.getId());
        CouponX couponById;
        if(bean.getStatus() != null && bean.getStatus() == 2){
            couponById = mapper.selectByPrimaryKey(bean.getId());
            Date now = new Date();
            if(couponById.getReleaseStartDate().after(now)){

                coupon.setStatus(3);
                JobClientUtils.couponEffectiveTrigger(jobClient, coupon.getId(), couponById.getReleaseStartDate());
                JobClientUtils.couponEndTrigger(jobClient, coupon.getId(), couponById.getReleaseEndDate());
                JobClientUtils.couponInvalidTrigger(jobClient, coupon.getId(), couponById.getEffectiveEndDate());
            }else if(couponById.getReleaseStartDate().before(now)  && couponById.getReleaseEndDate().after(now)){

                coupon.setStatus(4);
                JobClientUtils.couponEndTrigger(jobClient, coupon.getId(), couponById.getReleaseEndDate());
                JobClientUtils.couponInvalidTrigger(jobClient, coupon.getId(), couponById.getEffectiveEndDate());
            }else if(couponById.getReleaseEndDate().before(now)){

                coupon.setStatus(5);
                JobClientUtils.couponInvalidTrigger(jobClient, coupon.getId(), couponById.getEffectiveEndDate());
            }
        }

        return mapper.updateByPrimaryKeySelective(coupon);
    }

    @Override
    public int deleteCoupon(Integer id) {
        CouponX couponX = mapper.selectByPrimaryKey(id);
        if(couponX.getStatus() != 1){
            return 2;
        }
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public CouponBean findByCouponId(Integer id) {
        CouponX coupon = mapper.selectByPrimaryKey(id);
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
        map.put("collect_type", 1);
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        if(bean.getTagName() != null && !"".equals(bean.getTagName())){
            CouponTags tags = tagsMapper.selectByName(bean.getTagName());
            map.put("tagId", tags.getId());
        }
        List<CouponBean> couponBeans = new ArrayList<>();
        total = mapper.selectActiveCouponCount(map);
        if (total > 0) {
            List<CouponX> coupons = mapper.selectActiveCouponLimit(map);
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
        List<CouponTags> tagList = null;
        if(!tags.isEmpty()){
            tagList = tagsMapper.selectTags(tags);
        }
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
        CouponX coupon = mapper.selectByPrimaryKey(bean.getId());
        if(coupon == null){
            return null;
        }
        CouponBean couponBean = couponToBean(coupon);
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        queryProdBean.setOffset(bean.getOffset());
        queryProdBean.setPageNo(pageNo);
        queryProdBean.setPageSize(bean.getLimit());
        if(coupon.getScenarioType() == 1){
            queryProdBean.setCouponMpus(Arrays.asList(coupon.getCouponMpus().split(",")));
        }else if(coupon.getScenarioType() == 2){
            queryProdBean.setExcludeMpus(coupon.getExcludeMpus());
        }else if(coupon.getScenarioType() == 3){
            queryProdBean.setExcludeMpus(coupon.getExcludeMpus());
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
    public CouponUseInfoX consumeCoupon(CouponUseInfoBean bean) {
        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = useInfoMapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            return null;
        }else if (couponUseInfo.getStatus() == 3){
            return couponUseInfo;
        }
        useInfo.setId(bean.getId());
        useInfo.setConsumedTime(new Date());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setStatus(3);
        useInfoMapper.updateStatusByUserCode(useInfo);
        return couponUseInfo;
    }

    @Override
    public List<CouponBean> selectCouponByMpu(AoyiProdBean bean) {
        List<CouponBean> couponBeans =  new ArrayList<>();
        mapper.selectCouponByMpu(bean).forEach(coupon -> {
            couponBeans.add(couponToBean(coupon));
        });
        return couponBeans;
    }

    @Override
    public int effective(int couponId) {
        return mapper.couponEffective(couponId);
    }

    @Override
    public int end(int couponId) {
        int num = mapper.couponEnd(couponId);
        return num;
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

    private CouponX beanToCoupon(CouponBean bean){

        CouponX coupon = new CouponX();

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
                coupon.setCouponMpus(StringUtils.join(bean.getRules().getScenario().getCouponMpus(),","));
                coupon.setExcludeMpus(StringUtils.join(bean.getRules().getScenario().getExcludeMpus(),","));
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

    private CouponBean couponToBean(CouponX coupon){

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
            if(coupon.getCouponMpus() != null){
                couponBean.getRules().getScenario().setCouponMpus(coupon.getCouponMpus().split(","));
            }
            if(coupon.getExcludeMpus() != null){
                couponBean.getRules().getScenario().setExcludeMpus(coupon.getExcludeMpus().split(","));
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


    @Override
    public List<CouponBean> queryCouponBeanListIdList(List<Integer> idList) throws Exception {
        log.info("根据id集合查询coupon列表 查询参数:{}", JSONUtil.toJsonString(idList));
        List<Coupon> couponList = couponDao.selectCouponListByIdList(idList);
        log.info("根据id集合查询coupon列表 数据库返回:{}", JSONUtil.toJsonString(couponList));

        // 转dto
        List<CouponBean> couponBeanList = new ArrayList<>();
        for (Coupon coupon : couponList) {
            CouponBean couponBean = convertToCouponBean(coupon);

            couponBeanList.add(couponBean);
        }

        log.info("根据id集合查询coupon列表 返回List<CouponBean>:{}", JSONUtil.toJsonString(couponBeanList));

        return couponBeanList;
    }

    @Override
    public int invalid(int couponId) {
        return useInfoMapper.updateStatusByCouponId(couponId);
    }

    @Override
    public CouponUseInfoX adminConsumeCoupon(CouponUseInfoBean bean) {

        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = useInfoMapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            return null;
        }else if (couponUseInfo.getStatus() == 3){
            return couponUseInfo;
        }

        if(couponUseInfo != null){
            CouponX couponX = mapper.selectByPrimaryKey(couponUseInfo.getCouponId());
            Date date = new Date();
            if(couponX.getEffectiveStartDate().after(date) || couponX.getEffectiveEndDate().before(date)){
                couponUseInfo.setStatus(4);
                return couponUseInfo;
            }
        }

        useInfo.setId(bean.getId());
        useInfo.setConsumedTime(new Date());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setStatus(3);
        useInfoMapper.updateStatusByUserCode(useInfo);
        return couponUseInfo;
    }

    @Override
    public List<Object> giftCoupon(String openId, String iAppId) {
        List<Object> couponBeans = new ArrayList();

        OperaResult userResult = ssoService.findUser(openId.substring(2, openId.length()), iAppId);
        if (userResult.getCode() == 200) {
            Map<String, Object> data = userResult.getData();
            Object object = data.get("user");
            String jsonString = JSONUtil.toJsonString(object);
            User user = JSONObject.parseObject(jsonString, User.class) ;
            if(user == null){
                couponBeans.add(2);
                return couponBeans;
            }
        }

        HashMap map = new HashMap();
        map.put("userOpenId", openId);
        List<CouponX> coupons = mapper.selectGiftCoupon();
        coupons.forEach(couponX -> {
            map.put("couponId",couponX.getId());
            int num = useInfoMapper.selectCollectCount(map);
            couponX.setUserCollectNum(num);
            couponBeans.add(couponToBean(couponX));
        });
        return couponBeans;
    }

    @Override
    public PageableData<Coupon> findReleaseCoupon(Integer pageNo, Integer pageSize) {
        PageableData<Coupon> pageableData = new PageableData<>();
        PageInfo<Coupon> releaseCoupon = couponDao.findReleaseCoupon(pageNo, pageSize);
        PageVo pageVo = ConvertUtil.convertToPageVo(releaseCoupon);
        List<Coupon> groupInfoList = releaseCoupon.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public List<CouponBean> findCouponListByIdList(List<Integer> ids) {
        List<CouponX> couponList = mapper.selectCouponListByIdList(ids);
        List<CouponBean> couponBeanList = new ArrayList<>();
        for (CouponX coupon : couponList) {
            CouponBean couponBean = couponToBean(coupon);
            couponBeanList.add(couponBean);
        }
        return couponBeanList;
    }

    //============================== private =========================

    /**
     *
     * @param coupon
     * @return
     */
    private CouponBean convertToCouponBean(Coupon coupon) {
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
        // couponBean.setExcludeDates(coupon.getExcludeDates());
        couponBean.setUrl(coupon.getUrl());
        couponBean.setCategory(coupon.getCategory());
        // couponBean.setTags(coupon.getTags());
        couponBean.setImageUrl(coupon.getImageUrl());
        couponBean.setCreateDate(coupon.getCreateDate());
        // couponBean.setUserCollectNum();
        // couponBean.setRules(coupon.getCouponRules());
        // couponBean.setCouponUseInfo();

        return couponBean;
    }
}
