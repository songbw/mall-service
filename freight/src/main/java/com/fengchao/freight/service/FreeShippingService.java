package com.fengchao.freight.service;

import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.model.FreeShippingTemplate;

public interface FreeShippingService {
    int createFreeShipTemplate(FreeShipTemplateBean bean);

    PageableData<FreeShippingTemplate> findFreeShipTemplate(Integer pageNo, Integer pageSize);

    FreeShipTemplateBean findFreeShipTemplateById(Integer id);

    int updateFreeShipTemplate(FreeShipTemplateBean bean);

    int deleteFreeShipTemplate(Integer id);

    int deleteShipRegions(Integer id);

    int createShipRegions(FreeShipTemplateBean bean);

    FreeShipTemplateBean findTemplateByMerchantId(Integer merchantId);
}
