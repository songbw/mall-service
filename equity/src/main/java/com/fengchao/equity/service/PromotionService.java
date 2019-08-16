package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.bean.PromotionResult;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionX;

import java.util.List;

public interface PromotionService {

    int effective(int promotionId);

    int createPromotion(PromotionX bean);

    PageBean findPromotion(Integer offset, Integer limit);

    PromotionResult updatePromotion(PromotionX bean);

    PageBean searchPromotion(PromotionBean bean);

    int deletePromotion(Integer id);

    PromotionX findPromotionById(Integer id);

    int createContent(PromotionX bean);

    int updateContent(PromotionX bean);

    int deleteContent(PromotionX bean);

    PromotionX findPromotionToUser(Integer id, Boolean detail);

    List<PromotionInfoBean> findPromotionInfoByMpu(String mpu);

    List<PromotionInfoBean> findPromotionByMpu(String mpu);

    int end(int promotionId);

    PromotionX findPromotionName(Integer id);

    /**
     * 根据活动id集合， 查询活动列表
     *
     * @param promotionIdList
     * @return
     */
    List<PromotionBean> findPromotionListByIdList(List<Integer> promotionIdList) throws Exception;

    PromotionX findCurrentSchedule(Integer num);

    List<Promotion> findReleasePromotion();
}
