package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.dao.CouponDao;
import com.fengchao.equity.dao.PromotionScheduleDao;
import com.fengchao.equity.feign.ProdService;
import com.fengchao.equity.feign.SSOService;
import com.fengchao.equity.mapper.*;
import com.fengchao.equity.model.*;
import com.fengchao.equity.rpc.ProductRpcService;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponXMapper XMapper;
    @Autowired
    private CouponMapper mapper;
    @Autowired
    private CouponUseInfoXMapper useInfoXMapper;
    @Autowired
    private CouponUseInfoMapper couponUseInfoMapper;
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
    @Autowired
    private PromotionXMapper promotionXMapper;
    @Autowired
    private PromotionScheduleDao scheduleDao;
    @Autowired
    private CardTicketDao ticketDao;
    @Autowired
    private ProductRpcService prodService;
    @Autowired
    private Environment environment;

    @Override
    public int createCoupon(CouponBean bean) {
        CouponX couponx = beanToCoupon(bean);
        couponx.setCreateDate(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        if(couponx.getCode() == null || "".equals(couponx.getCode())){
            couponx.setCode(bean.getAppId() + uuid);
        }
        int num = XMapper.insertSelective(couponx);
        return couponx.getId();
    }

    @Override
    public PageBean findCoupon(Integer offset, Integer limit, String appId) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        map.put("appId",appId);
        List<CouponResultBean> coupons = new ArrayList<>();
        total = XMapper.selectCount(map);
        if (total > 0) {
            coupons = XMapper.selectLimit(map);
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
            couponById = XMapper.selectByPrimaryKey(bean.getId());
            Date now = new Date();
            if(couponById.getReleaseStartDate().after(now)){

                coupon.setStatus(CouponStatusEnum.READY_GO.getCode());
                JobClientUtils.couponEffectiveTrigger(environment, jobClient, coupon.getId(), couponById.getReleaseStartDate());
                JobClientUtils.couponEndTrigger(environment, jobClient, coupon.getId(), couponById.getReleaseEndDate());
                if (null != coupon.getCouponType() &&
                        !CouponTypeEnum.GIFT_PACKAGE.equals(coupon.getCouponType())) {
                    JobClientUtils.couponInvalidTrigger(environment, jobClient, coupon.getId(), couponById.getEffectiveEndDate());
                }
            }else if(couponById.getReleaseStartDate().before(now)  && couponById.getReleaseEndDate().after(now)){

                coupon.setStatus(CouponStatusEnum.UNDERGOING.getCode());
                JobClientUtils.couponEndTrigger(environment, jobClient, coupon.getId(), couponById.getReleaseEndDate());
                if (null != coupon.getCouponType() &&
                        !CouponTypeEnum.GIFT_PACKAGE.equals(coupon.getCouponType())) {
                    JobClientUtils.couponInvalidTrigger(environment, jobClient, coupon.getId(), couponById.getEffectiveEndDate());
                }
            }else if(couponById.getReleaseEndDate().before(now)){

                coupon.setStatus(CouponStatusEnum.INVALID.getCode());
                if (null != coupon.getCouponType() &&
                        !CouponTypeEnum.GIFT_PACKAGE.equals(coupon.getCouponType())) {
                    JobClientUtils.couponInvalidTrigger(environment, jobClient, coupon.getId(), couponById.getEffectiveEndDate());
                }
            }
        }
        return XMapper.updateByPrimaryKeySelective(coupon);
    }

    @Override
    public int deleteCoupon(Integer id) {
        CouponX couponX = XMapper.selectByPrimaryKey(id);
        if(couponX.getStatus() != 1){
            return 2;
        }
        return XMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CouponBean findByCouponId(Integer id) {
        CouponX coupon = XMapper.selectByPrimaryKey(id);
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
        map.put("appId", bean.getAppId());
        if(bean.getTagName() != null && !"".equals(bean.getTagName())){
            CouponTags tags = tagsMapper.selectByName(bean.getTagName());
            map.put("tagId", tags.getId());
        }
        List<CouponBean> couponBeans = new ArrayList<>();
        total = XMapper.selectActiveCouponCount(map);
        if (total > 0) {
            List<CouponX> coupons = XMapper.selectActiveCouponLimit(map);
            coupons.forEach(coupon -> {
                int num = useInfoXMapper.selectCollectCount(coupon.getId(), bean.getUserOpenId(), bean.getAppId());
                coupon.setUserCollectNum(num);
                couponBeans.add(couponToBean(coupon));
            });
        }
        pageBean = PageBean.build(pageBean, couponBeans, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public CategoryCouponBean activeCategories(String appId) {
        CategoryCouponBean categoryCouponBean = new CategoryCouponBean();
        List<String> tags = XMapper.selectTags(appId);
        List<CouponTags> tagList = null;
        if(!tags.isEmpty()){
            tagList = tagsMapper.selectTags(tags);
        }

        List<Category> categoryList = null;
        List<String> categories = XMapper.selectActiveCategories(appId);
        if(!categories.isEmpty()){
            OperaResult result = productService.findCategoryList(categories);
            Object object = result.getData().get("result");
            String objectString = JSON.toJSONString(object);
            categoryList = JSONObject.parseArray(objectString, Category.class);
        }

        categoryCouponBean.setTags(tagList);
        categoryCouponBean.setCategorys(categoryList);
        return categoryCouponBean;
    }

    @Override
    public CouponBean selectSkuByCouponId(CouponUseInfoBean bean) {
        QueryProdBean queryProdBean = new QueryProdBean();
        CouponX coupon = XMapper.selectByPrimaryKey(bean.getId());
        if(coupon == null){
            return null;
        }
        CouponBean couponBean = couponToBean(coupon);
        HashMap<String, String> map = new HashMap<>();
        JSONArray couponSkus = couponBean.getRules().getScenario().getCouponSkus();
        if(couponSkus != null){
            for (int i = 0; i < couponSkus.size(); i++){
                JSONObject object = couponSkus.getJSONObject(i);
                map.put(object.getString("mpu"), object.getString("skuId"));
            }
        }
        List<AoyiProdIndex> productList = new ArrayList<>();
        if(coupon.getScenarioType() == 1){
            productList = prodService.findProductListByMpuIdList(Arrays.asList(coupon.getCouponMpus().split(",")));
            if(!map.isEmpty()){
                for(AoyiProdIndex product: productList){
                    String skuId = map.get(product.getMpu());
                    List<StarSkuBean> skuList = product.getSkuList();
                    if(!skuList.isEmpty() && skuList.size() > 1){
                        for(StarSkuBean skuBean: skuList){
                            if(skuBean.getCode().equals(skuId)){
                                List<StarSkuBean> skus = new ArrayList<>();
                                skus.add(skuBean);
                                product.setSkuList(skus);
                            }
                        }
                    }
                }
            }
        }else if(coupon.getScenarioType() == 3){
            List<String> categories = Arrays.asList(coupon.getCategories().split(","));
            for (String categoryID : categories) {
                queryProdBean.setCategoryID(categoryID);
                productList = prodService.searchProd(queryProdBean);
            }
        }

        PageBean pageBean = new PageBean();
        pageBean.setPages(1);
        pageBean.setPageNo(1);
        pageBean.setPageSize(bean.getLimit());
        pageBean.setTotal(productList.size());
        pageBean.setList(productList);
//        OperaResult operaResult = productService.findProdList(queryProdBean, bean.getAppId());
//        Object object = operaResult.getData().get("result");
//        String objectString = JSON.toJSONString(object);
//        PageBean pageBean = JSONObject.parseObject(objectString, PageBean.class);
        couponBean.setCouponSkus(pageBean);
        return couponBean;
    }

    @Override
    public CouponUseInfoX consumeCoupon(CouponUseInfoBean bean) {
        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = useInfoXMapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            return null;
        }else if (couponUseInfo.getStatus() == CouponUseStatusEnum.USED.getCode()){
            //非首次核销单独处理
            log.info("优惠券已使用参数:{}", JSONUtil.toJsonString(couponUseInfo));
            if (null != bean.getOrderId()) {
                useInfo.setId(bean.getId());
                String orderId = couponUseInfo.getOrderId();
                if (orderId != null && !orderId.isEmpty()) {
                    //多个主订单号以逗号分割的字符串
                    useInfo.setOrderId(orderId + "," + String.valueOf(bean.getOrderId()));
                } else {
                    useInfo.setOrderId(String.valueOf(bean.getOrderId()));
                }
                useInfoXMapper.updateStatusByUserCode(useInfo);
            }
            return couponUseInfo;
        }

        log.info("首次核销 {}",JSON.toJSONString(couponUseInfo));
        Coupon coupon = couponDao.selectCouponById(couponUseInfo.getCouponId());
        if(coupon.getCouponType() != null && coupon.getCouponType() == 4){
            ticketDao.consumeCard(bean.getUserCouponCode());
        }
        useInfo.setId(bean.getId());
        useInfo.setConsumedTime(new Date());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        if (bean.getOrderId() != null) {
            if (couponUseInfo.getOrderId().isEmpty()) {
                useInfo.setOrderId(String.valueOf(bean.getOrderId()));
            }else{
                useInfo.setOrderId(couponUseInfo.getOrderId() + "," + String.valueOf(bean.getOrderId()));
            }
        }
        useInfo.setStatus(CouponUseStatusEnum.USED.getCode());
        useInfoXMapper.updateStatusByUserCode(useInfo);
        return couponUseInfo;
    }

    @Override
    public List<CouponBean> selectCouponByMpu(AoyiProdBean bean) {
        List<CouponBean> couponBeans =  new ArrayList<>();
        XMapper.selectCouponByMpu(bean).forEach(coupon -> {
            couponBeans.add(couponToBean(coupon));
        });
        return couponBeans;
    }

    @Override
    public int effective(int couponId) {
        return XMapper.couponEffective(couponId);
    }

    @Override
    public int end(int couponId) {
        int num = XMapper.couponEnd(couponId);
        return num;
    }

    private
    List<Integer> getIdList(String openId,String userCouponCode){

        CouponUseInfoExample infoExample = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria infoCriteria = infoExample.createCriteria();

        if (null != openId && !openId.isEmpty()){
            infoCriteria.andUserOpenIdEqualTo(openId);
        }
        if (null != userCouponCode && !userCouponCode.isEmpty()){
            infoCriteria.andUserCouponCodeEqualTo(userCouponCode);
        }
        List<CouponUseInfo> list = couponUseInfoMapper.selectByExample(infoExample);
        List<Integer> idList = new ArrayList<>();
        for(CouponUseInfo info: list){
            if(null != info.getCouponId()){
                idList.add(info.getCouponId());
            }
        }
        return idList;

    }

    @Override
    public PageBean searchCoupon(CouponSearchBean bean) {
        PageBean pageBean = new PageBean();
        int pageNo = bean.getOffset();
        Integer pageSize = bean.getLimit();
        if (1 > pageNo){
            pageNo = 1;
        }
        if (1 > pageSize){
            pageSize = 10;
        }
        String name = bean.getName();
        Integer status = bean.getStatus();
        Integer releaseTotal = bean.getReleaseTotal();
        String couponType = bean.getCouponType();
        String scenarioType = bean.getScenarioType();
        String appId = bean.getAppId();
        Date releaseStartDate = bean.getReleaseStartDate();
        Date releaseEndDate = bean.getReleaseEndDate();
        String openId = bean.getOpenId();
        String userCouponCode = bean.getUserCouponCode();

        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();

        boolean needCouponUserInfo = (null != openId && !openId.isEmpty()) ||
                (null != userCouponCode && !userCouponCode.isEmpty());
        if (needCouponUserInfo){
            List<Integer> idList = getIdList(openId,userCouponCode);
            if (0 < idList.size()){
                criteria.andIdIn(idList);
            }else{
                return PageBean.build(pageBean, null, 0, pageNo, pageSize);
            }
        }
        if (null != name && !name.isEmpty()){
            criteria.andNameEqualTo(name);
        }
        if (null != status){
            criteria.andStatusEqualTo(status);
        }
        if (null != releaseTotal){
            criteria.andReleaseTotalEqualTo(releaseTotal);
        }
        if (null != couponType){
            Integer ct = null;
            try {
                ct = Integer.valueOf(couponType);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            if (null != ct) {
                criteria.andCouponTypeEqualTo(ct);
            }
        }
        if (null != scenarioType && !scenarioType.isEmpty()){

            Integer st = null;
            try {
                st = Integer.valueOf(scenarioType);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            if (null != st) {
                criteria.andScenarioTypeEqualTo(st);
            }
        }
        if (null != appId && !appId.isEmpty()){
            criteria.andAppIdEqualTo(appId);
        }
        if (null != releaseStartDate){
            criteria.andReleaseStartDateEqualTo(releaseStartDate);
        }
        if (null != releaseEndDate){
            criteria.andReleaseEndDateEqualTo(releaseEndDate);
        }

        Page<Coupon> pages;
        List<Coupon> list;

        try{
            pages = PageHelper.startPage(pageNo, pageSize, true);
            list = mapper.selectByExample(example);
            List<CouponResultBean> coupons = new ArrayList<>();
            for(Coupon coupon: list){
                CouponResultBean b = new CouponResultBean();
                BeanUtils.copyProperties(coupon,b);
                coupons.add(b);
            }

            pageBean = PageBean.build(pageBean, coupons, (int)pages.getTotal(), pageNo, pageSize);

        }catch (Exception e) {
            log.error(e.getMessage(),e);
            pageBean = PageBean.build(pageBean, null, 0, bean.getOffset(), pageSize);
        }

        return pageBean;
    }

    public PageBean searchXCoupon(CouponSearchBean bean) {
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
        map.put("appId",bean.getAppId());
        List<CouponResultBean> coupons = new ArrayList<>();
        total = XMapper.selectCount(map);
        if (total > 0) {
            coupons = XMapper.selectLimit(map);
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
        coupon.setAppId(bean.getAppId());
        coupon.setEffectiveDays(bean.getEffectiveDays());
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
                if(bean.getRules().getScenario().getCouponSkus() != null){
                    coupon.setCouponSkus(bean.getRules().getScenario().getCouponSkus().toJSONString());
                }
//                coupon.setExcludeSkus(bean.getRules().getScenario().getExcludeSkus().toJSONString());
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
        couponBean.setEffectiveDays(coupon.getEffectiveDays());
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
        couponBean.setAppId(coupon.getAppId());
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
            if(coupon.getCouponSkus() != null){
                couponBean.getRules().getScenario().setCouponSkus(JSONArray.parseArray(coupon.getCouponSkus()));
            }
            if(coupon.getExcludeSkus() != null){
                couponBean.getRules().getScenario().setExcludeSkus(JSONArray.parseArray(coupon.getExcludeSkus()));
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
        return useInfoXMapper.updateStatusByCouponId(couponId);
    }

    @Override
    public CouponUseInfoX adminConsumeCoupon(CouponUseInfoBean bean) {

        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = useInfoXMapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            return null;
        }else if (couponUseInfo.getStatus() == 3){
            return couponUseInfo;
        }

        if(couponUseInfo != null){
//            CouponX couponX = XMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
            Date date = new Date();
            if(couponUseInfo.getEffectiveStartDate().after(date) || couponUseInfo.getEffectiveEndDate().before(date)){
                couponUseInfo.setStatus(4);
                return couponUseInfo;
            }
        }

        useInfo.setId(bean.getId());
        useInfo.setConsumedTime(new Date());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setStatus(3);
        useInfoXMapper.updateStatusByUserCode(useInfo);
        log.info("管理端核销consumeCoupon优惠券参数 完成:{}", JSONUtil.toJsonString(useInfo));
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

        List<CouponX> coupons = XMapper.selectGiftCoupon(iAppId);
        coupons.forEach(couponX -> {
            int num = useInfoXMapper.selectCollectCount(couponX.getId(), openId, iAppId);
            couponX.setUserCollectNum(num);
            couponBeans.add(couponToBean(couponX));
        });
        return couponBeans;
    }

    @Override
    public PageableData<Coupon> findReleaseCoupon(Integer pageNo, Integer pageSize, String appId, Integer couponType) {
        PageableData<Coupon> pageableData = new PageableData<>();
        PageInfo<Coupon> releaseCoupon = couponDao.findReleaseCoupon(pageNo, pageSize, appId, couponType);
        PageVo pageVo = ConvertUtil.convertToPageVo(releaseCoupon);
        List<Coupon> groupInfoList = releaseCoupon.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public List<CouponBean> findCouponListByIdList(List<Integer> ids, String openId, String appId) {
        List<CouponX> couponList = XMapper.selectCouponListByIdList(ids);
        List<CouponBean> couponBeanList = new ArrayList<>();
        for (CouponX coupon : couponList) {
            int num = useInfoXMapper.selectCollectCount(coupon.getId(), openId, appId);
            coupon.setUserCollectNum(num);
            CouponBean couponBean = couponToBean(coupon);
            couponBeanList.add(couponBean);
        }
        return couponBeanList;
    }

    @Override
    public List<CouponAndPromBean> findCouponListByMpuList(List<AoyiProdBean> prodBeans, String appId) {
        List<CouponAndPromBean> couponAndPromBeans = new ArrayList<>();
        for(int m = 0; m < prodBeans.size(); m++){
            CouponAndPromBean couponAndPromBean = new CouponAndPromBean();
            AoyiProdBean bean = prodBeans.get(m);
            bean.setAppId(appId);
            Date now = new Date();
            List<PromotionInfoBean> beans = promotionXMapper.selectPromotionInfoByMpu(bean.getMpu(), appId);
            for (int i = 0; i < beans.size(); i++){
                if(beans.get(i).getDailySchedule() != null && beans.get(i).getDailySchedule()){
                    List<PromotionSchedule> promotionSchedules = scheduleDao.findPromotionSchedule(beans.get(i).getScheduleId());
                    if(!promotionSchedules.isEmpty()){
                        PromotionSchedule promotionSchedule = promotionSchedules.get(0);
                        if(promotionSchedule.getEndTime().before(now)){
                            beans.remove(i);
                        }else{
                            beans.get(i).setStartDate(promotionSchedule.getStartTime());
                            beans.get(i).setEndDate(promotionSchedule.getEndTime());
                        }
                    }
                }
            }
            beans.sort(new Comparator<PromotionInfoBean>() {
                @Override
                public int compare(PromotionInfoBean o1, PromotionInfoBean o2) {
                    //以下代码决定按日期升序排序，若将return“-1”与“1”互换，即可实现升序。
                    //getTime 方法返回一个整数值，这个整数代表了从 1970 年 1 月 1 日开始计算到 Date 对象中的时间之间的毫秒数。
                    if (o1.getStartDate().after(o2.getStartDate())) {
                        return 1;
                    } else if (o1.getStartDate().before(o2.getStartDate())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            couponAndPromBean.setPromotions(beans);

            List<CouponBean> couponBeans =  new ArrayList<>();
            XMapper.selectCouponByMpu(bean).forEach(coupon -> {
                couponBeans.add(couponToBean(coupon));
            });
            couponAndPromBean.setCoupons(couponBeans);
            couponAndPromBean.setMpu(bean.getMpu());
            couponAndPromBeans.add(couponAndPromBean);
        }
        return couponAndPromBeans;
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
