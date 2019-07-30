package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.dao.PromotionDao;
import com.fengchao.equity.feign.ProdService;
import com.fengchao.equity.mapper.PromotionXMapper;
import com.fengchao.equity.mapper.PromotionMpuMapper;
import com.fengchao.equity.model.AoyiProdIndex;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionX;
import com.fengchao.equity.model.PromotionMpu;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.utils.JSONUtil;
import com.fengchao.equity.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionXMapper promotionXMapper;
    @Autowired
    private PromotionMpuMapper promotionMpuMapper;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private ProdService prodService;

    @Autowired
    private PromotionDao promotionDao;

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
    public int updatePromotion(PromotionX bean) {
        PromotionX promotionX = null;

        if(bean.getStatus() != null && bean.getStatus() == 2){
            bean.setStatus(3);
            promotionX = promotionXMapper.selectByPrimaryKey(bean.getId());
            if(promotionX == null){
                return 0;
            }
        }

        int num = promotionXMapper.updateByPrimaryKeySelective(bean);
        if(bean.getStatus() != null && bean.getStatus() == 2 && num ==1){
             JobClientUtils.promotionEffectiveTrigger(jobClient, bean.getId(), promotionX.getStartDate());
             JobClientUtils.promotionEndTrigger(jobClient, bean.getId(), promotionX.getEndDate());
        }

        return num;
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
        List<PromotionMpu> promotionMpus = null;
        promotion.setPromotionSkus(promotionMpuMapper.selectByPrimaryMpu(promotion.getId()));
        promotionMpus = promotionMpuMapper.selectByPrimaryMpu(promotion.getId());
        promotionMpus.forEach(promotionMpu ->{
            OperaResult result = prodService.find(promotionMpu.getMpu());
            if (result.getCode() == 200) {
                Map<String, Object> data = result.getData() ;
                Object object = data.get("result");
                String jsonString = JSON.toJSONString(object);
                AoyiProdIndex aoyiProdIndex = JSONObject.parseObject(jsonString, AoyiProdIndex.class) ;
                promotionMpu.setImage(aoyiProdIndex.getImage());
                promotionMpu.setBrand(aoyiProdIndex.getBrand());
                promotionMpu.setModel(aoyiProdIndex.getModel());
                promotionMpu.setName(aoyiProdIndex.getName());
                promotionMpu.setSprice(aoyiProdIndex.getSprice());
                promotionMpu.setPrice(aoyiProdIndex.getPrice());
                promotionMpu.setState(aoyiProdIndex.getState());
            }
        });
        promotion.setPromotionSkus(promotionMpus);
        return promotion;
    }

    @Override
    public int createContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.insertSelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int updateContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.updateByPrimaryKeySelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int deleteContent(PromotionX bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.deleteBypromotionId(promotionSku);
        });
        return num[0];
    }

    @Override
    public PromotionX findPromotionToUser(Integer id, Boolean detail) {

        PromotionX promotion = promotionXMapper.selectByPrimaryKey(id);
        List<PromotionMpu> promotionMpus = null;
        if(detail != null && detail == true){
            promotionMpus = promotionMpuMapper.selectByPrimaryMpu(promotion.getId());
            promotionMpus.forEach(promotionMpu ->{
                OperaResult result = prodService.find(promotionMpu.getMpu());
                if (result.getCode() == 200) {
                    Map<String, Object> data = result.getData() ;
                    Object object = data.get("result");
                    String jsonString = JSON.toJSONString(object);
                    AoyiProdIndex aoyiProdIndex = JSONObject.parseObject(jsonString, AoyiProdIndex.class) ;
                    promotionMpu.setImage(aoyiProdIndex.getImage());
                    promotionMpu.setBrand(aoyiProdIndex.getBrand());
                    promotionMpu.setModel(aoyiProdIndex.getModel());
                    promotionMpu.setName(aoyiProdIndex.getName());
                    promotionMpu.setSprice(aoyiProdIndex.getSprice());
                    promotionMpu.setPrice(aoyiProdIndex.getPrice());
                    promotionMpu.setState(aoyiProdIndex.getState());
                }
            });
            promotion.setPromotionSkus(promotionMpus);
        }
        return promotion;
    }

    @Override
    public List<PromotionInfoBean> findPromotionInfoByMpu(String mpu) {
        return promotionXMapper.selectPromotionInfoByMpu(mpu);
    }

    @Override
    public List<PromotionInfoBean> findPromotionByMpu(String mpu) {
        return promotionXMapper.selectPromotionInfoByMpu(mpu);
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

        // 执行查询
        List<Promotion> promotionList = promotionDao.selectPromotionListByIdList(promotionIdList);
        log.info("查询活动列表 根据id集合查询 获取到数据库返回:{}", JSONUtil.toJsonString(promotionList));


        // 转dto
        List<PromotionBean> promotionBeanList = new ArrayList<>();
        for (Promotion promotion : promotionList) {
            PromotionBean promotionBean = convertToPromotionBean(promotion);
            promotionBeanList.add(promotionBean);
        }

        log.info("查询活动列表 根据id集合查询 转dto:{}", JSONUtil.toJsonString(promotionBeanList));

        return promotionBeanList;
    }

    // ====================================== private ==========================

    private PromotionBean convertToPromotionBean(Promotion promotion) {
        PromotionBean promotionBean = new PromotionBean();

        promotionBean.setId(promotion.getId());
        promotionBean.setName(promotion.getName());
        // promotionBean.setTag(promotion.getTag());
//        promotionBean.setPromotionType(promotion.getDiscountType());
        promotionBean.setStatus(promotion.getStatus());
        // promotionBean.setStartDate(promotion.getStartDate());
        // promotionBean.setEndDate(promotion.getEndDate());
        // promotionBean.setCreatedDate(promotion.getCreatedDate());

        return promotionBean;
    }
}
