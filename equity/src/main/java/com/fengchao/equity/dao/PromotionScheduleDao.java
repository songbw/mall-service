package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionScheduleMapper;
import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.model.PromotionScheduleExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PromotionScheduleDao {

    @Autowired
    private PromotionScheduleMapper mapper;

    public int createPromotionSchedule(PromotionSchedule schedule){
        mapper.insertSelective(schedule);
        return schedule.getId();
    }

    public List<PromotionSchedule> findPromotionSchedule(int scheduleId){
        PromotionScheduleExample example = new PromotionScheduleExample();
        PromotionScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andIstatusEqualTo(1);
        criteria.andIdEqualTo(scheduleId);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }


    public int updatePromotionSchedule(PromotionSchedule schedule){
        mapper.updateByPrimaryKeySelective(schedule);
        return schedule.getId();
    }

    public int removePromotionSchedule(int scheduleId){
        PromotionSchedule promotionSchedule = new PromotionSchedule();
        promotionSchedule.setId(scheduleId);
        promotionSchedule.setUpdateTime(new Date());
        promotionSchedule.setIstatus(2);
        return mapper.updateByPrimaryKeySelective(promotionSchedule);
    }

    public PageInfo<PromotionSchedule> findSchedule(Integer offset, Integer limit) {
        PromotionScheduleExample example = new PromotionScheduleExample();
        PromotionScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andIstatusEqualTo(1);
        PageHelper.startPage(offset, limit);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return new PageInfo<>(schedules);
    }

    public List<PromotionSchedule> findScheduleAll(List<Integer> scheduleIdList) {
        PromotionScheduleExample example = new PromotionScheduleExample();
        PromotionScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(scheduleIdList);
        criteria.andIstatusEqualTo(1);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }

    public List<PromotionSchedule> findByPromotionId(Integer id) {
        PromotionScheduleExample example = new PromotionScheduleExample();
        example.setOrderByClause("start_time");
        PromotionScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andPromotionIdEqualTo(id);
        criteria.andIstatusEqualTo(1);
        List<PromotionSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }
}

