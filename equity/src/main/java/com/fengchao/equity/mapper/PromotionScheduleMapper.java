package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.model.PromotionScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PromotionScheduleMapper {
    long countByExample(PromotionScheduleExample example);

    int deleteByExample(PromotionScheduleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromotionSchedule record);

    int insertSelective(PromotionSchedule record);

    List<PromotionSchedule> selectByExample(PromotionScheduleExample example);

    PromotionSchedule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromotionSchedule record, @Param("example") PromotionScheduleExample example);

    int updateByExample(@Param("record") PromotionSchedule record, @Param("example") PromotionScheduleExample example);

    int updateByPrimaryKeySelective(PromotionSchedule record);

    int updateByPrimaryKey(PromotionSchedule record);
}