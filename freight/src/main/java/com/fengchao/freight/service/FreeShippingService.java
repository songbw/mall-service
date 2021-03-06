package com.fengchao.freight.service;

import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.model.FreeShippingTemplate;

import java.util.List;

public interface FreeShippingService {
    int createFreeShipTemplate(FreeShipTemplateBean bean);

    PageableData<FreeShippingTemplate> findFreeShipTemplate(Integer pageNo, Integer pageSize);

    FreeShipTemplateBean findFreeShipTemplateById(Integer id);

    int updateFreeShipTemplate(FreeShipTemplateBean bean);

    int deleteFreeShipTemplate(Integer id);

    int deleteShipRegions(Integer id);

    int createShipRegions(FreeShipTemplateBean bean);

    List<FreeShipTemplateBean> findTemplateByMerchantId(Integer merchantId);
}
