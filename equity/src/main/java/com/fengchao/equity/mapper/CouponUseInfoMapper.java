package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.model.CouponUseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CouponUseInfoMapper {
    int deleteByPrimaryKey(CouponUseInfoBean bean);

    int insert(CouponUseInfo record);

    int insertSelective(CouponUseInfo record);

    CouponUseInfo selectByPrimaryKey(CouponUseInfoBean bean);

    int updateByPrimaryKeySelective(CouponUseInfo record);

    int updateByPrimaryKey(CouponUseInfo record);

    int selectCount(HashMap map);

    List<CouponUseInfo> selectLimit(HashMap map);

    int selectCollectCount(HashMap map);

    int selectCollectCouponNum(CouponUseInfoBean couponUseInfoBean);

    int insertbatchCode(@Param("useInfos") List<CouponUseInfo> useInfos);

    List<CouponUseInfo> selectBybatchCode(CouponUseInfo couponUseInfo);

    int importCode(CouponUseInfoBean bean);

    CouponUseInfo selectByUserCode(String userCouponCode);

    int updateByUserCode(CouponUseInfo record);

    int updateStatusByUserCode(String userCouponCode);

    List<CouponUseInfo> selectCollect(CouponUseInfoBean couponUseInfoBean);
}