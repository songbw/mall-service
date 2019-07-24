package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.model.CouponUseInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CouponUseInfoMapper {
    long countByExample(CouponUseInfoExample example);

    int deleteByExample(CouponUseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CouponUseInfo record);

    int insertSelective(CouponUseInfo record);

    List<CouponUseInfo> selectByExample(CouponUseInfoExample example);

    CouponUseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CouponUseInfo record, @Param("example") CouponUseInfoExample example);

    int updateByExample(@Param("record") CouponUseInfo record, @Param("example") CouponUseInfoExample example);

    int updateByPrimaryKeySelective(CouponUseInfo record);

    int updateByPrimaryKey(CouponUseInfo record);
}