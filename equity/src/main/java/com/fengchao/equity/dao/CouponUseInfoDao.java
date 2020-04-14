package com.fengchao.equity.dao;

import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.mapper.CouponUseInfoMapper;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.model.CouponUseInfoExample;
import com.fengchao.equity.utils.CouponUseStatusEnum;
import com.fengchao.equity.utils.DataUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-24 下午2:58
 */
@Slf4j
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
     * @param idList id集合
     * @return list
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

    public PageInfo<CouponUseInfo> findCollectCoupon(CouponUseInfoBean bean) {
        //log.info("findCollectCoupon 入参： {}", JSON.toJSONString(bean));
        CouponUseInfoExample couponUseInfoExample = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = couponUseInfoExample.createCriteria();

        Integer id = bean.getId();
        String userOpenId = bean.getUserOpenId();
        String userCouponCode = bean.getUserCouponCode();
        Integer status = bean.getStatus();
        String collectedStartDate = bean.getCollectedStartDate();
        String collectedEndDate = bean.getCollectedEndDate();
        String consumedStartDate = bean.getConsumedStartDate();
        String consumedEndDate = bean.getConsumedEndDate();

        if (null != id){
            criteria.andCouponIdEqualTo(id);
        }
        if (null != userOpenId && !userOpenId.isEmpty()){
            criteria.andUserOpenIdEqualTo(userOpenId);
        }
        if (null != userCouponCode && !userCouponCode.isEmpty()){
            criteria.andUserCouponCodeEqualTo(userCouponCode);
        }
        if (null != status){
            criteria.andStatusEqualTo(status);
        }
        if (null != collectedStartDate && !collectedStartDate.isEmpty()){
            Date collectedStart = DataUtils.dateFormat(collectedStartDate);
            if (null != collectedStart){
                criteria.andCollectedTimeGreaterThanOrEqualTo(collectedStart);
            }
        }
        if (null != collectedEndDate && !collectedEndDate.isEmpty()){
            Date collectedEnd = DataUtils.dateFormat(collectedEndDate);
            if (null != collectedEnd){
                criteria.andCollectedTimeLessThanOrEqualTo(collectedEnd);
            }
        }
        if (null != consumedStartDate && !consumedStartDate.isEmpty()){
            Date consumedStart = DataUtils.dateFormat(consumedStartDate);
            if (null != consumedStart){
                criteria.andConsumedTimeGreaterThanOrEqualTo(consumedStart);
            }
        }
        if (null != consumedEndDate && !consumedEndDate.isEmpty()){
            Date consumedEnd = DataUtils.dateFormat(consumedEndDate);
            if (null != consumedEnd){
                criteria.andConsumedTimeLessThanOrEqualTo(consumedEnd);
            }
        }
        if (1 > bean.getOffset()){
            bean.setOffset(1);
        }
        if (1 > bean.getLimit()){
            bean.setLimit(10);
        }
        PageHelper.startPage(bean.getOffset(), bean.getLimit(),true);
        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(couponUseInfoExample);
        //log.info("findCollectCoupon list： {}", JSON.toJSONString(couponUseInfoList));
        return new PageInfo<>(couponUseInfoList);
    }

    public int insert(CouponUseInfo couponUseInfo) {
        return couponUseInfoMapper.insertSelective(couponUseInfo);
    }

    public int update(CouponUseInfo couponUseInfo) {
        return couponUseInfoMapper.updateByPrimaryKeySelective(couponUseInfo);
    }

    public CouponUseInfo findByUserCouponCode(String userCouponCode) {

        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        if (null != userCouponCode && !userCouponCode.isEmpty()){
            criteria.andUserCouponCodeLike(userCouponCode);
        }

        List<CouponUseInfo> list = couponUseInfoMapper.selectByExample(example);
        if(null == list || 0 == list.size()){
            return null;
        }else{
            return list.get(0);
        }

    }

    public List<CouponUseInfo> selectByCouponIdList(List<Integer> idList) {
        if(null == idList || 0 == idList.size()){
            return new ArrayList<>(0);
        }
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
        if (null == codeLst || 0 == codeLst.size()){
            log.warn("selectByUserCouponCodeList: codeList is null or empty");
            return new ArrayList<>();
        }
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        criteria.andStatusEqualTo(CouponUseStatusEnum.USED.getCode());
        criteria.andOrderIdIsNotNull();
        criteria.andUserCouponCodeIn(codeLst);

        List<CouponUseInfo> couponUseInfoList = couponUseInfoMapper.selectByExample(example);

        return couponUseInfoList;
    }

    public List<CouponUseInfo>
    selectCollect(CouponUseInfoBean couponUseInfoBean){
        if (null == couponUseInfoBean){
            log.error("CouponUseInfo: selectCollect couponUseInfoBean is null");
            return new ArrayList<>();
        }
        if(null == couponUseInfoBean.getCouponId() || null == couponUseInfoBean.getUserOpenId()){
            log.error("CouponUseInfo: selectCollect couponId or userOpenId is null");
            return new ArrayList<>();
        }
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        criteria.andCouponIdEqualTo(couponUseInfoBean.getCouponId());
        criteria.andUserOpenIdEqualTo(couponUseInfoBean.getUserOpenId());

        return couponUseInfoMapper.selectByExample(example);

    }

    public int
    selectCollectCount(Integer couponId, String userOpenId, String appId){

        if(null == couponId || null == userOpenId){
            log.error("CouponUseInfo: selectCollectCount couponId or userOpenId is null");
            return 0;
        }
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(0);
        criteria.andCouponIdEqualTo(couponId);
        criteria.andUserOpenIdEqualTo(userOpenId);
        if(null != appId && !appId.isEmpty()){
            criteria.andAppIdEqualTo(appId);
        }

        return (int)couponUseInfoMapper.countByExample(example);
    }

    public int
    invalidUserCouponByCodeList(List<String> codeLst){

        if (null == codeLst || 0 == codeLst.size()){
            return 0;
        }
        CouponUseInfoExample example = new CouponUseInfoExample();
        CouponUseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(CouponUseStatusEnum.AVAILABLE.getCode());
        criteria.andUserCouponCodeIn(codeLst);

        CouponUseInfo updateRecord = new CouponUseInfo();
        updateRecord.setStatus(CouponUseStatusEnum.INVALID.getCode());

        return couponUseInfoMapper.updateByExampleSelective(updateRecord,example);

    }
}
