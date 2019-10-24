package com.fengchao.freight.bean;

import com.fengchao.freight.model.FreeShippingTemplateX;
import com.fengchao.freight.model.ShippingTemplateX;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TemplateBean {
    private ShippingTemplateX shippingTemplate;
    private FreeShippingTemplateX freeShippingTemplate;
}
