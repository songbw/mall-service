package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.PromotionDao;
import com.fengchao.equity.dao.PromotionScheduleDao;
import com.fengchao.equity.dao.PromotionTypeDao;
import com.fengchao.equity.feign.ProdService;
import com.fengchao.equity.mapper.PromotionMpuXMapper;
import com.fengchao.equity.mapper.PromotionScheduleXMapper;
import com.fengchao.equity.mapper.PromotionXMapper;
import com.fengchao.equity.mapper.PromotionMpuMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionXMapper promotionXMapper;
    @Autowired
    private PromotionMpuMapper promotionMpuMapper;
    @Autowired
    private PromotionMpuXMapper mpuXMapper;
    @Autowired
    private PromotionScheduleXMapper scheduleXMapper;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private ProdService prodService;

    @Autowired
    private PromotionDao promotionDao;

    @Autowired
    private PromotionTypeDao promotionTypeDao;

    @Autowired
    private PromotionScheduleDao scheduleDao;

    @Override
    public int effective(int promotionId) {
        return promotionXMapper.promotionEffective(promotionId);
    }

    @Override
    public int createPromotion(PromotionX bean) {
        Date date = new Date();
        bean.setCreatedDate(date);
        int num = promotionXMapper.insertSelective(bean);
        return num;
    }

    @Override
    public PageBean findPromotion(Integer offset, Integer limit) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize", limit);
        List<PromotionX> promotions = new ArrayList<>();
        total = promotionXMapper.selectCount(map);
        if (total > 0) {
            promotions = promotionXMapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, promotions, total, offset, limit);
        return pageBean;
    }

    @Override
    public PromotionResult updatePromotion(PromotionX bean) {
        PromotionResult promotionResult = new PromotionResult();
        PromotionX promotionX  = promotionXMapper.selectByPrimaryKey(bean.getId());
        if(promotionX == null){
            promotionResult.setNum(0);
            return promotionResult;
        }
        Date now = new Date();
        //?????????????????????
        AtomicInteger result = new AtomicInteger();
        AtomicInteger resultPromotionId = new AtomicInteger();
        AtomicReference<Sets.SetView<String>> resultMpus = new AtomicReference<Sets.SetView<String>>();
        if(bean.getStatus() != null && bean.getStatus() == 2){
            List<Promotion> promotions = promotionDao.selectActivePromotion();
//            Promotion overduePromotion = promotionDao.selectOverduePromotion().get(0);
//            promotions.add(overduePromotion);
            if(promotionX.getDailySchedule()!=null && promotionX.getDailySchedule()){
                List<PromotionX> promotionXES = promotionXMapper.selectSchedulePromotion();
                for (int i=0; i < promotionXES.size(); i++){
                    PromotionX daliyPromotion = promotionXES.get(i);
                    boolean isDayTrue = DataUtils.isSameDay(promotionX.getStartDate(), daliyPromotion.getStartDate());
                    if(isDayTrue){
                        promotionResult.setNum(3);
                        promotionResult.setPromotionId(daliyPromotion.getId());
                        return promotionResult;
                    }
                }
                List<PromotionSchedule> schedules = scheduleDao.findByPromotionId(promotionX.getId());
                schedules.forEach(schedule ->{
                    promotions.forEach(promotion->{
                        if(promotion.getDailySchedule() != null && promotion.getDailySchedule()){
                            List<PromotionSchedule> promotionSchedules = scheduleDao.findByPromotionId(promotion.getId());
                            promotionSchedules.forEach(promotionSchedule ->{
                                boolean istrue = DataUtils.isContainDate(schedule.getStartTime(), schedule.getEndTime(),
                                        promotionSchedule.getStartTime(), promotionSchedule.getEndTime());
                                if(istrue){
                                    List<String> mpuList = mpuXMapper.selectDaliyMpuList(promotionSchedule.getPromotionId(), promotionSchedule.getId());
                                    List<String> daliyMpuList = mpuXMapper.selectDaliyMpuList(schedule.getPromotionId(), schedule.getId());
                                    Sets.SetView<String> intersection = Sets.intersection(ImmutableSet.copyOf(mpuList), ImmutableSet.copyOf(daliyMpuList));
                                    if(!intersection.isEmpty()){
                                        result.set(2);
                                        resultMpus.set(intersection);
                                        return;
                                    }
                                }
                            });
                        }else{
                            boolean istrue = DataUtils.isContainDate(schedule.getStartTime(), schedule.getEndTime(),
                                    promotion.getStartDate(), promotion.getEndDate());
                            if(istrue){
                                List<String> mpuList = mpuXMapper.selectMpuList(promotion.getId());
                                List<String> daliyMpuList = mpuXMapper.selectDaliyMpuList(schedule.getPromotionId(), schedule.getId());
                                Sets.SetView<String> intersection = Sets.intersection(ImmutableSet.copyOf(mpuList), ImmutableSet.copyOf(daliyMpuList));
                                if(!intersection.isEmpty()){
                                    result.set(2);
                                    resultMpus.set(intersection);
                                    return;
                                }
                            }
                        }
                    });
                });
            }else{
                promotions.forEach(promotion->{
                    if(promotion.getDailySchedule() != null && promotion.getDailySchedule()){
                        List<PromotionSchedule> promotionSchedules = scheduleDao.findByPromotionId(promotion.getId());
                        promotionSchedules.forEach(promotionSchedule ->{
                            boolean istrue = DataUtils.isContainDate(promotionX.getStartDate(), promotionX.getEndDate(),
                                    promotionSchedule.getStartTime(), promotionSchedule.getEndTime());
                            if(istrue){
                                List<String> mpuList = mpuXMapper.selectDaliyMpuList(promotionSchedule.getPromotionId(), promotionSchedule.getId());
                                List<String> daliyMpuList = mpuXMapper.selectMpuList(promotionX.getId());
                                Sets.SetView<String> intersection = Sets.intersection(ImmutableSet.copyOf(mpuList), ImmutableSet.copyOf(daliyMpuList));
                                if(!intersection.isEmpty()){
                                    result.set(2);
                                    resultMpus.set(intersection);
                                    return;
                                }
                            }
                        });
                    }else{
                        boolean istrue = DataUtils.isContainDate(promotionX.getStartDate(), promotionX.getEndDate(),
                                promotion.getStartDate(), promotion.getEndDate());
                        if(istrue){
                            List<String> mpuList = mpuXMapper.selectMpuList(promotion.getId());
                            List<String> daliyMpuList = mpuXMapper.selectMpuList(promotionX.getId());
//                            boolean b = daliyMpuList.retainAll(mpuList);
                            Sets.SetView<String> intersection = Sets.intersection(ImmutableSet.copyOf(mpuList), ImmutableSet.copyOf(daliyMpuList));
                            if(!intersection.isEmpty()){
                                result.set(2);
                                resultMpus.set(intersection);
                                return;
                            }
                        }
                    }
                });
            }

            int num = result.get();
            if(num != 0){
                promotionResult.setNum(num);
                promotionResult.setMpus(resultMpus.get());
                promotionResult.setPromotionId(resultPromotionId.get());
                return promotionResult;
            }
            if(promotionX.getStartDate().after(now)){
                //?????????
                bean.setStatus(3);
                JobClientUtils.promotionEffectiveTrigger(jobClient, bean.getId(), promotionX.getStartDate());
                JobClientUtils.promotionEndTrigger(jobClient, bean.getId(), promotionX.getEndDate());
            }else if(promotionX.getStartDate().before(now)  && promotionX.getEndDate().after(now)){
                //?????????
                bean.setStatus(4);
                JobClientUtils.promotionEndTrigger(jobClient, bean.getId(), promotionX.getEndDate());
            }else if( promotionX.getEndDate().before(now)){
                //?????????
                bean.setStatus(5);
                //TODO ??????????????????????????????
            }
        }else if(bean.getStatus() != null && bean.getStatus() == 4){  //?????????????????????
            //????????????"????????????"?????????, ??????????????????????????????,?????????????????????????????????
            if( bean.getStartDate() != null ){
                promotionX.setStartDate(bean.getStartDate());
            }
            if(promotionX.getStartDate().before(now)  && promotionX.getEndDate().after(now)){
                //?????????
                bean.setStatus(4);
                JobClientUtils.promotionEndTrigger(jobClient, bean.getId(), promotionX.getEndDate());
            }else if( promotionX.getEndDate().after(now)){
                //?????????
                bean.setStatus(5);
                //TODO ??????????????????????????????
            }
        }else if(bean.getStatus() != null && bean.getStatus() == 5){  //?????????????????????
            bean.setStatus(5);
        }

        int num = result.get();
        if(num != 2){
            num = promotionXMapper.updateByPrimaryKeySelective(bean);
            promotionResult.setNum(num);
        }
        return promotionResult;
    }



    @Override
    public PageBean searchPromotion(PromotionBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("name",bean.getName());
        map.put("promotionTypeId",bean.getPromotionTypeId());
        map.put("discountType",bean.getDiscountType());
        map.put("accountType",bean.getAccountType());
        if(StringUtils.isNotEmpty(bean.getDailySchedule())){
            map.put("dailySchedule",Boolean.valueOf(bean.getDailySchedule()));
        }
        map.put("status",bean.getStatus());
        List<PromotionX> promotions = new ArrayList<>();
        total = promotionXMapper.selectCount(map);
        if (total > 0) {
            promotions = promotionXMapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, promotions, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int deletePromotion(Integer id) {
        PromotionX promotionX = promotionXMapper.selectByPrimaryKey(id);

        if(promotionX.getStatus() != 1){
            return 2;
        }

        int num = promotionXMapper.deleteByPrimaryKey(id);

        return num;
    }

    @Override
    public PromotionX findPromotionById(Integer id) {
        PromotionX promotion = promotionXMapper.selectByPrimaryKey(id);
        if(promotion == null){
            return promotion;
        }
        List<PromotionMpuX> promotionMpus = null;
        promotionMpus = mpuXMapper.selectByPrimaryMpu(promotion.getId());
        List<String> mpuIdList = mpuXMapper.selectMpuList(promotion.getId());
//        List<Integer> scheduleIdList = mpuXMapper.selectscheduleIdList(promotion.getId());

        Map<String, AoyiProdIndex> aoyiProdMap = new HashMap<String, AoyiProdIndex>();
        if(!mpuIdList.isEmpty()){
            OperaResult result = prodService.findProductListByMpuIdList(mpuIdList);
            if (result.getCode() == 200) {
                Object object = result.getData().get("result");
                List<AoyiProdIndex> aoyiProdIndices = JSONObject.parseArray(JSON.toJSONString(object), AoyiProdIndex.class);
                for(AoyiProdIndex prod: aoyiProdIndices){
                    if(prod != null){
                        aoyiProdMap.put(prod.getMpu(), prod);
                    }
                }
            }
        }

        promotionMpus.forEach(promotionMpuX ->{
            AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(promotionMpuX.getMpu());
            if(aoyiProdIndex != null){
                promotionMpuX.setBrand(aoyiProdIndex.getBrand());
                promotionMpuX.setModel(aoyiProdIndex.getModel());
                promotionMpuX.setName(aoyiProdIndex.getName());
                promotionMpuX.setSprice(aoyiProdIndex.getSprice());
                promotionMpuX.setPrice(aoyiProdIndex.getPrice());
                promotionMpuX.setState(aoyiProdIndex.getState());
                String imageUrl = aoyiProdIndex.getImagesUrl();
                if (imageUrl != null && (!"".equals(imageUrl))) {
                    String image = "";
                    if (imageUrl.indexOf("/") == 0) {
                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                    } else {
                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                    }
                    aoyiProdIndex.setImage(image);
                }
                promotionMpuX.setImage(aoyiProdIndex.getImage());
            }
        });
        promotion.setPromotionSkus(promotionMpus);

        List<PromotionSchedule> scheduleAll = scheduleDao.findByPromotionId(promotion.getId());
        promotion.setPromotionSchedules(scheduleAll);

        return promotion;
    }

    @Override
    public int createContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpuX> promotionSkus = bean.getPromotionSkus();
        for(int i = 0; i < promotionSkus.size(); i++){
            PromotionMpuX promotionMpuX = promotionSkus.get(i);
            PromotionMpu promotionMpu = new PromotionMpu();
            promotionMpu.setId(promotionMpuX.getId());
            if(promotionMpuX.getMpu() == null){
                return 0;
            }
            promotionMpu.setMpu(promotionMpuX.getMpu());
            promotionMpu.setSkuid(promotionMpuX.getSkuid());
            promotionMpu.setDiscount(promotionMpuX.getDiscount());
            promotionMpu.setPromotionId(bean.getId());
            promotionMpu.setScheduleId(promotionMpuX.getScheduleId());
            promotionMpu.setPromotionImage(promotionMpuX.getPromotionImage());
            promotionMpu.setPerLimited(promotionMpuX.getPerLimited());
            num[0] = promotionMpuMapper.insertSelective(promotionMpu);
        };
        return num[0];
    }

    @Override
    public int updateContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpuX> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionMpuX ->{
            PromotionMpu promotionMpu = new PromotionMpu();
            promotionMpu.setId(promotionMpuX.getId());
            promotionMpu.setMpu(promotionMpuX.getMpu());
            promotionMpu.setSkuid(promotionMpuX.getSkuid());
            promotionMpu.setDiscount(promotionMpuX.getDiscount());
            promotionMpu.setPromotionId(bean.getId());
            promotionMpu.setScheduleId(promotionMpuX.getScheduleId());
            promotionMpu.setPromotionImage(promotionMpuX.getPromotionImage());
            promotionMpu.setPerLimited(promotionMpuX.getPerLimited());
            num[0] = promotionMpuMapper.updateByPrimaryKeySelective(promotionMpu);
        });
        return num[0];
    }

    @Override
    public int deleteContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpuX> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionMpuX ->{
            PromotionMpu promotionMpu = new PromotionMpu();
            promotionMpu.setId(promotionMpuX.getId());
            promotionMpu.setMpu(promotionMpuX.getMpu());
            promotionMpu.setDiscount(promotionMpuX.getDiscount());
            promotionMpu.setPromotionId(bean.getId());
            promotionMpu.setScheduleId(promotionMpuX.getScheduleId());
            num[0] = mpuXMapper.deleteBypromotionId(promotionMpu);
        });
        return num[0];
    }

    @Override
    public PromotionX findPromotionToUser(Integer id, Boolean detail) {

        PromotionX promotion = promotionXMapper.selectByPrimaryKey(id);
        if(promotion == null){
            return promotion;
        }

        List<PromotionMpuX> promotionMpus = null;
        if(detail != null && detail == true){
            promotionMpus = mpuXMapper.selectByPrimaryMpu(promotion.getId());
            List<String> mpuIdList = mpuXMapper.selectMpuList(promotion.getId());
            OperaResult result = prodService.findProductListByMpuIdList(mpuIdList);
            Map<String, AoyiProdIndex> aoyiProdMap = new HashMap<String, AoyiProdIndex>();
            if (result.getCode() == 200) {
                Object object = result.getData().get("result");
                List<AoyiProdIndex> aoyiProdIndices = JSONObject.parseArray(JSON.toJSONString(object), AoyiProdIndex.class);
                for(AoyiProdIndex prod: aoyiProdIndices){
                    if(prod != null){
                        aoyiProdMap.put(prod.getMpu(), prod);
                    }
                }
                promotionMpus.forEach(promotionMpuX ->{
                    AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(promotionMpuX.getMpu());
                    if(aoyiProdIndex != null){
                        promotionMpuX.setBrand(aoyiProdIndex.getBrand());
                        promotionMpuX.setModel(aoyiProdIndex.getModel());
                        promotionMpuX.setName(aoyiProdIndex.getName());
                        promotionMpuX.setSprice(aoyiProdIndex.getSprice());
                        promotionMpuX.setPrice(aoyiProdIndex.getPrice());
                        promotionMpuX.setState(aoyiProdIndex.getState());
                        String imageUrl = aoyiProdIndex.getImagesUrl();
                        if (imageUrl != null && (!"".equals(imageUrl))) {
                            String image = "";
                            if (imageUrl.indexOf("/") == 0) {
                                image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                            } else {
                                image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                            }
                            aoyiProdIndex.setImage(image);
                        }
                        promotionMpuX.setImage(aoyiProdIndex.getImage());
                    }
//                if(promotionMpuX.getScheduleId() != null){
//                    PromotionSchedule schedule = scheduleDao.findPromotionSchedule(promotionMpuX.getScheduleId());
//                    promotionMpuX.setSchedule(schedule);
//                }
                });
            }
            promotion.setPromotionSkus(promotionMpus);
        }
        List<PromotionSchedule> scheduleAll = scheduleDao.findByPromotionId(promotion.getId());
        promotion.setPromotionSchedules(scheduleAll);
        return promotion;
    }

    @Override
    public List<PromotionInfoBean> findPromotionByMpu(String mpu) {
        Date now = new Date();
        List<PromotionInfoBean> beans = promotionXMapper.selectPromotionInfoByMpu(mpu);
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
                //????????????????????????????????????????????????return???-1?????????1?????????????????????????????????
                //getTime ?????????????????????????????????????????????????????? 1970 ??? 1 ??? 1 ?????????????????? Date ???????????????????????????????????????
                if (o1.getStartDate().after(o2.getStartDate())) {
                    return 1;
                } else if (o1.getStartDate().before(o2.getStartDate())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return beans;
    }

    @Override
    public int end(int promotionId) {
        return promotionXMapper.promotionEnd(promotionId);
    }

    @Override
    public PromotionX findPromotionName(Integer id) {
        return promotionXMapper.selectPromotionName(id);
    }

    @Override
    public List<PromotionBean> findPromotionListByIdList(List<Integer> promotionIdList) throws Exception {
        if (CollectionUtils.isEmpty(promotionIdList)) {
            return Collections.emptyList();
        }

        // ????????????
        List<Promotion> promotionList = promotionDao.selectPromotionListByIdList(promotionIdList);
        log.info("?????????????????? ??????id???????????? ????????????????????????:{}", JSONUtil.toJsonString(promotionList));

        if (CollectionUtils.isEmpty(promotionList)) {
            return Collections.emptyList();
        }

        // ?????????????????????
        List<Long> promotionTypeIdList = promotionList.stream().map(p -> p.getPromotionTypeId()).collect(Collectors.toList());
        log.info("?????????????????? - ?????????????????? ???????????????:{}", JSONUtil.toJsonString(promotionTypeIdList));
        List<PromotionType> promotionTypeList = promotionTypeDao.selectPromotionTypesByIdList(promotionTypeIdList);
        log.info("?????????????????? - ?????????????????? ???????????????:{}", JSONUtil.toJsonString(promotionTypeList));
        // ???map key:promotionTypeId, value:PromotionType
        Map<Long, PromotionType> promotionTypeMap = promotionTypeList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        // ???dto
        List<PromotionBean> promotionBeanList = new ArrayList<>();
        for (Promotion promotion : promotionList) {
            PromotionBean promotionBean = convertToPromotionBean(promotion);
            // ????????????????????????
            promotionBean.setTypeName(promotionTypeMap.get(promotion.getPromotionTypeId()) == null ?
                    "" : promotionTypeMap.get(promotion.getPromotionTypeId()).getTypeName());


            promotionBeanList.add(promotionBean);
        }

        log.info("?????????????????? ??????id???????????? ???dto:{}", JSONUtil.toJsonString(promotionBeanList));

        return promotionBeanList;
    }

    @Override
    public PromotionX findCurrentSchedule(Integer num) {
        if(num == null){
            num = 16;
        }

        List<PromotionX> promotions = promotionXMapper.selectDaliyPromotion();
        if(promotions.isEmpty()){
            return null;
        }else{
            PromotionX promotion = null;
            Date date = new Date();
            for (int i=0; i<promotions.size();i++){
                boolean isSameDay = DataUtils.isSameDay(date, promotions.get(i).getStartDate());
                if(isSameDay){
                    promotion = promotions.get(i);
                }
            }
            if(promotion == null){
                return null;
            }
            List<PromotionSchedule> scheduleAll = scheduleDao.findByPromotionId(promotion.getId());
            promotion.setPromotionSchedules(scheduleAll);
            PromotionScheduleX schedule = scheduleXMapper.selectCurrentSchedule(promotion.getId());

            if(schedule == null){
                schedule = scheduleXMapper.selectSoonSchedule(promotion.getId());
                if(schedule == null){
                    return promotion;
                }
            }
            Integer finalNum = num;
            PageHelper.startPage(1, finalNum);
            List<PromotionMpuX> promotionMpus = mpuXMapper.selectDaliyPromotionMpu(promotion.getId(), schedule.getId());
            PageHelper.startPage(1, finalNum);
            List<String> mpuIdList = mpuXMapper.selectDaliyMpuList(promotion.getId(), schedule.getId());
            OperaResult result = prodService.findProductListByMpuIdList(mpuIdList);
            Map<String, AoyiProdIndex> aoyiProdMap = new HashMap<String, AoyiProdIndex>();
            if (result.getCode() == 200) {
                Object object = result.getData().get("result");
                List<AoyiProdIndex> aoyiProdIndices = JSONObject.parseArray(JSON.toJSONString(object), AoyiProdIndex.class);
                for(AoyiProdIndex prod: aoyiProdIndices){
                    aoyiProdMap.put(prod.getMpu(), prod);
                }
                promotionMpus.forEach(promotionMpuX ->{
                    AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(promotionMpuX.getMpu());
                    promotionMpuX.setBrand(aoyiProdIndex.getBrand());
                    promotionMpuX.setModel(aoyiProdIndex.getModel());
                    promotionMpuX.setName(aoyiProdIndex.getName());
                    promotionMpuX.setSprice(aoyiProdIndex.getSprice());
                    promotionMpuX.setPrice(aoyiProdIndex.getPrice());
                    promotionMpuX.setState(aoyiProdIndex.getState());
                    String imageUrl = aoyiProdIndex.getImagesUrl();
                    if (imageUrl != null && (!"".equals(imageUrl))) {
                        String image = "";
                        if (imageUrl.indexOf("/") == 0) {
                            image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                        } else {
                            image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                        }
                        aoyiProdIndex.setImage(image);
                    }
                    promotionMpuX.setImage(aoyiProdIndex.getImage());
                });
            }
            promotion.setPromotionSkus(promotionMpus);
//            schedule.setPromotionMpus(promotionMpus);
//            bean.setPromotionSchedules(promotionSchedules);
            return promotion;
        }
    }

    @Override
    public PageableData<Promotion> findReleasePromotion(Integer pageNo, Integer pageSize, Boolean dailySchedule, String name) {
        PageableData<Promotion> pageableData = new PageableData<>();
        PageHelper.startPage(pageNo, pageSize);
        List<Promotion> promotions = promotionDao.searchActivePromotion(dailySchedule, name);
        PageInfo<Promotion> promotionPageInfo = new PageInfo<>(promotions);
        PageVo pageVo = ConvertUtil.convertToPageVo(promotionPageInfo);
        List<Promotion> groupInfoList = promotionPageInfo.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public List<PromotionMpuX> findOnlineMpu() {
        List<PromotionMpuX> mpus = new ArrayList();
        List<Promotion> onlineMpu = promotionDao.findOnlineMpu();
        Date now = new Date();
        onlineMpu.forEach(promotion -> {
            if(promotion.getDailySchedule()){
                List<PromotionSchedule> schedules = scheduleDao.findByPromotionId(promotion.getId());
                schedules.forEach(schedule -> {
                    if(schedule.getStartTime().before(now) && schedule.getEndTime().after(now)){
                        List<PromotionMpuX> mpuList = mpuXMapper.selectDaliyPromotionMpu(promotion.getId(), schedule.getId());
                        mpuList.forEach(mpu -> {
                            mpus.add(mpu);
                        });
                    }
                });
            }else{
                List<PromotionMpuX> mpuList = mpuXMapper.selectByPrimaryMpu(promotion.getId());
                mpuList.forEach(mpu -> {
                    mpus.add(mpu);
                });
            }
        });
        return mpus;
    }

    @Override
    public List<PromotionMpuX> findPromotionByMpuList(List<String> mpus) {
        List<PromotionMpuX> promotionMpuXList = mpuXMapper.selectPromotionByMpuList(mpus);
        return promotionMpuXList;
    }

    @Override
    public PromotionX findPromotionToJob(int id) {
        PromotionX promotion = promotionXMapper.selectByPrimaryKey(id);
        return promotion;
    }

    @Override
    public List<PromotionInfoBean> verifyPromotionInfo(List<PromotionMpuBean> beans) {
        Date now = new Date();
        List<PromotionInfoBean> promotions = new ArrayList<>();
        for(int i = 0; i < beans.size(); i++) {
            PromotionMpuBean bean = beans.get(i);
            PromotionInfoBean promotionInfoBean = new PromotionInfoBean();
            PromotionX promotionX = promotionXMapper.selectByPrimaryKey(bean.getId());
            PromotionMpuX promotionMpuX = mpuXMapper.selectByPrimaryIdAndMpu(bean.getMpu(), bean.getId());
            promotionInfoBean.setId(bean.getId());
            promotionInfoBean.setMpu(bean.getMpu());
            if(promotionMpuX != null){
                promotionInfoBean.setStartDate(promotionX.getStartDate());
                promotionInfoBean.setEndDate(promotionX.getEndDate());
                promotionInfoBean.setCreatedDate(promotionX.getCreatedDate());
                promotionInfoBean.setDailySchedule(promotionX.getDailySchedule());
                promotionInfoBean.setName(promotionX.getName());
                promotionInfoBean.setStatus(promotionX.getStatus());
                promotionInfoBean.setSkuid(promotionMpuX.getSkuid());
                promotionInfoBean.setScheduleId(promotionMpuX.getScheduleId());
                if(promotionX.getStatus() != 5){
                    if(promotionX.getDailySchedule() != null && promotionX.getDailySchedule()){
                        List<PromotionSchedule> promotionSchedules = scheduleDao.findPromotionSchedule(promotionMpuX.getScheduleId());
                        if(!promotionSchedules.isEmpty()){
                            PromotionSchedule promotionSchedule = promotionSchedules.get(0);
                            if(promotionSchedule.getStartTime().before(now) && promotionSchedule.getEndTime().after(now)){
                                promotionInfoBean.setStatus(4);
                            }else{
                                promotionInfoBean.setStatus(5);
                            }
                        }
                    }else{
                        if(promotionX.getStartDate().before(now) && promotionX.getEndDate().after(now)){
                            promotionInfoBean.setStatus(4);
                        }else{
                            promotionInfoBean.setStatus(5);
                        }
                    }
                }
            }
            promotions.add(promotionInfoBean);
        }
        return promotions;
    }

    // ====================================== private ==========================

    private PromotionBean convertToPromotionBean(Promotion promotion) {
        PromotionBean promotionBean = new PromotionBean();

        promotionBean.setId(promotion.getId());
        promotionBean.setName(promotion.getName());
        // promotionBean.setTag(promotion.getTag());
        promotionBean.setDiscountType(promotion.getDiscountType());
        promotionBean.setStatus(promotion.getStatus());
        // promotionBean.setStartDate(promotion.getStartDate());
        // promotionBean.setEndDate(promotion.getEndDate());
        // promotionBean.setCreatedDate(promotion.getCreatedDate());
        promotionBean.setAccountType(promotion.getAccountType());

        return promotionBean;
    }
//
//    private PromotionSchduleMpuBean convertToSchduleMpuBean(PromotionX bean) {
//        PromotionSchduleMpuBean promotionSchduleMpuBean = new PromotionSchduleMpuBean();
//
//        promotionSchduleMpuBean.setId(bean.getId());
//        promotionSchduleMpuBean.setName(bean.getName());
//        promotionSchduleMpuBean.setTag(bean.getTag());
//        promotionSchduleMpuBean.setDiscountType(bean.getDiscountType());
//        promotionSchduleMpuBean.setStatus(bean.getStatus());
//        promotionSchduleMpuBean.setStartDate(bean.getStartDate());
//        promotionSchduleMpuBean.setEndDate(bean.getEndDate());
//        promotionSchduleMpuBean.setCreatedDate(bean.getCreatedDate());
//        promotionSchduleMpuBean.setDailySchedule(bean.getDailySchedule());
//
//        return promotionSchduleMpuBean;
//    }
}
