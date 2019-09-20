package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.FreeShipRegionsBean;
import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.dao.FreeShipRegionsDao;
import com.fengchao.freight.dao.FreeshipTemplateDao;
import com.fengchao.freight.model.FreeShippingRegions;
import com.fengchao.freight.model.FreeShippingTemplate;
import com.fengchao.freight.service.FreeShippingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FreeShippingServiceImpl implements FreeShippingService {

    @Autowired
    private FreeshipTemplateDao freeshipTemplateDao;
    @Autowired
    private FreeShipRegionsDao freeShipRegionsDao;

    @Override
    public int createFreeShipTemplate(FreeShipTemplateBean bean) {
        int num = 0;
        FreeShippingTemplate template = new FreeShippingTemplate();
        template.setMerchantId(bean.getMerchantId());
        template.setName(bean.getName());
        template.setMode(bean.getMode());
        template.setIsDefault(bean.getIsDefault());
        template.setCreateTime(new Date());
        int templateNum = freeshipTemplateDao.createFreeShipTemplate(template);
        List<FreeShipRegionsBean> regionsList = bean.getRegions();
        if(templateNum == 1 && !regionsList.isEmpty()){
            for (FreeShipRegionsBean regionsBean : regionsList) {
                FreeShippingRegions regions = new FreeShippingRegions();
                regions.setTemplateId(template.getId());
                regions.setFullAmount(regionsBean.getFullAmount());
                regions.setName(regionsBean.getName());
                regions.setProvinces(StringUtils.join(regionsBean.getProvinces(), ","));
                int regionsNum = freeShipRegionsDao.createFreeShipRegions(regions);
                if (regionsNum == 1) {
                    num = 1;
                }else{
                    num = 0;
                }
            }
        }

        if(num == 0){
            num = template.getId();
        }
        return num;
    }

    @Override
    public PageableData<FreeShippingTemplate> findFreeShipTemplate(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public FreeShipTemplateBean findFreeShipTemplateById(Integer id) {
        return null;
    }

    @Override
    public int updateFreeShipTemplate(FreeShipTemplateBean bean) {
        int num = 0;
        FreeShippingTemplate template = new FreeShippingTemplate();
        template.setId(bean.getId());
        template.setMerchantId(bean.getMerchantId());
        template.setName(bean.getName());
        template.setMode(bean.getMode());
        template.setIsDefault(bean.getIsDefault());
        template.setUpdateTime(new Date());
        int templateNum = freeshipTemplateDao.updateFreeShipTemplate(template);
        List<FreeShipRegionsBean> regionsList = bean.getRegions();
        if(templateNum == 1 && !regionsList.isEmpty()){
            for (FreeShipRegionsBean regionsBean : regionsList) {
                FreeShippingRegions regions = new FreeShippingRegions();
                regions.setId(regionsBean.getId());
                regions.setTemplateId(template.getId());
                regions.setFullAmount(regionsBean.getFullAmount());
                regions.setName(regionsBean.getName());
                regions.setProvinces(StringUtils.join(regionsBean.getProvinces(), ","));
                int regionsNum = freeShipRegionsDao.updateFreeShipRegions(regions);
                if (regionsNum == 1) {
                    num = 1;
                }else{
                    num = 0;
                }
            }
        }
        return num;
    }

    @Override
    public int deleteFreeShipTemplate(Integer id) {
        return freeshipTemplateDao.deleteFreeShipTemplate(id);
    }
}
