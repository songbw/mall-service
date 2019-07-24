package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.dao.CouponDao;
import com.fengchao.equity.dao.CouponUseInfoDao;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.feign.SSOService;
import com.fengchao.equity.mapper.CouponThirdMapper;
import com.fengchao.equity.mapper.CouponUseInfoXMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.AESUtils;
import com.fengchao.equity.utils.DataUtils;
import com.fengchao.equity.utils.JSONUtil;
import com.fengchao.equity.utils.Pkcs8Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponUseInfoServiceImpl implements CouponUseInfoService {

   @Autowired
   private CouponUseInfoXMapper mapper;
   @Autowired
   private CouponXMapper couponXMapper;
   @Autowired
   private CouponThirdMapper couponThirdMapper;
   @Autowired
   private SSOService ssoService;

    @Autowired
    private CouponUseInfoDao couponUseInfoDao;

    @Autowired
    private CouponDao couponDao;

    @Override
    public CouponUseInfoBean collectCoupon(CouponUseInfoBean bean) throws EquityException {
        CouponX couponNew = new CouponX();
        CouponUseInfoX couponUseInfo = new CouponUseInfoX();
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
        if(coupon.getCollectType() == 4){
            List<CouponUseInfoX> couponUseInfos = mapper.selectBybatchCode(couponUseInfo);
            if(couponUseInfos != null){
                CouponUseInfoX useInfo = couponUseInfos.get(0);
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
        List<CouponUseInfoX> coupons = new ArrayList<>();
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
        List<CouponUseInfoX> couponUseInfos = new ArrayList<>();
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
        List<CouponUseInfoX> useInfos = new ArrayList<>();
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
            CouponUseInfoX couponUseInfo = new CouponUseInfoX();
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
        List<CouponUseInfoX> useInfos = new ArrayList<>();
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
            CouponUseInfoX couponUseInfo = new CouponUseInfoX();
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
        CouponUseInfoX useInfo =new CouponUseInfoX();
        useInfo.setUserOpenId(bean.getUserOpenId());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setCollectedTime(new Date());
        CouponUseInfoX couponUseInfo = mapper.selectByUserCode(bean.getUserCouponCode());
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
        List<CouponUseInfoX> useInfos = mapper.selectCollect(bean);
        couponBean.setCouponUseInfo(useInfos);
        return couponBean;
    }

    @Override
    public int deleteUserCoupon(CouponUseInfoBean bean) throws EquityException {
        return mapper.deleteByPrimaryKey(bean);
    }

    @Override
    public CouponUseInfoX findById(CouponUseInfoBean bean) throws EquityException {
        CouponUseInfoX couponUseInfo = mapper.selectByPrimaryKey(bean);
        if(couponUseInfo != null){
            CouponBean couponBean = couponToBean(couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId()));
            couponUseInfo.setCouponInfo(couponBean);
        }
        return couponUseInfo;
    }

    @Override
    public OperaResult consumedToushi(ToushiResult bean) throws EquityException {
        OperaResult result = new OperaResult();
        if(StringUtils.isEmpty(bean.getSign())){
            result.setCode(700002);
            result.setMsg( "sign签名数据为空");
            return result;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("open_id",bean.getData().getOpen_id());
            map.put("coupon_code",bean.getData().getCoupon_code());
            String urlMap = Pkcs8Util.formatUrlMap(map, false, false);
            boolean verify = Pkcs8Util.verify(urlMap.getBytes(), bean.getSign());
            if(!verify){
                result.setCode(700001);
                result.setMsg( "验签失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(StringUtils.isBlank(bean.getData().getOpen_id())){
            result.setCode(700011);
            result.setMsg("open_id用户ID为空");
            return result;
        }
        if(StringUtils.isBlank(bean.getData().getCoupon_code())){
            result.setCode(700012);
            result.setMsg("code唯一券码为空");
            return result;
        }

        String openID = null;
        String couponCode = null;
        try {
            openID = new String(AESUtils.decryptAES(AESUtils.base642Byte(bean.getData().getOpen_id()), AESUtils.loadKeyAES()));
            if(StringUtils.isEmpty(openID)){
                result.setCode(700003);
                result.setMsg( "open_is解密失败");
                return result;
            }
            couponCode = new String(AESUtils.decryptAES(AESUtils.base642Byte(bean.getData().getCoupon_code()), AESUtils.loadKeyAES()));
            if(StringUtils.isEmpty(couponCode)){
                result.setCode(700004);
                result.setMsg( "couponCode解密失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CouponUseInfoX couponUseInfo = new CouponUseInfoX();
        couponUseInfo.setUserOpenId(openID);
        couponUseInfo.setConsumedTime(new Date());
        couponUseInfo.setUserCouponCode(couponCode);
        couponUseInfo.setStatus(2);
        int num = mapper.updateStatusByToushiCode(couponUseInfo);
        if(num==0){
            result.setCode(700022);
            result.setMsg("核销优惠券失败");
            return result;
        }
        result.getData().put("result",num);
        return result;
    }

    @Override
    public OperaResult obtainCoupon(ToushiResult bean) throws EquityException{
        OperaResult result = new OperaResult();
        if(StringUtils.isEmpty(bean.getSign())){
            result.setCode(700002);
            result.setMsg( "sign签名数据为空");
            return result;
        }
        try {
            ToushiParam toushiParam = bean.getData();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> props = objectMapper.convertValue(toushiParam, Map.class);
            String urlMap = Pkcs8Util.formatUrlMap(props, false, false);
            boolean verify = Pkcs8Util.verify(urlMap.getBytes(), bean.getSign());
            if(!verify){
                result.setCode(700001);
                result.setMsg( "验签失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(bean.getData().getOpen_id())){
            result.setCode(700011);
            result.setMsg( "open_id用户ID为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getCode())){
            result.setCode(700012);
            result.setMsg( "code唯一券码为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getMerchantId())){
            result.setCode(700013);
            result.setMsg( "merchantId商户ID为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getMerchantName())){
            result.setCode(700014);
            result.setMsg( "商户名称为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getName())){
            result.setCode(700015);
            result.setMsg( "券名称为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getPrice())){
            result.setCode(700016);
            result.setMsg( "券价格为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getDescription())){
            result.setCode(700017);
            result.setMsg( "券描述为空");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getEffectiveStartDate())){
            result.setCode(700018);
            result.setMsg( "有效期开始时间为空");
            return result;
        }
        if(!DataUtils.isValidDate(bean.getData().getCoupon().getEffectiveStartDate())){
            result.setCode(700024);
            result.setMsg( "有效期开始时间格式有误");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getEffectiveEndDate())){
            result.setCode(700019);
            result.setMsg( "有效期结束时间为空");
            return result;
        }
        if(!DataUtils.isValidDate(bean.getData().getCoupon().getEffectiveEndDate())){
            result.setCode(700010);
            result.setMsg( "有效期结束时间格式有误");
            return result;
        }
        if(StringUtils.isEmpty(bean.getData().getCoupon().getUrl())){
            result.setCode(700021);
            result.setMsg( "url为空");
            return result;
        }

        String openID = null;
        String couponCode = null;
        try {
            openID = new String(AESUtils.decryptAES(AESUtils.base642Byte(bean.getData().getOpen_id()), AESUtils.loadKeyAES()));
            if(StringUtils.isEmpty(openID)){
                result.setCode(700003);
                result.setMsg( "open_is解密失败");
                return result;
            }
            couponCode = new String(AESUtils.decryptAES(AESUtils.base642Byte(bean.getData().getCoupon().getCode()), AESUtils.loadKeyAES()));
            if(StringUtils.isEmpty(couponCode)){
                result.setCode(700004);
                result.setMsg( "couponCode解密失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CouponUseInfoX couponUseInfo = new CouponUseInfoX();
        couponUseInfo.setType(1);
        couponUseInfo.setUserOpenId(openID);
        couponUseInfo.setCollectedTime(new Date());
        couponUseInfo.setUserCouponCode(couponCode);
        int num = mapper.insertSelective(couponUseInfo);
        if(num == 0){
            result.setCode(700023);
            result.setMsg("领取优惠券失败");
            return result;
        }

        CouponThird couponThird = new CouponThird();
        couponThird.setMerchantId(bean.getData().getMerchantId());
        couponThird.setCouponUserId(couponUseInfo.getId());
        couponThird.setMerchantName(bean.getData().getMerchantName());
        couponThird.setName(bean.getData().getCoupon().getName());
        couponThird.setDescription(bean.getData().getCoupon().getDescription());
        couponThird.setPrice(DataUtils.decimalFormat(bean.getData().getCoupon().getPrice()));
        couponThird.setUrl(bean.getData().getCoupon().getUrl());
        couponThird.setEffectiveEndDate(bean.getData().getCoupon().getEffectiveEndDate());
        couponThird.setEffectiveStartDate(bean.getData().getCoupon().getEffectiveStartDate());
        int number = couponThirdMapper.insertSelective(couponThird);
        if(number==0){
            result.setCode(700023);
            result.setMsg("领取优惠券失败");
            return result;
        }
        result.getData().put("result",number);
        return result;
    }

    @Override
    public OperaResult userVerified(ToushiResult bean){
        OperaResult result = new OperaResult();
        if(StringUtils.isEmpty(bean.getSign())){
            result.setCode(700002);
            result.setMsg( "sign签名数据为空");
            return result;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("open_id",bean.getData().getOpen_id());
            String urlMap = Pkcs8Util.formatUrlMap(map, false, false);
            boolean verify = Pkcs8Util.verify(urlMap.getBytes(), bean.getSign());
            if(!verify){
                result.setCode(700001);
                result.setMsg("验签失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(bean.getData().getOpen_id())){
            result.setCode(700011);
            result.setMsg( "open_id用户ID为空");
            return result;
        }
        String openID = null;
        try {
            openID = new String(AESUtils.decryptAES(AESUtils.base642Byte(bean.getData().getOpen_id()), AESUtils.loadKeyAES()));
            if(StringUtils.isEmpty(openID)){
                result.setCode(700003);
                result.setMsg( "open_is解密失败");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        OperaResult userResult = ssoService.findUser(openID);
        if (userResult.getCode() == 200) {
            Map<String, Object> data = userResult.getData() ;
            Object object = data.get("user");
            String jsonString = JSONUtil.toJsonString(object);
            User user = JSONObject.parseObject(jsonString, User.class) ;
            if(user == null){
                result.setCode(700025);
                result.setMsg( "open_id:"+ bean.getData().getOpen_id() + "用户不存在");
                return result;
            }else{
                return result;
            }
        }
        return result;
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

    @Override
    public List<CouponUseInfoBean> queryByIdList(List<Integer> idList) throws Exception {
        // 1. 查询CouponUseInfo
        log.info("通过id集合查询coupon_use_info列表 数据库入参:{}", JSONUtil.toJsonString(idList));
        List<CouponUseInfo> couponUseInfoList = couponUseInfoDao.selectByIdList(idList);
        log.info("通过id集合查询coupon_use_info列表 数据库返回:{}", JSONUtil.toJsonString(couponUseInfoList));

        // 2. 查询Coupon表
        // 根据第一步获取coupon id集合
        List<Integer> couponIdList = couponUseInfoList.stream().map(c -> c.getCouponId()).collect(Collectors.toList());
        // 查询
        List<Coupon> couponList = couponDao.selectCouponListByIdList(couponIdList);
        log.info("通过id集合查询coupon_use_info列表-查询coupon表 数据库返回:{}", JSONUtil.toJsonString(couponList));
        // 转map key：couponId, value : Coupon
        Map<Integer, Coupon> couponMap = couponList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

        // 转dto
        List<CouponUseInfoBean> couponUseInfoBeanList = new ArrayList<>();
        for (CouponUseInfo couponUseInfo : couponUseInfoList) {
            CouponUseInfoBean couponUseInfoBean = convertToCouponUseInfoBean(couponUseInfo);
            Coupon _coupon = couponMap.get(couponUseInfo.getCouponId());
            if (_coupon != null) {
                couponUseInfoBean.setSupplierMerchantId(_coupon.getSupplierMerchantId());
                couponUseInfoBean.setSupplierMerchantName(_coupon.getSupplierMerchantName());
            }
            couponUseInfoBeanList.add(couponUseInfoBean);
        }

        log.info("通过id集合查询coupon_use_info列表 获取List<CouponUseInfoBean>: {}",
                JSONUtil.toJsonString(couponUseInfoBeanList));

        return couponUseInfoBeanList;
    }

    // ========================= private =====================================

    private CouponUseInfoBean convertToCouponUseInfoBean(CouponUseInfo couponUseInfo) {
        CouponUseInfoBean couponUseInfoBean = new CouponUseInfoBean();

        couponUseInfoBean.setUserOpenId(couponUseInfo.getUserOpenId());
        couponUseInfoBean.setCode(couponUseInfo.getCode());
        couponUseInfoBean.setUserCouponCode(couponUseInfo.getUserCouponCode());
        couponUseInfoBean.setId(couponUseInfo.getId());
        couponUseInfoBean.setStatus(couponUseInfo.getStatus());
        // couponUseInfoBean.setCollectedStartDate(couponUseInfo.getCollectedTime());
        // couponUseInfoBean.setCollectedEndDate(couponUseInfo.getcollected);
        // couponUseInfoBean.setConsumedStartDate();
        // couponUseInfoBean.setConsumedEndDate();
        // couponUseInfoBean.setCouponCollectNum(couponUseInfo.get);
        couponUseInfoBean.setCouponId(couponUseInfo.getCouponId());
        // couponUseInfoBean.setCategoryId(couponUseInfo.);
        // couponUseInfoBean.setCategoryName();
        // couponUseInfoBean.setTagId();
        // couponUseInfoBean.setTagName();
        // couponUseInfoBean.setSupplierMerchantId();
        // couponUseInfoBean.setCouponCode(couponUseInfo.getCode());

        return couponUseInfoBean;
    }
}
