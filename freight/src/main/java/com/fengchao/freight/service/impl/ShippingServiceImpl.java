package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.ShipRegionsBean;
import com.fengchao.freight.bean.ShipTemplateBean;
import com.fengchao.freight.bean.page.PageVo;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.dao.ShipRegionsDao;
import com.fengchao.freight.dao.ShipTemplateDao;
import com.fengchao.freight.model.ShippingRegions;
import com.fengchao.freight.model.ShippingRegionsX;
import com.fengchao.freight.model.ShippingTemplate;
import com.fengchao.freight.model.ShippingTemplateX;
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
        if(bean.getIsDefault()){
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
        ShippingTemplateX template = shipTemplateDao.findShipTemplateById(id);
        List<ShippingRegionsX> regionsByTemplateId = shipRegionsDao.findRegionsByTemplateId(id);
        template.setRegions(regionsByTemplateId);
        ShipTemplateBean templateBean = convertToTemplateBean(template);
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
                regionsBeanList.add(regionsBean);
            });
            templateBean.setRegions(regionsBeanList);
        }
        return templateBean;
    }
}
