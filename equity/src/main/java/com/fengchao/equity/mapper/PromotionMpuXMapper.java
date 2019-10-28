package com.fengchao.equity.mapper;

import com.fengchao.equity.model.PromotionMpu;
import com.fengchao.equity.model.PromotionMpuX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PromotionMpuXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionMpu record);

    int insertSelective(PromotionMpu record);

    int updateByPrimaryKeySelective(PromotionMpu record);

    int updateByPrimaryKey(PromotionMpu record);

    int deleteBypromotionId(PromotionMpu record);

    List<PromotionMpuX> selectByPrimaryMpu(Integer id);

    List<String> selectMpuList(Integer id);

    List<Integer> selectscheduleIdList(Integer id);

    List<PromotionMpuX> selectDaliyPromotionMpu(@Param("promotionId") Integer promotionId, @Param("scheduleId")Integer scheduleId);

    List<String> selectDaliyMpuList(@Param("promotionId") Integer promotionId, @Param("scheduleId")Integer scheduleId);

    List<PromotionMpuX> selectPromotionByMpuList(@Param("mpus") List<String> mpus);

    PromotionMpuX selectByPrimaryIdAndMpu(@Param("mpu")String mpu, @Param("promotionId")Integer promotionId);
}