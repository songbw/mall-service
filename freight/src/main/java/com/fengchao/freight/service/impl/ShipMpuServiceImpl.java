package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.ShipMpuBean;
import com.fengchao.freight.dao.ShipMpuDao;
import com.fengchao.freight.model.ShippingMpu;
import com.fengchao.freight.service.ShipMpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipMpuServiceImpl implements ShipMpuService {

    @Autowired
    private ShipMpuDao dao;

    @Override
    public int createShipMpu(ShipMpuBean bean) {
        int num = 0;
        ShippingMpu shippingMpu = new ShippingMpu();
        shippingMpu.setMpu(bean.getMpu());
        shippingMpu.setTemplateId(bean.getTemplateId());
        int shipMpu = dao.createShipMpu(shippingMpu);
        if(shipMpu == 1){
            num = shippingMpu.getId();
        }
        return num;
    }

    @Override
    public ShippingMpu findShipMpuById(Integer id) {
        return null;
    }

    @Override
    public int updateShipMpu(ShipMpuBean bean) {
        ShippingMpu shippingMpu = new ShippingMpu();
        shippingMpu.setId(bean.getId());
        shippingMpu.setMpu(bean.getMpu());
        shippingMpu.setTemplateId(bean.getTemplateId());
        return dao.updateShipMpu(shippingMpu);
    }

    @Override
    public int deleteShipMpu(Integer id) {
        ShippingMpu shippingMpu = new ShippingMpu();
        shippingMpu.setId(id);
        shippingMpu.setStatus(2);
        return dao.updateShipMpu(shippingMpu);
    }
}