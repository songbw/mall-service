package com.fengchao.equity.service;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.PromotionType;

public interface PromotionTypeService {

    PageableData<PromotionType> getPromotionTypes(int page, int size);
}
