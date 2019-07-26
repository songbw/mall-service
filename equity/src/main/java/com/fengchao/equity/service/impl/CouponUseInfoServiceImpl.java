package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.feign.SSOService;
import com.fengchao.equity.mapper.CouponThirdMapper;
import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class CouponUseInfoServiceImpl implements CouponUseInfoService {

   @Autowired
   private CouponUseInfoMapper mapper;
   @Autowired
   private CouponXMapper couponXMapper;
   @Autowired
   private CouponThirdMapper couponThirdMapper;

   private static Logger logger = LoggerFactory.getLogger(CouponUseInfoServiceImpl.class);

    @Override
    public CouponUseInfoBean collectCoupon(CouponUseInfoBean bean) throws EquityException {
        CouponX couponNew = new CouponX();
        CouponUseInfo couponUseInfo = new CouponUseInfo();
        CouponUseInfoBean couponUseInfoBean = new CouponUseInfoBean();

        CouponX coupon = couponXMapper.selectByCodeKey(bean.getCode());
        if(coupon == null){
            couponUseInfoBean.setUserCouponCode("0");
            return couponUseInfoBean;
        }

        if(coupon.getReleaseNum() >= coupon.getReleaseTotal()){
            couponUseInfoBean.setUserCouponCode("1");
            return couponUseInfoBean;
        }

        HashMap map = new HashMap<>();
        map.put("couponId", coupon.getId());
        map.put("userOpenId", bean.getUserOpenId());
        int collectNum = mapper.selectCollectCount(map);
        if(collectNum >= coupon.getPerLimited()){
            couponUseInfoBean.setUserCouponCode("2");
            return couponUseInfoBean;
        }

        couponUseInfo.setCouponId(coupon.getId());
        couponUseInfo.setCode(bean.getCode());
        int num = 0;
        if(coupon.getSupplierMerchantId().equals("3")){
            ToushiResult result = getCouponUserCode(bean.getUserOpenId(), bean.getCode());
            couponUseInfo.setUserOpenId(bean.getUserOpenId());
            couponUseInfo.setCollectedTime(new Date());
            couponUseInfo.setUserCouponCode(result.getData().getCoupon_code());
            num = mapper.insertSelective(couponUseInfo);
            couponUseInfoBean.setUserCouponCode(result.getData().getCoupon_code());
        } else if(coupon.getCollectType() == 4){
            List<CouponUseInfo> couponUseInfos = mapper.selectBybatchCode(couponUseInfo);
            if(couponUseInfos != null){
                CouponUseInfo useInfo = couponUseInfos.get(0);
                useInfo.setUserOpenId(bean.getUserOpenId());
                useInfo.setCollectedTime(new Date());
                num= mapper.updateByPrimaryKeySelective(useInfo);
                couponUseInfoBean.setUserCouponCode(useInfo.getUserCouponCode());
            }
        }else{
            DecimalFormat df=new DecimalFormat("0000");
            String userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + (int)((Math.random()*9+1)*100000) + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setUserOpenId(bean.getUserOpenId());
            couponUseInfo.setCollectedTime(new Date());
            couponUseInfo.setUserCouponCode(userCouponCode);
            num = mapper.insertSelective(couponUseInfo);
            couponUseInfoBean.setUserCouponCode(userCouponCode);
        }


        if(num == 1){
            couponNew.setId(coupon.getId());
            couponNew.setReleaseNum(coupon.getReleaseNum() + 1);
            couponXMapper.updateByPrimaryKeySelective(couponNew);

            couponUseInfoBean.setCouponCollectNum(collectNum + 1);
            return couponUseInfoBean;
        }else{
            couponUseInfoBean.setUserCouponCode("3");
            return couponUseInfoBean;
        }
    }

    public ToushiResult getCouponUserCode(String openID, String couponCode){
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.TOUDHI_PATH);
        CouponResquest resquest = new CouponResquest();
        EquityParam equityParam = new EquityParam();
        equityParam.setOpen_id(openID);
        equityParam.setEquity_code(couponCode);
        resquest.setData(equityParam);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(equityParam, Map.class);
        String messageString = Pkcs8Util.formatUrlMap(props, false, false) ;
        try {
            String sign = Pkcs8Util.getSign(messageString);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("thirdCoupon request is " + objectMapper.writeValueAsString(resquest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(resquest, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            ToushiResult toushiResult = response.readEntity(ToushiResult.class);
            try {
                logger.info("payment response is " + objectMapper.writeValueAsString(toushiResult));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return toushiResult;
        } else {
            try {
                logger.info("payment error response is " + objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public PageBean findCollect(CouponUseInfoBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("status",bean.getStatus());
        map.put("id",bean.getId());
        map.put("collectedStartDate",bean.getCollectedStartDate());
        map.put("collectedEndDate",bean.getCollectedEndDate());
        map.put("consumedStartDate", bean.getConsumedStartDate());
        map.put("consumedEndDate",bean.getConsumedEndDate());
        List<CouponUseInfo> coupons = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            coupons = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, coupons, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int getCouponNum(CouponUseInfoBean bean) throws EquityException {
        CouponUseInfoBean couponUseInfoBean = new CouponUseInfoBean();
        couponUseInfoBean.setCode(bean.getCode());
        couponUseInfoBean.setUserOpenId(bean.getUserOpenId());
        return mapper.selectCollectCouponNum(couponUseInfoBean);
    }

    @Override
    public PageBean selectCouponByOpenId(CouponUseInfoBean bean) throws EquityException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("status",bean.getStatus());
        map.put("userOpenId",bean.getUserOpenId());
        map.put("deleteFlag",0);
        List<CouponUseInfo> couponUseInfos = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            couponUseInfos = mapper.selectLimit(map);
            couponUseInfos.forEach(couponUseInfo -> {

                CouponBean couponBean = new CouponBean();
                if(couponUseInfo.getType() == 1){
                    CouponThird couponThird =couponThirdMapper.selectByUserCouponId(couponUseInfo.getId());
                    if(couponThird != null){
                        couponBean.setName(couponThird.getName());
                        couponBean.setEffectiveEndDate(DataUtils.dateFormat(couponThird.getEffectiveEndDate()));
                        couponBean.setEffectiveStartDate(DataUtils.dateFormat(couponThird.getEffectiveStartDate()));
                        couponBean.setUrl(couponThird.getUrl());
                        couponBean.setDescription(couponThird.getDescription());
                        couponBean.setSupplierMerchantId(couponThird.getMerchantId());
                        couponBean.setSupplierMerchantName(couponThird.getMerchantName());
                        couponBean.setPrice(couponThird.getPrice());
                    }
                }else if(couponUseInfo.getType() == 0){
                    CouponX couponx = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
                    if(couponx != null){
                        couponBean = couponToBean(couponx);
                    }
                }
                couponUseInfo.setCouponInfo(couponBean);
            });
        }
        pageBean = PageBean.build(pageBean, couponUseInfos, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int batchCode(CouponUseInfoBean bean) throws EquityException {
        int num = 0;
        List<CouponUseInfo> useInfos = new ArrayList<>();
        DecimalFormat df=new DecimalFormat("0000");

        CouponX coupon = couponXMapper.selectByPrimaryKey(bean.getCouponId());
        if(coupon == null){
            num = 1001;
            return num;
        }

        if(coupon.getCollectType() == null || coupon.getCollectType() != 4){
            num = 1002;
            return num;
        }

        HashMap map = new HashMap<>();
        map.put("id", bean.getCouponId());
        int CollectCount = mapper.selectCount(map);
        if(CollectCount > 0){
            num = 1003;
            return num;
        }

        for (int i=0; i < coupon.getReleaseTotal(); i++){
            CouponUseInfo couponUseInfo = new CouponUseInfo();
            String userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + (int)((Math.random()*9+1)*100000) + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setCode(coupon.getCode());
            couponUseInfo.setUserCouponCode(userCouponCode);
            couponUseInfo.setCouponId(bean.getCouponId());
            useInfos.add(couponUseInfo);
        }

        num = mapper.insertbatchCode(useInfos);
        return num;
    }

    @Override
    public int importCode(CouponUseInfoBean bean) throws EquityException {
        int num = 0;
        List<CouponUseInfo> useInfos = new ArrayList<>();
        DecimalFormat df=new DecimalFormat("0000");

        CouponX coupon = couponXMapper.selectByPrimaryKey(bean.getCouponId());
        if(coupon == null){
            num = 1001;
            return num;
        }

        if(coupon.getCollectType() == null || coupon.getCollectType() != 4){
            num = 1002;
            return num;
        }

        HashMap map = new HashMap<>();
        map.put("id", bean.getCouponId());
        int CollectCount = mapper.selectCount(map);
        if(CollectCount > 0){
            num = 1003;
            return num;
        }

        for (int i=0; i < bean.getUserCouponCodes().size(); i++){
            CouponUseInfo couponUseInfo = new CouponUseInfo();
            couponUseInfo.setCode(coupon.getCode());
            couponUseInfo.setUserCouponCode(bean.getUserCouponCodes().get(i));
            couponUseInfo.setCouponId(bean.getCouponId());
            useInfos.add(couponUseInfo);
        }

        num = mapper.importCode(bean);
        return num;
    }

    @Override
    public CouponUseInfoBean redemption(CouponUseInfoBean bean) throws EquityException {
        CouponUseInfo useInfo =new CouponUseInfo();
        useInfo.setUserOpenId(bean.getUserOpenId());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setCollectedTime(new Date());
        CouponUseInfo couponUseInfo = mapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            bean.setUserCouponCode("2");
            return bean;
        }
        int num= mapper.updateByUserCode(useInfo);
        CouponX coupon = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
        if(num == 1){
            coupon.setReleaseNum(coupon.getReleaseNum() + 1);
            couponXMapper.updateByPrimaryKeySelective(coupon);
            bean.setCouponCode(coupon.getCode());
            bean.setCouponCollectNum(coupon.getReleaseNum() + 1);
            return bean;
        }else{
            bean.setUserCouponCode("3");
            return bean;
        }
    }

    @Override
    public CouponBean selectCouponByEquityId(CouponUseInfoBean bean) throws EquityException {
        CouponX coupon = couponXMapper.selectByPrimaryKey(bean.getCouponId());
        CouponBean couponBean = couponToBean(coupon);
        List<CouponUseInfo> useInfos = mapper.selectCollect(bean);
        couponBean.setCouponUseInfo(useInfos);
        return couponBean;
    }

    @Override
    public int deleteUserCoupon(CouponUseInfoBean bean) throws EquityException {
        return mapper.deleteByPrimaryKey(bean);
    }

    @Override
    public CouponUseInfo findById(CouponUseInfoBean bean) throws EquityException {
        CouponUseInfo couponUseInfo = mapper.selectByPrimaryKey(bean);
        if(couponUseInfo != null){
            CouponBean couponBean = couponToBean(couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId()));
            couponUseInfo.setCouponInfo(couponBean);
        }
        return couponUseInfo;
    }

    private CouponBean couponToBean(CouponX coupon) {

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
        coupon.setDescription(coupon.getDescription());
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
}
