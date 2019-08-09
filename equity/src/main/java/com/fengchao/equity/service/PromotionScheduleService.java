package com.fengchao.equity.service;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.model.PromotionScheduleX;

public interface PromotionScheduleService {
    int createSchedule(PromotionScheduleX bean);

    PageableData<PromotionSchedule> findSchedule(Integer offset, Integer limit);

//    PromotionSchedule findScheduleById(Integer id);

    int deletePromotion(Integer id);

    int updateSchedule(PromotionScheduleX bean);
}
