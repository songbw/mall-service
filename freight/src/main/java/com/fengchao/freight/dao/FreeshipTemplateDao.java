package com.fengchao.freight.dao;

import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.mapper.FreeShippingTemplateMapper;
import com.fengchao.freight.model.FreeShippingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreeshipTemplateDao {

    @Autowired
    private FreeShippingTemplateMapper mapper;


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
}
