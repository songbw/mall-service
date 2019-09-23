package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.FreeShipRegionsBean;
import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.bean.page.PageVo;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.dao.FreeShipRegionsDao;
import com.fengchao.freight.dao.FreeshipTemplateDao;
import com.fengchao.freight.model.FreeShippingRegions;
import com.fengchao.freight.model.FreeShippingRegionsX;
import com.fengchao.freight.model.FreeShippingTemplate;
import com.fengchao.freight.model.FreeShippingTemplateX;
import com.fengchao.freight.service.FreeShippingService;
import com.fengchao.freight.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        int num = 1;
        List<FreeShippingTemplate> templateByMerchantId = null;
        if(bean.getMerchantId() != null){
            templateByMerchantId = freeshipTemplateDao.findFreeShipTemplateByMerchantId(bean.getMerchantId());
        }
        if(templateByMerchantId != null && !templateByMerchantId.isEmpty()){
            num = 2;
            return num;
        }
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
    public PageableData<FreeShippingTemplate> findFreeShipTemplate(Integer pageNo, Integer pageSize) {
        PageableData<FreeShippingTemplate> pageableData = new PageableData<>();
        PageInfo<FreeShippingTemplate> shipTemplate =  freeshipTemplateDao.findFreeShipTemplate(pageNo, pageSize);

        PageVo pageVo = ConvertUtil.convertToPageVo(shipTemplate);
        List<FreeShippingTemplate> templateList = shipTemplate.getList();
        pageableData.setList(templateList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public FreeShipTemplateBean findFreeShipTemplateById(Integer id) {
        FreeShippingTemplateX templateX = freeshipTemplateDao.findFreeShipTemplateById(id);
        templateX.setRegions(freeShipRegionsDao.findFreeShipRegionsByTemplateId(id));
        FreeShipTemplateBean templateBean = convertToTemplateBean(templateX);
        return templateBean;
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
        int num = freeshipTemplateDao.deleteFreeShipTemplate(id);
        if(num == 1){
            num = freeShipRegionsDao.deleteFreeShipRegionsByTemplateId(id);
        }
        return num;
    }

    @Override
    public int deleteShipRegions(Integer id) {
        return freeShipRegionsDao.deleteShipRegions(id);
    }

    @Override
    public int createShipRegions(FreeShipTemplateBean bean) {
        int num = 1;
        List<FreeShipRegionsBean> regionsList = bean.getRegions();
        if(!regionsList.isEmpty()){
            for (FreeShipRegionsBean regionsBean : regionsList) {
                FreeShippingRegions regions = new FreeShippingRegions();
                regions.setTemplateId(bean.getId());
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
            num = bean.getId();
        }
        return num;
    }

    private FreeShipTemplateBean convertToTemplateBean(FreeShippingTemplateX template){
        FreeShipTemplateBean templateBean = new FreeShipTemplateBean();
        templateBean.setId(template.getId());
        templateBean.setIsDefault(template.getIsDefault());
        templateBean.setMerchantId(template.getMerchantId());
        templateBean.setMode(template.getMode());
        templateBean.setName(template.getName());
        templateBean.setCreateTime(template.getCreateTime());
        templateBean.setUpdateTime(template.getUpdateTime());
        templateBean.setStatus(template.getStatus());
        List<FreeShipRegionsBean> regionsBeanList = new ArrayList<>();
        List<FreeShippingRegionsX> templateRegions = template.getRegions();
        if(!templateRegions.isEmpty()){
            templateRegions.forEach(shippingRegionsX -> {
                FreeShipRegionsBean regionsBean = new FreeShipRegionsBean();
                regionsBean.setFullAmount(shippingRegionsX.getFullAmount());
                regionsBean.setId(shippingRegionsX.getId());
                regionsBean.setName(shippingRegionsX.getName());
                regionsBean.setTemplateId(shippingRegionsX.getTemplateId());
                regionsBean.setProvinces(shippingRegionsX.getProvinces().split(","));
                regionsBeanList.add(regionsBean);
            });
            templateBean.setRegions(regionsBeanList);
        }
        return templateBean;
    }
}
