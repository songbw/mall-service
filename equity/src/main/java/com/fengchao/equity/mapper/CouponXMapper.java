package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.AoyiProdBean;
import com.fengchao.equity.bean.CouponResultBean;
import com.fengchao.equity.model.CouponX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Mapper
public interface CouponXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponX record);

    int insertSelective(CouponX record);

    CouponX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponX record);

    int increaseReleaseNumById(Integer id);

    int updateByPrimaryKey(CouponX record);

    int selectCount(HashMap map);

    List<CouponResultBean> selectLimit(HashMap map);

    List<CouponX> selectActiveCouponLimit(HashMap map);

    int selectActiveCouponCount(HashMap map);

    CouponX selectByCodeKey(String code, String appId);

    List<String> selectActiveCategories(@Param("appId")String appId);

    List<String> selectTags(@Param("appId") String appId);

    List<CouponX> selectCouponByMpu(AoyiProdBean aoyiProdBean);

    int couponEffective(int couponId);

    int couponEnd(int couponId);

    List<CouponX> selectGrantCoupon(@Param("appId") String appId);

    List<CouponX> selectGiftCoupon(@Param("appId") String appId);

    List<Integer> selectActiveTagsCoupon(@Param("tagId") Integer tagId);

    List<CouponX> selectCouponListByIdList(List<Integer> ids);
}
