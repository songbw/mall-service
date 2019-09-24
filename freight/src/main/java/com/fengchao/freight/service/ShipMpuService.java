package com.fengchao.freight.service;

import com.fengchao.freight.bean.ShipMpuBean;
import com.fengchao.freight.model.ShippingMpu;

public interface ShipMpuService {

    int createShipMpu(ShipMpuBean bean);

    ShippingMpu findShipMpuById(Integer id);

    int updateShipMpu(ShipMpuBean bean);

    int deleteShipMpu(Integer id);
}
