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

    public List<PromotionInitialSchedule> findInitialSchedule(){
        PromotionInitialScheduleExample example = new PromotionInitialScheduleExample();
        PromotionInitialScheduleExample.Criteria criteria = example.createCriteria();
        List<PromotionInitialSchedule> schedules = mapper.selectByExample(example);
        return schedules;
    }

    public int createInitialSchedule(PromotionInitialSchedule schedule){
        int insert = mapper.insertSelective(schedule);
        return insert;
    }

    public int deleteInitialSchedule() {
        int num = mapper.deleteByPrimaryKey();
        return num;
    }
}
