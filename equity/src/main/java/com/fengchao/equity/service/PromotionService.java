package com.fengchao.equity.service;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionMpuX;
import com.fengchao.equity.model.PromotionX;

import java.util.List;

public interface PromotionService {

    int effective(int promotionId);

    int createPromotion(PromotionX bean);

    PageBean findPromotion(Integer offset, Integer limit, String appId);

    PromotionResult updatePromotion(PromotionX bean);

    PageBean searchPromotion(PromotionBean bean);

    int deletePromotion(Integer id);

    PromotionX findPromotionById(Integer id);

    int createContent(PromotionX bean);

    int updateContent(PromotionX bean);

    int deleteContent(PromotionX bean);

    PromotionX findPromotionToUser(Integer id, Boolean detail, String appId);

    List<PromotionInfoBean> findPromotionByMpu(String mpu, String appId);

    int end(int promotionId);

    PromotionX findPromotionName(Integer id);

    /**
     * 根据活动id集合， 查询活动列表
     *
     * @param promotionIdList
     * @return
     */
    List<PromotionBean> findPromotionListByIdList(List<Integer> promotionIdList) throws Exception;

    PromotionX findCurrentSchedule(Integer num, String appId);

    PageableData<Promotion> findReleasePromotion(Integer pageNo, Integer pageSize, Boolean dailySchedule, String name, String appId);

    List<PromotionMpuX> findOnlineMpu(String appId);

    List<PromotionMpuX> findPromotionByMpuList(List<String> mpus, String appId);

    PromotionX findPromotionToJob(int promotionId);

    List<PromotionInfoBean> verifyPromotionInfo(List<PromotionMpuBean> beans);
}
