package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.ShippingMpuMapper;
import com.fengchao.freight.model.ShippingMpu;
import com.fengchao.freight.model.ShippingMpuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipMpuDao {

    @Autowired
    private ShippingMpuMapper mpuMapper;

    public int createShipMpu(ShippingMpu shippingMpu) {
        return mpuMapper.insertSelective(shippingMpu);
    }

    public int updateShipMpu(ShippingMpu shippingMpu) {
        return mpuMapper.updateByPrimaryKeySelective(shippingMpu);
    }

    public ShippingMpu findByMpu(String mpu) {
        ShippingMpuExample example = new ShippingMpuExample();
        ShippingMpuExample.Criteria criteria = example.createCriteria();

        criteria.andMpuEqualTo(mpu);
        criteria.andStatusEqualTo(1);
        List<ShippingMpu> shippingMpus = mpuMapper.selectByExample(example);

        if(shippingMpus.isEmpty()){
            return null;
        }

        return shippingMpus.get(0);
    }

    public int deleteShipMpu(Integer id) {
        ShippingMpu shippingMpu = new ShippingMpu();
        shippingMpu.setStatus(2);

        ShippingMpuExample example = new ShippingMpuExample();
        ShippingMpuExample.Criteria criteria = example.createCriteria();

        criteria.andTemplateIdEqualTo(id);
        return mpuMapper.updateByExampleSelective(shippingMpu,example);
    }
}
