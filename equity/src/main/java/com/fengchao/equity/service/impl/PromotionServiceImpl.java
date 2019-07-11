package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.mapper.PromotionMapper;
import com.fengchao.equity.mapper.PromotionMpuMapper;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionMpu;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionMapper mapper;
    @Autowired
    private PromotionMpuMapper promotionMpuMapper;
    @Autowired
    private JobClient jobClient;

    @Override
    public int effective(int promotionId) {
        return mapper.promotionEffective(promotionId);
    }

    @Override
    public int createPromotion(Promotion bean) {
        Date date = new Date();
        bean.setCreatedDate(date);
        int num = mapper.insertSelective(bean);
        if(num == 1){
            JobClientUtils.promotionEffectiveTrigger(jobClient, bean.getId(), bean.getStartDate());
            JobClientUtils.promotionEndTrigger(jobClient, bean.getId(), bean.getEndDate());
        }
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
        List<Promotion> promotions = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            promotions = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, promotions, total, offset, limit);
        return pageBean;
    }

    @Override
    public int updatePromotion(Promotion bean) {
        return mapper.updateByPrimaryKeySelective(bean);
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
        map.put("promotionType",bean.getPromotionType());
        map.put("status",bean.getStatus());
        List<Promotion> promotions = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            promotions = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, promotions, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int deletePromotion(Integer id) {
        int num = mapper.deleteByPrimaryKey(id);
        if(num == 1){
            promotionMpuMapper.deleteByPrimaryKey(id);
        }
        return num;
    }

    @Override
    public Promotion findPromotionById(Integer id) {
        Promotion promotion = mapper.selectByPrimaryKey(id);
        promotion.setPromotionSkus(promotionMpuMapper.selectByPrimaryMpu(promotion.getId()));
        return promotion;
    }

    @Override
    public int createContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.insertSelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int updateContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.updateByPrimaryKeySelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int deleteContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionMpu> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionMpuMapper.deleteBypromotionId(promotionSku);
        });
        return num[0];
    }

    @Override
    public Promotion findPromotionToUser(Integer id, Boolean detail) {

        Promotion promotion = mapper.selectByPrimaryKey(id);
        if(detail != null && detail == true){
            promotion.setPromotionSkus(promotionMpuMapper.selectByPrimaryMpu(promotion.getId()));
        }
        return promotion;
    }

    @Override
    public List<PromotionInfoBean> findPromotionInfoByMpu(String mpu) {
        return mapper.selectPromotionInfoByMpu(mpu);
    }

    @Override
    public List<PromotionInfoBean> findPromotionByMpu(String mpu) {
        return mapper.selectPromotionInfoByMpu(mpu);
    }

    @Override
    public int end(int promotionId) {
        return mapper.promotionEnd(promotionId);
    }
}
