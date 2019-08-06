package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionScheduleMapper;
import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.model.PromotionScheduleExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionScheduleDao {

    @Autowired
    private PromotionScheduleMapper mapper;

    public int createPromotionSchedule(PromotionSchedule schedule){
        mapper.insertSelective(schedule);
        return schedule.getId();
    }

    public PromotionSchedule findPromotionSchedule(int scheduleId){
        PromotionSchedule promotionSchedule = mapper.selectByPrimaryKey(scheduleId);
        return promotionSchedule;
    }


    public int updatePromotionSchedule(PromotionSchedule schedule){
        mapper.updateByPrimaryKeySelective(schedule);
        return schedule.getId();
    }

    public int removePromotionSchedule(int scheduleId){
        return mapper.deleteByPrimaryKey(scheduleId);
    }

    public PageInfo<PromotionSchedule> findSchedule(Integer offset, Integer limit) {
        PromotionScheduleExample example = new PromotionScheduleExample();
        PageHelper.startPage(offset, limit);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return new PageInfo<>(schedules);
    }

    public List<PromotionSchedule> findScheduleAll(List<Integer> scheduleIdList) {
        PromotionScheduleExample example = new PromotionScheduleExample();
        PromotionScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(scheduleIdList);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }
}

