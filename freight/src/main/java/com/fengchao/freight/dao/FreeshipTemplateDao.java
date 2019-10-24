package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.FreeShippingTemplateMapper;
import com.fengchao.freight.mapper.FreeShippingTemplateXMapper;
import com.fengchao.freight.model.FreeShippingTemplate;
import com.fengchao.freight.model.FreeShippingTemplateExample;
import com.fengchao.freight.model.FreeShippingTemplateX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FreeshipTemplateDao {

    @Autowired
    private FreeShippingTemplateMapper mapper;
    @Autowired
    private FreeShippingTemplateXMapper xMapper;


    public int createFreeShipTemplate(FreeShippingTemplate template) {
        return mapper.insertSelective(template);
    }

    public int updateFreeShipTemplate(FreeShippingTemplate template) {
        return mapper.updateByPrimaryKeySelective(template);
    }

    public int deleteFreeShipTemplate(Integer id) {
        FreeShippingTemplate template = new FreeShippingTemplate();
        template.setId(id);
        template.setStatus(2);
        return mapper.updateByPrimaryKeySelective(template);
    }

    public PageInfo<FreeShippingTemplate> findFreeShipTemplate(Integer pageNo, Integer pageSize) {
        FreeShippingTemplateExample example = new FreeShippingTemplateExample();
        FreeShippingTemplateExample.Criteria criteria = example.createCriteria();

        criteria.andStatusEqualTo(1);
        PageHelper.startPage(pageNo, pageSize);
        List<FreeShippingTemplate> shippingTemplates = mapper.selectByExample(example);
        return new PageInfo<>(shippingTemplates);
    }

    public FreeShippingTemplateX findFreeShipTemplateById(Integer id) {
        FreeShippingTemplateX freeShippingTemplate = xMapper.selectByPrimaryKey(id);
        return freeShippingTemplate;
    }

    public List<FreeShippingTemplate> findFreeShipTemplateByMerchantId(Integer merchantId) {
        FreeShippingTemplateExample example = new FreeShippingTemplateExample();
        FreeShippingTemplateExample.Criteria criteria = example.createCriteria();

        criteria.andMerchantIdEqualTo(merchantId);
        criteria.andStatusEqualTo(1);
        List<FreeShippingTemplate> shippingTemplates = mapper.selectByExample(example);
        return shippingTemplates;
    }

    public FreeShippingTemplateX findTemplateBymerchantId(Integer merchantId) {
        return xMapper.selectByMerchantId(merchantId);
    }

    public FreeShippingTemplateX fingDefaltShipTemplate() {
        return xMapper.selectDefaltShipTemplate();
    }
}
