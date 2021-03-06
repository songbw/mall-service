package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.model.PromotionScheduleExample;
import com.fengchao.equity.model.PromotionScheduleX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PromotionScheduleXMapper {
    long countByExample(PromotionScheduleExample example);

    int deleteByExample(PromotionScheduleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromotionSchedule record);

    int insertSelective(PromotionSchedule record);

    List<PromotionScheduleX> selectByExample(PromotionScheduleExample example);

    PromotionScheduleX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromotionSchedule record, @Param("example") PromotionScheduleExample example);

    int updateByExample(@Param("record") PromotionSchedule record, @Param("example") PromotionScheduleExample example);

    int updateByPrimaryKeySelective(PromotionSchedule record);

    int updateByPrimaryKey(PromotionSchedule record);

    PromotionScheduleX selectCurrentSchedule(Integer id);

    PromotionScheduleX selectSoonSchedule(Integer id);

    List<PromotionScheduleX> selectBypromotionId(Integer id);
}