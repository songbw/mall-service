package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.*;
import com.fengchao.freight.bean.page.PageVo;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.dao.*;
import com.fengchao.freight.model.*;
import com.fengchao.freight.service.ShippingService;
import com.fengchao.freight.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShipTemplateDao shipTemplateDao;
    @Autowired
    private ShipRegionsDao shipRegionsDao;
    @Autowired
    private ShipMpuDao shipMpuDao;
    @Autowired
    private FreeshipTemplateDao freeshipTemplateDao;
    @Autowired
    private FreeShipRegionsDao freeShipRegionsDao;

    @Override
    public int createShipTemplate(ShipTemplateBean bean) {
        int num = 1;
        ShippingTemplate template = new ShippingTemplate();
        template.setMerchantId(bean.getMerchantId());
        template.setName(bean.getName());
        template.setMode(bean.getMode());
        template.setIsDefault(bean.getIsDefault());
        template.setCreateTime(new Date());
        int templateNum = shipTemplateDao.createShipTemplate(template);
        List<ShipRegionsBean> regionsList = bean.getRegions();
        if(templateNum == 1 && regionsList != null && !regionsList.isEmpty()){
            for (ShipRegionsBean regionsBean : regionsList) {
                ShippingRegions regions = new ShippingRegions();
                regions.setTemplateId(template.getId());
                regions.setBaseAmount(regionsBean.getBaseAmount());
                regions.setBasePrice(regionsBean.getBasePrice());
                regions.setCumulativePrice(regionsBean.getCumulativePrice());
                regions.setCumulativeUnit(regionsBean.getCumulativeUnit());
                regions.setName(regionsBean.getName());
                regions.setProvinces(StringUtils.join(regionsBean.getProvinces(), ","));
                int regionsNum = shipRegionsDao.createShipRegions(regions);
                if (regionsNum == 0) {
                    num = 0;
                }
            }
        }
        if(num == 1){
            num = template.getId();
        }
        return num;
    }

    @Override
    public int deleteShipTemplate(Integer id) {
        int num = shipTemplateDao.deleteShipTemplate(id);
        if(num == 1){
            num = shipRegionsDao.deleteShipRegionsByTemplateId(id);
        }
        return num;
    }

    @Override
    public int updateShipTemplate(ShipTemplateBean bean) {
        int num = 1;
        if(bean.getIsDefault() != null && bean.getIsDefault()){
            List<ShippingTemplate> templateList = shipTemplateDao.selectDefaultTemplate();
            if(!templateList.isEmpty()){
                int templatenum = shipTemplateDao.updateTemplateDefault();
                if(templatenum == 0){
                    num = 0;
                    return num;
                }
            }
        }
        ShippingTemplate template = new ShippingTemplate();
        template.setId(bean.getId());
        template.setMerchantId(bean.getMerchantId());
        template.setName(bean.getName());
        template.setMode(bean.getMode());
        template.setIsDefault(bean.getIsDefault());
        template.setUpdateTime(new Date());
        int templateNum = shipTemplateDao.updateShipTemplate(template);
        List<ShipRegionsBean> regionsList = bean.getRegions();
        if(templateNum == 1 && regionsList != null && !regionsList.isEmpty()){
            for (ShipRegionsBean regionsBean : regionsList) {
                ShippingRegions regions = new ShippingRegions();
                regions.setId(regionsBean.getId());
                regions.setTemplateId(bean.getId());
                regions.setBaseAmount(regionsBean.getBaseAmount());
                regions.setBasePrice(regionsBean.getBasePrice());
                regions.setCumulativePrice(regionsBean.getCumulativePrice());
                regions.setCumulativeUnit(regionsBean.getCumulativeUnit());
                regions.setName(regionsBean.getName());
                regions.setProvinces(StringUtils.join(regionsBean.getProvinces(), ","));
                int regionsNum = shipRegionsDao.updateShipRegions(regions);
                if (regionsNum == 0) {
                    num = 0;
                }
            }
        }
        return num;
    }

    @Override
    public ShipTemplateBean findShipTemplateById(Integer id) {
        ShipTemplateBean templateBean = null;
        ShippingTemplateX template = shipTemplateDao.findShipTemplateById(id);
        if(template != null){
            List<ShippingRegionsX> regionsByTemplateId = shipRegionsDao.findRegionsByTemplateId(id);
            template.setRegions(regionsByTemplateId);
            templateBean = convertToTemplateBean(template);
        }
        return templateBean;
    }

    @Override
    public PageableData<ShippingTemplate> findShipTemplate(Integer pageNo, Integer pageSize) {
        PageableData<ShippingTemplate> pageableData = new PageableData<>();
        PageInfo<ShippingTemplate> shipTemplate = shipTemplateDao.findShipTemplate(pageNo, pageSize);

        PageVo pageVo = ConvertUtil.convertToPageVo(shipTemplate);
        List<ShippingTemplate> templateList = shipTemplate.getList();
        pageableData.setList(templateList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public int deleteShipRegions(Integer id) {
        return shipRegionsDao.deleteShipRegions(id);
    }

    @Override
    public int createShipRegions(ShipTemplateBean bean) {
        int num = 1;
        List<ShipRegionsBean> regionsList = bean.getRegions();
        if(regionsList != null && !regionsList.isEmpty()){
            for (ShipRegionsBean regionsBean : regionsList) {
                ShippingRegions regions = new ShippingRegions();
                regions.setId(regionsBean.getId());
                regions.setTemplateId(bean.getId());
                regions.setBaseAmount(regionsBean.getBaseAmount());
                regions.setBasePrice(regionsBean.getBasePrice());
                regions.setCumulativePrice(regionsBean.getCumulativePrice());
                regions.setCumulativeUnit(regionsBean.getCumulativeUnit());
                regions.setName(regionsBean.getName());
                regions.setProvinces(StringUtils.join(regionsBean.getProvinces(), ","));
                int regionsNum = shipRegionsDao.createShipRegions(regions);
                if (regionsNum == 0) {
                    num = 0;
                }
            }
        }
        return num;
    }

    @Override
    public List<ShipTemplateBean> findShipTemplateByMpu(String mpu) {
        List<ShipTemplateBean> templateBean = new ArrayList<>();
        int templateId = 0;
        ShippingMpu shipByMpu = shipMpuDao.findByMpu(mpu);
        if(shipByMpu != null){
            templateId = shipByMpu.getTemplateId();
        }

        List<ShippingTemplateX> shipTemplateByMpu = shipTemplateDao.findShipTemplateByMpu(templateId);
        int finalTemplateId = templateId;
        shipTemplateByMpu.forEach(template -> {
            if(template != null){
                List<ShippingRegionsX> regionsByTemplateId = shipRegionsDao.findRegionsByTemplateId(template.getId());
                template.setRegions(regionsByTemplateId);
                ShipTemplateBean bean = convertToTemplateBean(template);
                if(finalTemplateId == template.getId()){
                    bean.setShipMpuId(shipByMpu.getId());
                }
                templateBean.add(bean);
            }
        });
        return templateBean;
    }

    @Override
    public List<ShipPriceBean> getMpuShipping(List<ShipMpuParam> beans) {
        List<ShipPriceBean> priceBeans = new ArrayList<>();
        for (int j = 0; j < beans.size(); j++){
            int status = 0;
            float shipPrice = 0;
            ShipPriceBean shipPriceBean = new ShipPriceBean();
            ShipMpuParam bean = beans.get(j);
            if(bean.getMerchantId() != null){
                List<FreeShippingTemplate> templateByMerchantId = freeshipTemplateDao.findFreeShipTemplateByMerchantId(bean.getMerchantId());
                if(!templateByMerchantId.isEmpty()){
                    FreeShippingTemplate template = templateByMerchantId.get(0);
                    FreeShippingRegionsX freeRegions = freeShipRegionsDao.findByProvinceId(bean.getProvinceId(), template.getId());
                    if(freeRegions == null){
                        freeRegions = freeShipRegionsDao.findDefaltShipRegions(template.getId());
                    }
                    if(freeRegions.getFullAmount() <= bean.getTotalPrice()){
                        status = 1;
                    }
                }else{
                    FreeShippingTemplateX templateX = freeshipTemplateDao.fingDefaltShipTemplate();
                    if(templateX == null){
                        status = 1;
                    }else{
                        FreeShippingRegionsX freeRegions = freeShipRegionsDao.findByProvinceId(bean.getProvinceId(), templateX.getId());
                        if(freeRegions == null){
                            freeRegions = freeShipRegionsDao.findDefaltShipRegions(templateX.getId());
                        }
                        if(freeRegions.getFullAmount() <= bean.getTotalPrice()){
                            status = 1;
                        }
                    }
                }
            }
            if(status == 0){
                int templateId = 0;
                List<MpuParam> mpuParams = bean.getMpuParams();
                for (int i = 0; i < mpuParams.size(); i++){
                    ShippingMpu shipByMpu = shipMpuDao.findByMpu(mpuParams.get(i).getMpu());
                    if(shipByMpu != null){
                        ShippingTemplateX shipTemplate = shipTemplateDao.findShipTemplateById(shipByMpu.getTemplateId());
                        if(shipTemplate != null){
                            templateId = shipTemplate.getId();
                        }
                    }else{
                        List<ShippingTemplate> templateList = shipTemplateDao.selectDefaultTemplate();
                        if(!templateList.isEmpty()){
                            templateId = templateList.get(0).getId();
                        }
                    }
                    ShippingRegionsX shippingRegions = shipRegionsDao.findByProvinceId(bean.getProvinceId(), templateId);
                    if(shippingRegions == null){
                        shippingRegions = shipRegionsDao.findDefaltShipRegions(templateId);
                    }
                    if(shippingRegions.getBaseAmount() < mpuParams.get(i).getNum()){
                        shipPrice += mpuParams.get(i).getNum()/shippingRegions.getCumulativeUnit() * shippingRegions.getCumulativePrice()
                                + shippingRegions.getBasePrice();
                    }else{
                        shipPrice += shippingRegions.getBasePrice();
                    }
                }
            }
            shipPriceBean.setMerchantId(bean.getMerchantId());
            shipPriceBean.setShipPrice(shipPrice);
            priceBeans.add(shipPriceBean);
        }
        return priceBeans;
    }

    private ShipTemplateBean convertToTemplateBean(ShippingTemplateX template){
        ShipTemplateBean templateBean = new ShipTemplateBean();
        templateBean.setId(template.getId());
        templateBean.setIsDefault(template.getIsDefault());
        templateBean.setMerchantId(template.getMerchantId());
        templateBean.setMode(template.getMode());
        templateBean.setName(template.getName());
        templateBean.setCreateTime(template.getCreateTime());
        templateBean.setUpdateTime(template.getUpdateTime());
        templateBean.setStatus(template.getStatus());
        List<ShipRegionsBean> regionsBeanList = new ArrayList<>();
        List<ShippingRegionsX> templateRegions = template.getRegions();
        if(templateRegions !=null && !templateRegions.isEmpty()){
            templateRegions.forEach(shippingRegionsX -> {
                ShipRegionsBean regionsBean = new ShipRegionsBean();
                regionsBean.setBaseAmount(shippingRegionsX.getBaseAmount());
                regionsBean.setBasePrice(shippingRegionsX.getBasePrice());
                regionsBean.setCumulativePrice(shippingRegionsX.getCumulativePrice());
                regionsBean.setCumulativeUnit(shippingRegionsX.getCumulativeUnit());
                regionsBean.setId(shippingRegionsX.getId());
                regionsBean.setName(shippingRegionsX.getName());
                regionsBean.setTemplateId(shippingRegionsX.getTemplateId());
                regionsBean.setProvinces(shippingRegionsX.getProvinces().split(","));
                regionsBean.setStatus(shippingRegionsX.getStatus());
                regionsBeanList.add(regionsBean);
            });
            templateBean.setRegions(regionsBeanList);
        }
        return templateBean;
    }
}
