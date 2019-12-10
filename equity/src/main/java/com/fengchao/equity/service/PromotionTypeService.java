package com.fengchao.equity.service;

import com.fengchao.equity.bean.PromotionTypeResDto;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.PromotionType;

import java.util.List;

public interface PromotionTypeService {

    PageableData<PromotionType> getPromotionTypes(int page, int size, String appId);


    /**
     * 获取所有的PromotionType
     *
     * @return
     */
    List<PromotionTypeResDto> queryAllPromotionType();

    Long createPromotionType(PromotionType type);

    Long updatePromotionType(PromotionType type);

    int removePromotionType(Long promotionTypeId);

}
