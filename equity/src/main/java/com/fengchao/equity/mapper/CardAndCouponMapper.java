package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.model.CardAndCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CardAndCouponMapper {
    long countByExample(CardAndCouponExample example);

    int deleteByExample(CardAndCouponExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardAndCoupon record);

    int insertSelective(CardAndCoupon record);

    List<CardAndCoupon> selectByExample(CardAndCouponExample example);

    CardAndCoupon selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardAndCoupon record, @Param("example") CardAndCouponExample example);

    int updateByExample(@Param("record") CardAndCoupon record, @Param("example") CardAndCouponExample example);

    int updateByPrimaryKeySelective(CardAndCoupon record);

    int updateByPrimaryKey(CardAndCoupon record);

    List<Integer> selectCouponIdByExample(CardAndCouponExample example);
}