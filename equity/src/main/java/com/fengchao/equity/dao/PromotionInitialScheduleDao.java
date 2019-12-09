package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionInitialScheduleMapper;
import com.fengchao.equity.model.PromotionInitialSchedule;
import com.fengchao.equity.model.PromotionInitialScheduleExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionInitialScheduleDao {
    @Autowired
    private PromotionInitialScheduleMapper mapper;

    public List<PromotionInitialSchedule> findInitialSchedule(String appId){
        PromotionInitialScheduleExample example = new PromotionInitialScheduleExample();
        PromotionInitialScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);

        List<PromotionInitialSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }

    public int createInitialSchedule(PromotionInitialSchedule schedule){
        int insert = mapper.insertSelective(schedule);
        return insert;
    }

    public int deleteInitialSchedule(String appId) {
        PromotionInitialScheduleExample example = new PromotionInitialScheduleExample();
        PromotionInitialScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);

        int num = mapper.deleteByExample(example);
        return num;
    }
}
