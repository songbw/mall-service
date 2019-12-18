package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionInitialSchedule;
import com.fengchao.equity.model.PromotionInitialScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PromotionInitialScheduleMapper {
    long countByExample(PromotionInitialScheduleExample example);

    int deleteByExample(PromotionInitialScheduleExample example);

    int deleteByPrimaryKey();

    int insert(PromotionInitialSchedule record);

    int insertSelective(PromotionInitialSchedule record);

    List<PromotionInitialSchedule> selectByExample(PromotionInitialScheduleExample example);

    PromotionInitialSchedule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromotionInitialSchedule record, @Param("example") PromotionInitialScheduleExample example);

    int updateByExample(@Param("record") PromotionInitialSchedule record, @Param("example") PromotionInitialScheduleExample example);

    int updateByPrimaryKeySelective(PromotionInitialSchedule record);

    int updateByPrimaryKey(PromotionInitialSchedule record);
}