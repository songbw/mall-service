package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.mapper.PromotionMapper;
import com.fengchao.equity.mapper.PromotionSkuMapper;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionSku;
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
    private PromotionSkuMapper promotionSkuMapper;
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
        mapper.deleteByPrimaryKey(id);
        return promotionSkuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Promotion findPromotionById(Integer id) {
        Promotion promotion = mapper.selectByPrimaryKey(id);
        promotion.setPromotionSkus(promotionSkuMapper.selectByPrimarySku(promotion.getId()));
        return promotion;
    }

    @Override
    public int createContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionSku> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionSkuMapper.insertSelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int updateContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionSku> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionSkuMapper.updateByPrimaryKeySelective(promotionSku);
        });
        return num[0];
    }

    @Override
    public int deleteContent(Promotion bean) {
        final int[] num = {0};
        List<PromotionSku> promotionSkus = bean.getPromotionSkus();
        promotionSkus.forEach(promotionSku ->{
            promotionSku.setPromotionId(bean.getId());
            num[0] = promotionSkuMapper.deleteBypromotionId(promotionSku);
        });
        return num[0];
    }

    @Override
    public Promotion findPromotionToUser(Integer id, Boolean detail) {

        Promotion promotion = mapper.selectByPrimaryKey(id);
        if(detail != null && detail == true){
            promotion.setPromotionSkus(promotionSkuMapper.selectByPrimarySku(promotion.getId()));
        }
        return promotion;
    }

    @Override
    public List<PromotionInfoBean> findPromotionInfoBySkuId(String skuId) {
        return mapper.selectPromotionInfoBySku(skuId);
    }

    @Override
    public List<PromotionInfoBean> findPromotionBySkuId(String skuId) {
        return mapper.selectPromotionInfoBySku(skuId);
    }

    @Override
    public int end(int promotionId) {
        return mapper.promotionEnd(promotionId);
    }
}
