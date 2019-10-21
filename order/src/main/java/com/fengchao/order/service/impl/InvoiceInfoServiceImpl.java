package com.fengchao.order.service.impl;

import com.fengchao.order.mapper.InvoiceInfoMapper;
import com.fengchao.order.model.InvoiceInfo;
import com.fengchao.order.model.InvoiceInfoExample;
import com.fengchao.order.model.OrdersExample;
import com.fengchao.order.service.InvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

    @Autowired
    private InvoiceInfoMapper mapper;

    @Override
    public Integer add(InvoiceInfo bean) {
        Date date = new Date() ;
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        mapper.insert(bean) ;
        return bean.getId() ;
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer modify(InvoiceInfo bean) {
        bean.setUpdatedAt(new Date());
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public InvoiceInfo find(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public InvoiceInfo findByUserId(String userId) {
        InvoiceInfoExample example = new InvoiceInfoExample();
        InvoiceInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<InvoiceInfo> invoiceInfos = mapper.selectByExample(example) ;
        if (invoiceInfos != null && invoiceInfos.size() > 0) {
            return invoiceInfos.get(0) ;
        }
        return null;
    }
}
