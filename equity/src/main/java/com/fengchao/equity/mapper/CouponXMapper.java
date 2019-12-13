package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.AoyiProdBean;
import com.fengchao.equity.bean.CouponResultBean;
import com.fengchao.equity.model.CouponX;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CouponXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponX record);

    int insertSelective(CouponX record);

    CouponX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponX record);

    int updateByPrimaryKey(CouponX record);

    int selectCount(HashMap map);

    List<CouponResultBean> selectLimit(HashMap map);

    List<CouponX> selectActiveCouponLimit(HashMap map);

    int selectActiveCouponCount(HashMap map);

    CouponX selectByCodeKey(String code, String appId);

    List<String> selectActiveCategories();

    List<String> selectTags();

    List<CouponX> selectCouponByMpu(AoyiProdBean aoyiProdBean);

    int couponEffective(int couponId);

    int couponEnd(int couponId);

    List<CouponX> selectGrantCoupon();

    List<CouponX> selectGiftCoupon(@Param("appId") String appId);

    List<Integer> selectActiveTagsCoupon(@Param("tagId") Integer tagId);

    List<CouponX> selectCouponListByIdList(List<Integer> ids);
}