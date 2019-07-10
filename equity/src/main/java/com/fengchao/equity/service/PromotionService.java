package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.model.Promotion;

import java.util.List;

public interface PromotionService {

    int effective(int promotionId);

    int createPromotion(Promotion bean);

    PageBean findPromotion(Integer offset, Integer limit);

    int updatePromotion(Promotion bean);

    PageBean searchPromotion(PromotionBean bean);

    int deletePromotion(Integer id);

    Promotion findPromotionById(Integer id);

    int createContent(Promotion bean);

    int updateContent(Promotion bean);

    int deleteContent(Promotion bean);

    Promotion findPromotionToUser(Integer id, Boolean detail);

    List<PromotionInfoBean> findPromotionInfoByMpu(String mpu);

    List<PromotionInfoBean> findPromotionByMpu(String mpu);

    int end(int promotionId);
}
