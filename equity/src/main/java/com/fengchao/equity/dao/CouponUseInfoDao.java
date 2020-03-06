package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.mapper.CouponUseInfoXMapper;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.model.CouponUseInfoExample;
import com.fengchao.equity.model.CouponUseInfoX;
import com.fengchao.equity.utils.CouponUseStatusEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private CouponUseInfoXMapper couponUseInfoXMapper;

    @Autowired
    public CouponUseInfoDao(CouponUseInfoMapper couponUseInfoMapper,
                            CouponUseInfoXMapper couponUseInfoXMapper) {
        this.couponUseInfoMapper = couponUseInfoMapper;
        this.couponUseInfoXMapper = couponUseInfoXMapper;
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

    public PageInfo<CouponUseInfo> findUnCollectCoupon(CouponUseInfoBean bean) {
        CouponUseInfoExample couponUseInfoExample = new CouponUseInfoExample();

        CouponUseInfoExample.Criteria criteria = couponUseInfoExample.createCriteria();
        criteria.andCouponIdEqualTo(bean.getCouponId());
        criteria.andStatusEqualTo(1);
        criteria.andUserOpenIdIsNull();

        PageHelper.startPage(bean.getOffset(), bean.getLimit());
        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(couponUseInfoExample);

        return new PageInfo<>(couponUseInfoList);
    }

    public int insert(CouponUseInfo couponUseInfo) {
        return couponUseInfoMapper.insertSelective(couponUseInfo);
    }

    public int update(CouponUseInfo couponUseInfo) {
        return couponUseInfoMapper.updateByPrimaryKeySelective(couponUseInfo);
    }

    public CouponUseInfoX findByUserCouponCode(String userCouponCode) {
        return couponUseInfoXMapper.selectByUserCode(userCouponCode);
    }

    public List<CouponUseInfo> selectByCouponIdList(List<Integer> idList) {
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        criteria.andStatusEqualTo(CouponUseStatusEnum.USED.getCode());
        criteria.andOrderIdIsNotNull();
        criteria.andCouponIdIn(idList);

        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(example);

        return couponUseInfoList;
    }

    public List<CouponUseInfo> selectByUserCouponCodeList(List<String> codeLst) {
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        criteria.andStatusEqualTo(CouponUseStatusEnum.USED.getCode());
        criteria.andOrderIdIsNotNull();
        criteria.andUserCouponCodeIn(codeLst);

        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(example);

        return couponUseInfoList;
    }
}
