package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.feign.SSOService;
import com.fengchao.equity.mapper.CouponThirdMapper;
import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CouponThirdService;
import com.fengchao.equity.utils.AESUtils;
import com.fengchao.equity.utils.DataUtils;
import com.fengchao.equity.utils.JSONUtil;
import com.fengchao.equity.utils.Pkcs8Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CouponThirdServiceImpl implements CouponThirdService {

    @Autowired
    private CouponUseInfoMapper mapper;
    @Autowired
    private CouponThirdMapper couponThirdMapper;
    @Autowired
    private CouponXMapper couponXMapper;
    @Autowired
    private SSOService ssoService;

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

        CouponUseInfo couponUseInfo = new CouponUseInfo();
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

        CouponUseInfo couponUseInfo = new CouponUseInfo();
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

    @Override
    public OperaResult equityCreate(ThirdResult bean) {
        OperaResult result = new OperaResult();
        ThirdCouponParam couponBean = bean.getData();
        for(Field f : couponBean.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                if(f.getName().equals("imageUrl")){continue;}
                if(f.get(couponBean) == null){
                    result.setCode(700000);
                    result.setMsg(f.getName() + "不能为空");
                    return result;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        try {
////            ObjectMapper objectMapper = new ObjectMapper();
////            Map<String, Object> props = objectMapper.convertValue(couponBean, Map.class);
////            String urlMap = Pkcs8Util.formatUrlMap(props, false, false);
////            String sign = Pkcs8Util.getSign(urlMap);
////            boolean verify = Pkcs8Util.verify(urlMap.getBytes(), bean.getSign());
////            if(!verify){
////                result.setCode(700001);
////                result.setMsg("验签失败");
////                return result;
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }

        CouponX coupon = new CouponX();
        coupon.setName(couponBean.getName());
        coupon.setSupplierMerchantId(couponBean.getSupplierMerchantId());
        coupon.setSupplierMerchantName(couponBean.getSupplierMerchantName());
        coupon.setReleaseTotal(couponBean.getReleaseTotal());
        coupon.setReleaseStartDate(couponBean.getReleaseStartDate());
        coupon.setReleaseEndDate(couponBean.getReleaseEndDate());
        coupon.setStatus(1);
        coupon.setEffectiveStartDate(couponBean.getEffectiveStartDate());
        coupon.setEffectiveEndDate(couponBean.getEffectiveEndDate());
        coupon.setDescription(couponBean.getDescription());
        coupon.setCreateDate(new Date());
        coupon.setTags("7");
        coupon.setCouponType(3);
        coupon.setImageUrl(couponBean.getImageUrl());
        coupon.setUrl(couponBean.getUrl());
        coupon.setPerLimited(couponBean.getPerLimited());
        String rules = "{\"type\":3,\"serviceCoupon \":{\"price\":"+ DataUtils.decimalFormat(couponBean.getPrice()) +",\"description\":\""+ couponBean.getSupplierMerchantName() +"服务券\"}}";
        coupon.setCouponRules(rules);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        if(coupon.getCode() == null || "".equals(coupon.getCode())){
            coupon.setCode(uuid);
        }
        int num = couponXMapper.insertSelective(coupon);

        if(num == 1){
            result.getData().put("equity_code",uuid);
        }
        return result;
    }

}
