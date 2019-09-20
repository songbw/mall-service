package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.ShippingRegionsMapper;
import com.fengchao.freight.mapper.ShippingRegionsXMapper;
import com.fengchao.freight.model.ShippingRegions;
import com.fengchao.freight.model.ShippingRegionsX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipRegionsDao {

    @Autowired
    private ShippingRegionsMapper mapper;
    @Autowired
    private ShippingRegionsXMapper xMapper;

    public int createShipRegions(ShippingRegions regions) {
        return mapper.insertSelective(regions);
    }

    public int updateShipRegions(ShippingRegions regions) {
        return mapper.updateByPrimaryKeySelective(regions);
    }

    public List<ShippingRegionsX> findRegionsByTemplateId(Integer id) {
        return xMapper.findRegionsByTemplateId(id);
    }

    public int deleteShipRegionsByTemplateId(Integer templateId) {
        return xMapper.deleteByTemplateId(templateId);
    }

    public int deleteShipRegions(Integer id) {
        ShippingRegions regions = new ShippingRegions();
        regions.setStatus(2);
        regions.setId(id);
        return mapper.updateByPrimaryKeySelective(regions);
    }
}
