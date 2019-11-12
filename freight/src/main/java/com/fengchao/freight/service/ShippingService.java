package com.fengchao.freight.service;

import com.fengchao.freight.bean.CarriageBean;
import com.fengchao.freight.bean.*;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.model.ShippingTemplate;

import java.util.List;

public interface ShippingService {
    int createShipTemplate(ShipTemplateBean bean);

    int deleteShipTemplate(Integer id);

    int updateShipTemplate(ShipTemplateBean bean);

    ShipTemplateBean findShipTemplateById(Integer id);

    PageableData<ShippingTemplate> findShipTemplate(Integer pageNo, Integer pageSize);

    int deleteShipRegions(Integer id);

    int createShipRegions(ShipTemplateBean bean);

    List<ShipTemplateBean> findShipTemplateByMpu(String mpu);

    List<ShipPriceBean> getMpuShipping(List<ShipMpuParam> beans);

    TemplateBean getMpuTemplate(ShipMpuParam bean);

    List<CarriagePriceBean> getMpuCarriage(CarriageBean bean);
}
