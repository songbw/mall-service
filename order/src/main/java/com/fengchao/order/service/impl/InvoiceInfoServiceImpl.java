package com.fengchao.order.service.impl;

import com.fengchao.order.bean.InvoiceInfoQueryBean;
import com.fengchao.order.bean.PageBean;
import com.fengchao.order.mapper.InvoiceInfoMapper;
import com.fengchao.order.model.InvoiceInfo;
import com.fengchao.order.service.InvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        return mapper.insert(bean) ;
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
    public PageBean findList(InvoiceInfoQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("openId", queryBean.getOpenId()) ;
        List<InvoiceInfo> invoiceInfos = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            invoiceInfos = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, invoiceInfos, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public InvoiceInfo find(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
}
