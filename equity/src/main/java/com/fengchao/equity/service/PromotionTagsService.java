package com.fengchao.equity.service;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.PromotionTags;

public interface PromotionTagsService {
    int createPromotionTags(PromotionTags bean);

    PageableData<PromotionTags> findPromotionTags(Integer pageNo, Integer pageSize);

    int updatePromotionTags(PromotionTags bean);

    int deletePromotionTags(Integer id);
}
