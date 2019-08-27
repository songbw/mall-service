package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.model.CouponUseInfoX;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CouponUseInfoXMapper {
    int deleteByPrimaryKey(CouponUseInfoBean bean);

    int insert(CouponUseInfoX record);

    int insertSelective(CouponUseInfoX record);

    CouponUseInfoX selectByPrimaryKey(CouponUseInfoBean bean);

    int updateByPrimaryKeySelective(CouponUseInfoX record);

    int updateByPrimaryKey(CouponUseInfoX record);

    int selectCount(HashMap map);

    List<CouponUseInfoX> selectLimit(HashMap map);

    int selectCollectCount(HashMap map);

    int selectCollectCouponNum(CouponUseInfoBean couponUseInfoBean);

    int insertbatchCode(@Param("useInfos") List<CouponUseInfoX> useInfos);

    List<CouponUseInfoX> selectBybatchCode(CouponUseInfoX couponUseInfo);

    int importCode(CouponUseInfoBean bean);

    CouponUseInfoX selectByUserCode(String userCouponCode);

    int updateByUserCode(CouponUseInfoX record);

    int updateStatusByUserCode(CouponUseInfoX record);

    List<CouponUseInfoX> selectCollect(CouponUseInfoBean couponUseInfoBean);

    int updateStatusByCouponId(int couponId);

    int updateStatusByToushiCode(CouponUseInfoX couponUseInfo);

    List<Integer> selectGiftCouponIds(String openId);
}