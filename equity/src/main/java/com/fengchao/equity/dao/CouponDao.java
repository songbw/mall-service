package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.CouponMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponExample;
import com.fengchao.equity.model.CouponX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-19 下午2:08
 */
@Component
public class CouponDao {

    private CouponMapper couponMapper;
    private CouponXMapper couponXMapper;

    @Autowired
    public CouponDao(CouponMapper couponMapper, CouponXMapper couponXMapper) {
        this.couponMapper = couponMapper;
        this.couponXMapper = couponXMapper;
    }

    /**
     * 根据id集合查询
     *
     * @param idList
     * @return
     */
    public List<Coupon> selectCouponListByIdList(List<Integer> idList) {
        CouponExample couponExample = new CouponExample();
        CouponExample.Criteria criteria = couponExample.createCriteria();

        criteria.andIdIn(idList);

        List<Coupon> couponList = couponMapper.selectByExample(couponExample);

        return couponList;
    }

    public PageInfo<Coupon> findReleaseCoupon(Integer pageNo, Integer pageSize, String appId, Integer couponType) {
        CouponExample couponExample = new CouponExample();
        CouponExample.Criteria criteria = couponExample.createCriteria();
        criteria.andStatusBetween(3,4);
        criteria.andAppIdEqualTo(appId);
        if(couponType != null){
            criteria.andCouponTypeEqualTo(couponType);
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Coupon> couponList = couponMapper.selectByExample(couponExample);

        return new PageInfo<>(couponList);
    }

    public List<Coupon> selectGiftCouponListByIdList(List<Integer> couponIds) {
        CouponExample couponExample = new CouponExample();
        CouponExample.Criteria criteria = couponExample.createCriteria();

        criteria.andIdIn(couponIds);
        criteria.andCustomerTypeEqualTo(1);

        List<Coupon> couponList = couponMapper.selectByExample(couponExample);

        return couponList;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Coupon selectCouponById(Integer id) {

        return couponMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id查询CouponX
     *
     * @return
     */
    public CouponX selectCouponXById(Integer id) {

        return couponXMapper.selectByPrimaryKey(id);
    }
}
