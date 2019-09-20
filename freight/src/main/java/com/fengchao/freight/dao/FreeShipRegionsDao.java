package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.FreeShippingRegionsMapper;
import com.fengchao.freight.model.FreeShippingRegions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreeShipRegionsDao {

    @Autowired
    private FreeShippingRegionsMapper mapper;

    public int createFreeShipRegions(FreeShippingRegions regions) {
        return mapper.insertSelective(regions);
    }

    public int updateFreeShipRegions(FreeShippingRegions regions) {
        return mapper.updateByPrimaryKeySelective(regions);
    }
}
