package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.model.PromotionExample;
import com.fengchao.equity.model.PromotionX;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PromotionXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionX record);

    int insertSelective(PromotionX record);

    PromotionX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionX record);

    int updateByPrimaryKey(PromotionX record);

    int selectCount(HashMap map);

    List<PromotionX> selectLimit(HashMap map);

    List<PromotionX>  selectAll();

    List<PromotionInfoBean> selectPromotionInfoByMpu(@Param("mpu")String mpu, @Param("appId")String appId);

    int promotionEnd(int promotionId);

    int promotionEffective(int promotionId);

    PromotionX selectPromotionName(Integer id);

    List<PromotionX> selectByExample(PromotionExample example);

    int deleteByExample(PromotionExample example);

    long countByExample(PromotionExample example);

    int updateByExampleSelective(@Param("record") PromotionX record, @Param("example") PromotionExample example);

    int updateByExample(@Param("record") PromotionX record, @Param("example") PromotionExample example);

    List<PromotionX> selectDaliyPromotion(@Param("appId") String appId);

    List<PromotionX> selectSchedulePromotion(@Param("appId") String appId);
}