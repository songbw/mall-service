package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.FreeShippingRegionsMapper;
import com.fengchao.freight.mapper.FreeShippingRegionsXMapper;
import com.fengchao.freight.model.FreeShippingRegions;
import com.fengchao.freight.model.FreeShippingRegionsX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FreeShipRegionsDao {

    @Autowired
    private FreeShippingRegionsMapper mapper;
    @Autowired
    private FreeShippingRegionsXMapper xMapper;

    public int createFreeShipRegions(FreeShippingRegions regions) {
        return mapper.insertSelective(regions);
    }

    public int updateFreeShipRegions(FreeShippingRegions regions) {
        return mapper.updateByPrimaryKeySelective(regions);
    }

    public List<FreeShippingRegionsX> findFreeShipRegionsByTemplateId(Integer id) {
        List<FreeShippingRegionsX> regionsList = xMapper.selectByTemplateId(id);
        return regionsList;
    }

    public int deleteFreeShipRegionsByTemplateId(Integer templateId) {
        return xMapper.deleteByTemplateId(templateId);
    }

    public int deleteShipRegions(Integer id) {
        FreeShippingRegions regions = new FreeShippingRegions();
        regions.setId(id);
        regions.setStatus(2);
        return mapper.updateByPrimaryKeySelective(regions);
    }

    public FreeShippingRegionsX findByProvinceId(String provinceId, Integer templateId) {
        return xMapper.selectByProvinceId(provinceId, templateId);
    }

    public FreeShippingRegionsX findDefaltShipRegions(Integer templateId) {
        return xMapper.selectDefaltShipRegions(templateId);
    }
}
