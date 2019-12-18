package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionMapper;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午4:35
 */
@Component
public class PromotionDao {

    private PromotionMapper promotionMapper;

    @Autowired
    public PromotionDao(PromotionMapper promotionMapper) {
        this.promotionMapper = promotionMapper;
    }

    /**
     * 根据活动id集合，查询活动列表
     *
     * @param promotionIdList
     * @return
     */
    public List<Promotion> selectPromotionListByIdList(List<Integer> promotionIdList) {
        PromotionExample promotionExample = new PromotionExample();
        PromotionExample.Criteria criteria = promotionExample.createCriteria();

        criteria.andIdIn(promotionIdList);

        List<Promotion> promotionList = promotionMapper.selectByExample(promotionExample);

        return promotionList;
    }

    public List<Promotion> selectActivePromotion(String appId) {
        PromotionExample promotionExample = new PromotionExample();
        PromotionExample.Criteria criteria = promotionExample.createCriteria();
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        criteria.andStatusIn(list);
        criteria.andAppIdEqualTo(appId);

        List<Promotion> promotionList = promotionMapper.selectByExample(promotionExample);

        return promotionList;
    }

    public List<Promotion> searchActivePromotion(Boolean dailySchedule, String name, String appId) {
        PromotionExample promotionExample = new PromotionExample();
        PromotionExample.Criteria criteria = promotionExample.createCriteria();
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        criteria.andStatusIn(list);
        if(name != null && !name.equals("") ){
            criteria.andNameLike("%" + name + "%");
        }
        if(dailySchedule != null && !dailySchedule.equals("")){
            criteria.andDailyScheduleEqualTo(dailySchedule);
        }
        criteria.andAppIdEqualTo(appId);

        List<Promotion> promotionList = promotionMapper.selectByExample(promotionExample);

        return promotionList;
    }

    public List<Promotion> findOnlineMpu(String appId) {
        PromotionExample promotionExample = new PromotionExample();
        PromotionExample.Criteria criteria = promotionExample.createCriteria();
        criteria.andStatusEqualTo(4);
        criteria.andAppIdEqualTo(appId);

        List<Promotion> promotionList = promotionMapper.selectByExample(promotionExample);

        return promotionList;
    }
}
