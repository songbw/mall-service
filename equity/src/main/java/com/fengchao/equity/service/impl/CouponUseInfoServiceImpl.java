package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.dao.CouponDao;
import com.fengchao.equity.dao.CouponUseInfoDao;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.mapper.CouponThirdMapper;
import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.mapper.CouponUseInfoXMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fengchao
 * */
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
    private CouponUseInfoDao couponUseInfoDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CardTicketDao ticketDao;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private Environment environment;

    @Override
    public CouponUseInfoBean collectCoupon(CouponUseInfoBean bean) throws EquityException {

        CouponUseInfoX couponUseInfo = new CouponUseInfoX();
        CouponUseInfoBean couponUseInfoBean = new CouponUseInfoBean();

        CouponX coupon = couponXMapper.selectByCodeKey(bean.getCode(), bean.getAppId());
        if(coupon == null){
            couponUseInfoBean.setUserCouponCode("0");
            return couponUseInfoBean;
        }

        if(coupon.getReleaseNum() >= coupon.getReleaseTotal()){
            couponUseInfoBean.setUserCouponCode("1");
            return couponUseInfoBean;
        }

        int collectNum = couponUseInfoDao.selectCollectCount(coupon.getId(), bean.getUserOpenId(), coupon.getAppId());
        if(coupon.getPerLimited() != -1){
            if(collectNum >= coupon.getPerLimited()){
                couponUseInfoBean.setUserCouponCode("2");
                return couponUseInfoBean;
            }
        }

        couponUseInfo.setCouponId(coupon.getId());
        couponUseInfo.setCode(bean.getCode());
        couponUseInfo.setUserOpenId(bean.getUserOpenId());
        couponUseInfo.setAppId(bean.getAppId());
        int num = 0;
        if(coupon.getSupplierMerchantId().equals("3")){
            OperaResult toushiResult = collectThirdCoupon(bean.getUserOpenId(), bean.getCode());
            String url = (String) toushiResult.getData().get("url");
            String user_coupon_code = (String) toushiResult.getData().get("coupon_code");
            try {
                user_coupon_code = new String(AESUtils.decryptAES(AESUtils.base642Byte(user_coupon_code), AESUtils.loadKeyAES()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            couponUseInfo.setCollectedTime(new Date());
            couponUseInfo.setUserCouponCode(coupon.getAppId() + user_coupon_code);
            couponUseInfo.setUrl(url);
            num = mapper.insertSelective(couponUseInfo);
            couponUseInfoBean.setUserCouponCode(user_coupon_code);
        }else if(CouponCollectTypeEnum.MANUAL_ASSIGN.equals(coupon.getCollectType())){
            couponUseInfoBean.setUserCouponCode("3");
            return couponUseInfoBean;
        }else{
            DecimalFormat df=new DecimalFormat("0000");
            String userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + System.currentTimeMillis() + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setCollectedTime(new Date());
            couponUseInfo.setUserCouponCode(userCouponCode);
            couponUseInfo.setEffectiveStartDate(coupon.getEffectiveStartDate());
            couponUseInfo.setEffectiveEndDate(coupon.getEffectiveEndDate());
            num = mapper.insertSelective(couponUseInfo);
            couponUseInfoBean.setUserCouponCode(userCouponCode);
        }


        if(num == 1){
            couponXMapper.increaseReleaseNumById(coupon.getId());

            couponUseInfoBean.setCouponCollectNum(collectNum + 1);
            return couponUseInfoBean;
        }else{
            couponUseInfoBean.setUserCouponCode("3");
            return couponUseInfoBean;
        }
    }

    private OperaResult collectThirdCoupon(String userOpenId, String code) {
        OperaResult result = new OperaResult();
        String url = HttpClient.TOUDHI_PATH + HttpClient.TOUDHI_COUPONCODE;
        WebTarget webTarget = HttpClient.createClient().target(url);
        ThirdResquest resquest = new ThirdResquest();
        ThirdResquestParam param = new ThirdResquestParam();
        try {
            param.setOpen_id(AESUtils.encryptAES(userOpenId.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.setEquity_code(code);
        resquest.setData(param);
        resquest.setTimestamp(String.valueOf(System.currentTimeMillis()) /*(String.valueOf(new Date().getTime())*/);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> props = objectMapper.convertValue(param, Map.class);
        String urlMap = Pkcs8Util.formatUrlMap(props, false, false);
        try {
            String sign = Pkcs8Util.getSign(urlMap);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(resquest, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            OperaResult toushiResult = response.readEntity(OperaResult.class);
            return toushiResult;
        }else{
            result.setCode(700050);
        }
        return result;
    }

    @Override
    public PageBean findCollect(CouponUseInfoBean bean) {

        PageBean pageBean = new PageBean();
        PageInfo<CouponUseInfo> pageInfo = couponUseInfoDao.findCollectCoupon(bean);
        pageBean = PageBean.build(pageBean, pageInfo.getList(), (int)pageInfo.getTotal(), bean.getOffset(), bean.getLimit());
        return pageBean;

    }

    public PageBean findXCollect(CouponUseInfoBean bean) {
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
        map.put("userCouponCode",bean.getUserCouponCode());
        map.put("userOpenId",bean.getUserOpenId());

        List<CouponUseInfoX> coupons = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            log.info("findCollect selectLimit {}", JSON.toJSONString(map));
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
        couponUseInfoBean.setAppId(bean.getAppId());
        return mapper.selectCollectCouponNum(couponUseInfoBean);
    }

    @Override
    public CouponUserResultBean selectCouponByOpenId(CouponUseInfoBean bean) throws EquityException {
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("status",bean.getStatus());
        map.put("userOpenId",bean.getUserOpenId());
        map.put("deleteFlag",0);
        map.put("appId",bean.getAppId());
        List<CouponUseInfoX> couponUseInfos = new ArrayList<>();
        total = mapper.selectCountByOpenId(map);
        if (total > 0) {
            couponUseInfos = mapper.selectLimitByOpenId(map);
            couponUseInfos.forEach(couponUseInfo -> {
                CouponBean couponBean = new CouponBean();
                if(couponUseInfo.getType() == 1){
                    CouponThird couponThird =couponThirdMapper.selectByUserCouponId(couponUseInfo.getId());
                    if(couponThird != null){
                        couponBean.setName(couponThird.getName());
                        couponBean.setEffectiveEndDate(DataUtils.dateFormat(couponThird.getEffectiveEndDate()));
                        couponBean.setEffectiveStartDate(DataUtils.dateFormat(couponThird.getEffectiveStartDate()));
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
        CouponUserResultBean couponUserResultBean = new CouponUserResultBean();
        if(bean.getStatus() != null && bean.getStatus() == 1){
            List<CouponX> coupons = couponXMapper.selectGrantCoupon(bean.getAppId());
            List<CouponBean> couponBeans = new ArrayList<>() ;
            coupons.forEach( coupon -> {
                int num = mapper.selectCollectCount(coupon.getId(), bean.getUserOpenId(), coupon.getAppId());
                coupon.setUserCollectNum(num);
                couponBeans.add(couponToBean(coupon));
            });
            couponUserResultBean.setGrantCoupons(couponBeans);
        }
//        pageBean = PageBean.build(pageBean, couponUseInfos, total, bean.getOffset(), bean.getLimit());
        couponUserResultBean.setList(couponUseInfos);
        couponUserResultBean.setPageNo(bean.getOffset());
        couponUserResultBean.setPageSize(bean.getLimit());
        couponUserResultBean.setTotal(total);
        couponUserResultBean.setPages(PageBean.getPages(total, bean.getLimit()));
        return couponUserResultBean;
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

        Date date = new Date();
        if(coupon.getReleaseStartDate().after(date) || coupon.getReleaseEndDate().before(date)){
            num = 1004;
            return num;
        }

        if(coupon.getCollectType() == null ||
                !CouponCollectTypeEnum.MANUAL_ASSIGN.equals(coupon.getCollectType())){
            num = 1002;
            return num;
        }

        if(coupon.getStatus() == 5){
            num = 1005;
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
            String userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + System.currentTimeMillis() + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setCode(coupon.getCode());
            couponUseInfo.setEffectiveStartDate(coupon.getEffectiveStartDate());
            couponUseInfo.setEffectiveEndDate(coupon.getEffectiveEndDate());
            couponUseInfo.setUserCouponCode(coupon.getAppId() + userCouponCode);
            couponUseInfo.setCouponId(bean.getCouponId());
            couponUseInfo.setAppId(coupon.getAppId());
            useInfos.add(couponUseInfo);
        }

        num = mapper.insertbatchCode(useInfos);
        log.info("{} 生成 num= {}",MyFunctions.WEB_ADMIN_BATCH_CODE,String.valueOf(num));
//        if(num == 1){
//            JobClientUtils.couponInvalidTrigger(jobClient, coupon.getId(), coupon.getEffectiveEndDate());
//        }
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

        if(null == coupon.getCollectType() ||
                !CouponCollectTypeEnum.MANUAL_ASSIGN.equals(coupon.getCollectType())){
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
        useInfo.setAppId(bean.getAppId());
        Date date = new Date();
        useInfo.setCollectedTime(date);
        CouponUseInfoX couponUseInfo = mapper.selectByUserCode(bean.getUserCouponCode());
        if(couponUseInfo == null){
            bean.setUserCouponCode("2");
            return bean;
        }
        if(couponUseInfo.getUserOpenId() != null){
            bean.setUserCouponCode("4");
            return bean;
        }

        CouponX coupon = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
        int collectNum = mapper.selectCollectCount(coupon.getId(), bean.getUserOpenId(), coupon.getAppId());
        if(coupon.getPerLimited() != -1){
            if(collectNum >= coupon.getPerLimited()){
                bean.setUserCouponCode("3");
                return bean;
            }
        }
        if(coupon.getStatus() == 5){
            bean.setUserCouponCode("6");
            return bean;
        }
        int num= mapper.updateByUserCode(useInfo);
        if(num == 1){
            couponXMapper.increaseReleaseNumById(coupon.getId());
            bean.setCouponCode(coupon.getCode());
            Coupon tmpCoupon = couponDao.selectCouponById(coupon.getId());
            bean.setCouponCollectNum(tmpCoupon.getReleaseNum());
            return bean;
        }else{
            bean.setUserCouponCode("5");
            return bean;
        }
    }

    @Override
    public CouponBean selectCouponByEquityId(CouponUseInfoBean bean) throws EquityException {
        CouponX coupon = couponXMapper.selectByPrimaryKey(bean.getCouponId());
        CouponBean couponBean = couponToBean(coupon);
        //List<CouponUseInfoX> useInfos = mapper.selectCollect(bean);
        List<CouponUseInfo> useInfos = couponUseInfoDao.selectCollect(bean);
        if(null != useInfos && 0 < useInfos.size()) {
            List<CouponUseInfoX> useInfoXES = new ArrayList<>();
            for(CouponUseInfo info: useInfos){
                CouponUseInfoX infoX = new CouponUseInfoX();
                BeanUtils.copyProperties(info,infoX);
                useInfoXES.add(infoX);
            }
            couponBean.setCouponUseInfo(useInfoXES);
        }
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
    public int occupyCoupon(CouponUseInfoBean bean) {
        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = mapper.selectByPrimaryKey(bean);
        Date date = new Date();

        if(couponUseInfo == null){
            return 3;
        }else if (couponUseInfo.getStatus() == 3){
            log.info("优惠券已使用参数:{}", JSONUtil.toJsonString(couponUseInfo));
            return 4;
        }

        if(couponUseInfo.getType() == 0){
//            CouponX couponX = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
            if(couponUseInfo.getEffectiveStartDate().after(date) || couponUseInfo.getEffectiveEndDate().before(date)){
                return 2;
            }
        }else{
            CouponThird couponThird = couponThirdMapper.selectByUserCouponId(couponUseInfo.getId());
            if(DataUtils.dateFormat(couponThird.getEffectiveStartDate()).after(date) || DataUtils.dateFormat(couponThird.getEffectiveEndDate()).before(date)){
                return 2;
            }
        }
        useInfo.setId(bean.getId());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        useInfo.setStatus(CouponUseStatusEnum.OCCUPIED.getCode());
        int num = mapper.updateStatusByUserCode(useInfo);
        if(num == 1){
            Coupon coupon = couponDao.selectCouponById(couponUseInfo.getCouponId());
            if(CouponTypeEnum.isWelfareTicket(coupon.getCouponType())){
                ticketDao.occupyCard(bean.getUserCouponCode());
            }
            JobClientUtils.couponReleaseTrigger(environment, jobClient, bean.getId());
        }
        return num;
    }

    @Override
    public int releaseCoupon(CouponUseInfoBean bean) {
        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfoX couponUseInfo = mapper.selectByPrimaryKey(bean);
        if(couponUseInfo != null){
//            CouponX couponX = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
            Integer currentStatus = couponUseInfo.getStatus();
            boolean canRelease = (currentStatus == CouponUseStatusEnum.OCCUPIED.getCode());
            if (!canRelease){
                log.error("该优惠券无法释放, {}",JSON.toJSONString(couponUseInfo));
                return 1;
            }

            Date date = new Date();
            if(couponUseInfo.getEffectiveEndDate().before(date)){
                useInfo.setStatus(CouponUseStatusEnum.INVALID.getCode());
            }else{
                useInfo.setStatus(CouponUseStatusEnum.AVAILABLE.getCode());
            }

        }
        useInfo.setId(bean.getId());
        useInfo.setUserCouponCode(bean.getUserCouponCode());
        Coupon coupon = couponDao.selectCouponById(couponUseInfo.getCouponId());
        if(coupon.getCouponType() != null &&
                CouponTypeEnum.isWelfareTicket(coupon.getCouponType())){
            ticketDao.releaseCard(bean.getUserCouponCode());
        }
        return mapper.updateStatusByUserCode(useInfo);
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

    @Override
    public int verifyCoupon(CouponUseInfoBean bean) {
        CouponUseInfoX couponUseInfo = mapper.selectByPrimaryKey(bean);
            if(couponUseInfo != null){
//                CouponX couponX = couponXMapper.selectByPrimaryKey(couponUseInfo.getCouponId());
                Date date = new Date();
                if(couponUseInfo.getEffectiveStartDate().after(date) || couponUseInfo.getEffectiveEndDate().before(date)){
                    return 2;
                }else{
                    return 1;
                }
            }
        return 0;
    }

    @Override
    public List<Coupon> getCollectGiftCoupon(String openId, String appId) {
        List<Coupon> coupons = new ArrayList<>();
        List<Integer> couponIds = mapper.selectGiftCouponIds(openId, appId);
        if(!couponIds.isEmpty()){
            coupons = couponDao.selectGiftCouponListByIdList(couponIds);
        }

        return coupons;
    }

    @Override
    public CouponUseInfo findBycouponUserId(int couponUserId) {
        return couponUseInfoDao.findBycouponUserId(couponUserId);
    }

    @Override
    public int triggerRelease(int couponUserId) {
        CouponUseInfoX useInfo = new CouponUseInfoX();
        CouponUseInfo couponUseInfo = couponUseInfoDao.findBycouponUserId(couponUserId);
        Date date = new Date();
        if(couponUseInfo != null){
            Integer currentStatus = couponUseInfo.getStatus();
            boolean canRelease = (currentStatus == CouponUseStatusEnum.OCCUPIED.getCode());
            if (!canRelease){
                log.error("该优惠券无法释放, {}",JSON.toJSONString(couponUseInfo));
                return 1;
            }

            if(couponUseInfo.getEffectiveEndDate().before(date)){
                useInfo.setStatus(CouponUseStatusEnum.INVALID.getCode());
            }else{
                useInfo.setStatus(CouponUseStatusEnum.AVAILABLE.getCode());
            }
        }
        useInfo.setId(couponUserId);

        CardTicket cardTicket = ticketDao.findByUseCouponCode(couponUseInfo.getUserCouponCode());
        CardTicket ticket = new CardTicket();
        if(cardTicket != null){
            ticket.setId(cardTicket.getId());
            if(cardTicket.getEndTime().before(date)){
                ticket.setStatus((short)CardTicketStatusEnum.TIMEOUT.getCode());
            }else{
                ticket.setStatus((short)CardTicketStatusEnum.EXCHANGED.getCode());
            }
        }
        ticketDao.update(ticket);
        return mapper.updateByPrimaryKeySelective(useInfo);
    }

    @Override
    public PageableData<CouponUseInfo> findUnCollect(CouponUseInfoBean bean) {
        PageableData<CouponUseInfo> pageableData = new PageableData<>();
        PageInfo<CouponUseInfo> unCollectCoupon = couponUseInfoDao.findUnCollectCoupon(bean);
        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(unCollectCoupon);
        List<CouponUseInfo> groupInfoList = unCollectCoupon.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public CouponUseInfo findBycouponUseId(int couponUseId) {
        return couponUseInfoDao.findBycouponUserId(couponUseId);
    }

    @Override
    public int couponUseInvalid(int couponUseId) {
        CouponUseInfo couponUseInfo = new CouponUseInfo();
        couponUseInfo.setId(couponUseId);
        couponUseInfo.setStatus(CouponUseStatusEnum.INVALID.getCode());
        return couponUseInfoDao.update(couponUseInfo);
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
