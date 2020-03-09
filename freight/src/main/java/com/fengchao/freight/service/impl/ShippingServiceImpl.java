package com.fengchao.freight.service.impl;

import com.fengchao.freight.bean.CarriageBean;
import com.fengchao.freight.bean.*;
import com.fengchao.freight.bean.page.PageVo;
import com.fengchao.freight.bean.page.PageableData;
import com.fengchao.freight.dao.*;
import com.fengchao.freight.model.*;
import com.fengchao.freight.service.ShippingService;
import com.fengchao.freight.utils.ConvertUtil;
import com.fengchao.freight.utils.JSONUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
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
            shipRegionsDao.deleteShipRegionsByTemplateId(id);
            num = shipMpuDao.deleteShipMpu(id);
        }
        return num;
    }

    @Override
    public int updateShipTemplate(ShipTemplateBean bean) {
        int num = 1;
        if(bean.getIsDefault() != null && bean.getIsDefault()){
            ShippingTemplateX shippingTemplateX = shipTemplateDao.selectDefaultTemplate();
            if(shippingTemplateX != null){
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
                FreeShippingTemplateX templateX = freeshipTemplateDao.findTemplateBymerchantId(bean.getMerchantId());
                if(templateX == null){
                    templateX = freeshipTemplateDao.fingDefaltShipTemplate();
                }

                if(templateX != null){
                    FreeShippingRegionsX freeRegions = freeShipRegionsDao.findByProvinceId(bean.getProvinceId(), templateX.getId());
                    if(freeRegions == null){
                        freeRegions = freeShipRegionsDao.findDefaltShipRegions(templateX.getId());
                    }

                    if(freeRegions != null){
                        if(templateX.getMode() == 0){
                            if(freeRegions.getFullAmount() <= bean.getTotalPrice()){
                                status = 1;
                            }
                        }else if(templateX.getMode() == 1){
                            List<MpuParam> mpuParams = bean.getMpuParams();
                            int mpuNum = 0;
                            for (int i = 0; i < mpuParams.size(); i++){
                                mpuNum += mpuParams.get(i).getNum();
                            }
                            if(freeRegions.getFullAmount() <= mpuNum){
                                status = 1;
                            }
                        }
                    }
                }else{
                    status = 1;
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
                        ShippingTemplateX shippingTemplateX = shipTemplateDao.selectDefaultTemplate();
                        if(shippingTemplateX != null){
                            templateId = shippingTemplateX.getId();
                        }
                    }
                    ShippingRegionsX shippingRegions = shipRegionsDao.findByProvinceId(bean.getProvinceId(), templateId);
                    if(shippingRegions == null){
                        shippingRegions = shipRegionsDao.findDefaltShipRegions(templateId);
                    }
                    if(shippingRegions != null){
                        if(shippingRegions.getBaseAmount() < mpuParams.get(i).getNum()){
                            shipPrice += (mpuParams.get(i).getNum() - shippingRegions.getBaseAmount())/shippingRegions.getCumulativeUnit() * shippingRegions.getCumulativePrice()
                                    + shippingRegions.getBasePrice();
                        }else{
                            shipPrice += shippingRegions.getBasePrice();
                        }
                    }
                }
            }
            shipPriceBean.setMerchantId(bean.getMerchantId());
            shipPriceBean.setShipPrice(shipPrice);
            priceBeans.add(shipPriceBean);
        }
        log.info("导出运费:{}", JSONUtil.toJsonString(priceBeans));
        return priceBeans;
    }

    @Override
    public TemplateBean getMpuTemplate(ShipMpuParam bean) {
        TemplateBean templateBean = new TemplateBean();
        List<FreeShippingRegionsX> freeShippingRegionsS = new ArrayList<>();
        List<ShippingRegionsX> shippingRegionsS = new ArrayList<>();
        FreeShippingTemplateX templateX = null;
        if(bean.getMerchantId() != null){
            templateX = freeshipTemplateDao.findTemplateBymerchantId(bean.getMerchantId());
        }
        if(templateX == null){
            templateX = freeshipTemplateDao.fingDefaltShipTemplate();
        }
        if(templateX != null){
            FreeShippingRegionsX regions = freeShipRegionsDao.findByProvinceId(bean.getProvinceId(), templateX.getId());
            if(regions == null){
                regions = freeShipRegionsDao.findDefaltShipRegions( templateX.getId());
            }
            freeShippingRegionsS.add(regions);
            templateX.setRegions(freeShippingRegionsS);
        }else{
            return templateBean;
        }

        ShippingMpu shipByMpu = shipMpuDao.findByMpu(bean.getMpu());
        ShippingTemplateX shipTemplate = null;
        if(shipByMpu != null){
            shipTemplate = shipTemplateDao.findShipTemplateById(shipByMpu.getTemplateId());
        }
        if(shipTemplate == null){
            shipTemplate = shipTemplateDao.selectDefaultTemplate();
        }
        if(shipTemplate != null){
            ShippingRegionsX regionsX = shipRegionsDao.findByProvinceId(bean.getProvinceId(), shipTemplate.getId());
            if(regionsX == null){
                regionsX = shipRegionsDao.findDefaltShipRegions(shipTemplate.getId());
            }
            shippingRegionsS.add(regionsX);
            shipTemplate.setRegions(shippingRegionsS);
        }
        templateBean.setFreeShippingTemplate(templateX);
        templateBean.setShippingTemplate(shipTemplate);
        return templateBean;
    }

    @Override
    public CarriagePriceBean getMpuCarriage(CarriageBean bean) {
        List<ShipPriceBean> shipPriceBeans = new ArrayList<>();
        CarriagePriceBean carriagePriceBean = new CarriagePriceBean();
        FreeShippingTemplateX template = freeshipTemplateDao.fingDefaltShipTemplate();
        int status = 0;
        if(template != null) {
            FreeShippingRegionsX freeRegions = freeShipRegionsDao.findByProvinceId(bean.getProvinceId(), template.getId());
            if (freeRegions == null) {
                freeRegions = freeShipRegionsDao.findDefaltShipRegions(template.getId());
                if(freeRegions != null){
                    if(template.getMode() == 0){
                        if(freeRegions.getFullAmount() <= bean.getTotalPrice()){
                            status = 1;
                        }
                    }else if(template.getMode() == 1){

                        int mpuNum = 0;
                        List<ShipMerchantInfo> merchantInfos = bean.getMerchantInfos();
                        for (int i = 0; i < merchantInfos.size(); i++){
                            List<MpuParam> mpuParams = merchantInfos.get(i).getMpuParams();
                            for (int j = 0; j < mpuParams.size(); j++){
                                mpuNum += mpuParams.get(j).getNum();
                            }
                        }
                        if(freeRegions.getFullAmount() <= mpuNum){
                            status = 1;
                        }
                    }
                }
            }
        }
        if(status == 0){
            int templateId = 0;
            float totalPrice = 0;
            float maxShipPrice = 0;
            int maxMerchantId = 0;
//            List<MpuParam> mpuParams = bean.getMpuParams();
            List<ShipMerchantInfo> merchantInfos = bean.getMerchantInfos();
            for (int i = 0; i < merchantInfos.size(); i++){
                ShipPriceBean shipPriceBean = new ShipPriceBean();
                ShipMerchantInfo merchantInfo = merchantInfos.get(i);
                shipPriceBean.setMerchantCode(merchantInfo.getMerchantCode());
                shipPriceBean.setMerchantId(merchantInfo.getMerchantId());
                List<MpuParam> mpuParams = merchantInfo.getMpuParams();
                for (int j = 0; j < mpuParams.size(); j++){
                    float shipPrice = 0;
                    ShippingMpu shipByMpu = shipMpuDao.findByMpu(mpuParams.get(j).getMpu());
                    if(shipByMpu != null){
                        ShippingTemplateX shipTemplate = shipTemplateDao.findShipTemplateById(shipByMpu.getTemplateId());
                        if(shipTemplate != null){
                            templateId = shipTemplate.getId();
                        }
                    }else{
                        ShippingTemplateX shippingTemplateX = shipTemplateDao.selectDefaultTemplate();
                        if(shippingTemplateX != null){
                            templateId = shippingTemplateX.getId();
                        }
                    }
                    ShippingRegionsX shippingRegions = shipRegionsDao.findByProvinceId(bean.getProvinceId(), templateId);
                    if(shippingRegions == null){
                        shippingRegions = shipRegionsDao.findDefaltShipRegions(templateId);
                    }
                    if(shippingRegions != null){
                        if(shippingRegions.getBaseAmount() < mpuParams.get(j).getNum()){
                            shipPrice = (mpuParams.get(j).getNum() - shippingRegions.getBaseAmount())/shippingRegions.getCumulativeUnit() * shippingRegions.getCumulativePrice()
                                    + shippingRegions.getBasePrice();
                        }else{
                            shipPrice = shippingRegions.getBasePrice();
                        }
                    }
                    if (shipPrice > maxShipPrice) {
                        maxShipPrice = shipPrice;
                        maxMerchantId = merchantInfo.getMerchantId();
                    }
                }
                shipPriceBean.setShipPrice(maxShipPrice);
                shipPriceBeans.add(shipPriceBean);
            }
            for(ShipPriceBean shipPriceBean : shipPriceBeans) {
                if (shipPriceBean.getMerchantId() != maxMerchantId) {
                    shipPriceBean.setShipPrice((float)0);
                }
            }
            totalPrice = maxShipPrice;
            carriagePriceBean.setTotalPrice(totalPrice);
            carriagePriceBean.setPriceBeans(shipPriceBeans);
        }else{
            carriagePriceBean.setTotalPrice((float) 0);
        }
        return carriagePriceBean;
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
