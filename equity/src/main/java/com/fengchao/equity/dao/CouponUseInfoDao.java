package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.model.CouponUseInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-24 下午2:58
 */
@Component
public class CouponUseInfoDao {

    private CouponUseInfoMapper couponUseInfoMapper;

    @Autowired
    public CouponUseInfoDao(CouponUseInfoMapper couponUseInfoMapper) {
        this.couponUseInfoMapper = couponUseInfoMapper;
    }

    /**
     * 根据id集合查询coupon_use_info列表
     *
     * @param idList
     * @return
     */
    public List<CouponUseInfo> selectByIdList(List<Integer> idList) {
        CouponUseInfoExample couponUseInfoExample = new CouponUseInfoExample();

        CouponUseInfoExample.Criteria criteria = couponUseInfoExample.createCriteria();
        criteria.andIdIn(idList);

        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(couponUseInfoExample);

        return couponUseInfoList;
    }

    public CouponUseInfo findBycouponUserId(int id) {
        return couponUseInfoMapper.selectByPrimaryKey(id);
    }
}
