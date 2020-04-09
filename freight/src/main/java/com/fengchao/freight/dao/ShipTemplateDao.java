package com.fengchao.freight.dao;

import com.fengchao.freight.mapper.ShippingTemplateMapper;
import com.fengchao.freight.mapper.ShippingTemplateXMapper;
import com.fengchao.freight.model.ShippingTemplate;
import com.fengchao.freight.model.ShippingTemplateExample;
import com.fengchao.freight.model.ShippingTemplateX;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipTemplateDao {

    @Autowired
    private ShippingTemplateMapper mapper;
    @Autowired
    private ShippingTemplateXMapper xMapper;

    public int createShipTemplate(ShippingTemplate template) {
        return mapper.insertSelective(template);
    }

    public int deleteShipTemplate(Integer id) {
        ShippingTemplate template = new ShippingTemplate();
        template.setId(id);
        template.setStatus(2);
        return mapper.updateByPrimaryKeySelective(template);
    }

    public int updateShipTemplate(ShippingTemplate template) {
        return mapper.updateByPrimaryKeySelective(template);
    }

    public ShippingTemplateX findShipTemplateById(Integer id) {
        ShippingTemplateX templateX = xMapper.selectByPrimaryKey(id);
        return templateX;
    }

    public List<ShippingTemplate>
    selectByIdList(List<Integer> idList){
        ShippingTemplateExample example = new ShippingTemplateExample();
        ShippingTemplateExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        criteria.andMerchantIdIn(idList);

        return mapper.selectByExample(example);
    }

    public PageInfo<ShippingTemplate> findShipTemplate(Integer pageNo, Integer pageSize, Integer merchantId) {
        ShippingTemplateExample example = new ShippingTemplateExample();
        ShippingTemplateExample.Criteria criteria = example.createCriteria();

        criteria.andStatusEqualTo(1);

        if (null == merchantId) {
            criteria.andMerchantIdGreaterThan(0);
        } else {
            criteria.andMerchantIdEqualTo(merchantId);
        }

        PageHelper.startPage(pageNo, pageSize);
        List<ShippingTemplate> shippingTemplates = mapper.selectByExample(example);
        return new PageInfo<>(shippingTemplates);
    }

    public ShippingTemplateX selectDefaultTemplate() {
        ShippingTemplateX shippingTemplateX = xMapper.selectDefaultTemplate();
        return shippingTemplateX;
    }

    public int updateTemplateDefault() {
        ShippingTemplate template = new ShippingTemplate();
        template.setIsDefault(false);

        ShippingTemplateExample example = new ShippingTemplateExample();
        ShippingTemplateExample.Criteria criteria = example.createCriteria();
        criteria.andIsDefaultEqualTo(true);
        criteria.andStatusEqualTo(1);

        return xMapper.updateByExampleSelective(template, example);
    }

    public List<ShippingTemplateX> findShipTemplateByMpu(Integer id) {
        List<ShippingTemplateX> shipTemplateByMpu = xMapper.findShipTemplateByMpu(id);
        return shipTemplateByMpu;
    }

    public List<ShippingTemplate>
    findShipTemplateByMerchantId(Integer merchantId) {
        ShippingTemplateExample example = new ShippingTemplateExample();
        ShippingTemplateExample.Criteria criteria = example.createCriteria();

        criteria.andStatusEqualTo(1);
        criteria.andMerchantIdEqualTo(merchantId);
        List<ShippingTemplate> shippingTemplates = mapper.selectByExample(example);
        return shippingTemplates;
    }
}
