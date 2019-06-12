package com.fengchao.order.service.impl;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ReceiverQueryBean;
import com.fengchao.order.mapper.AoyiBaseFulladdressMapper;
import com.fengchao.order.mapper.ReceiverMapper;
import com.fengchao.order.model.AoyiBaseFulladdress;
import com.fengchao.order.model.Receiver;
import com.fengchao.order.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReceiverServiceImpl implements ReceiverService {

    @Autowired
    private ReceiverMapper mapper;
    @Autowired
    private AoyiBaseFulladdressMapper addressMapper;

    @Override
    public Integer add(Receiver bean) {
        Date date = new Date() ;
        bean.setUpdatedAt(date);
        if (bean.getStatus() == 1) {
            setStatusZore(bean.getOpenId());
        }
        bean.setCreatedAt(date);
        mapper.insert(bean) ;
        return bean.getId() ;
    }



    @Override
    public Integer delete(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer modify(Receiver bean) {
        bean.setUpdatedAt(new Date());
        if (bean.getStatus() == 1) {
            setStatusZore(bean.getOpenId());
        }
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public PageBean findList(ReceiverQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("openId", queryBean.getOpenId()) ;
        List<Receiver> receivers = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            receivers = mapper.selectLimit(map);
            receivers.forEach(receiver -> {
                receiver = handleBean(receiver);
            });
        }
        pageBean = PageBean.build(pageBean, receivers, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public Receiver find(Integer id) {
        Receiver receiver = mapper.selectByPrimaryKey(id) ;
        receiver = handleBean(receiver);
        return receiver ;
    }

    private Receiver handleBean(Receiver receiver) {
        AoyiBaseFulladdress data = new AoyiBaseFulladdress();
        data.setId(receiver.getProvinceId());
        data.setLevel("1");
        AoyiBaseFulladdress province = addressMapper.selectById(data);
        if (province != null) {
            receiver.setProvinceName(province.getName());
        }
        data.setId(receiver.getCityId());
        data.setLevel("2");
        AoyiBaseFulladdress city = addressMapper.selectById(data);
        if (city != null) {
            receiver.setCityName(city.getName());
        }
        data.setId(receiver.getCountyId());
        data.setLevel("3");
        data.setPid(receiver.getCityId());
        AoyiBaseFulladdress county = addressMapper.selectById(data);
        if (county != null) {
            receiver.setCountyName(county.getName());
        }
        return receiver;
    }

    @Override
    public Integer setDefault(Receiver bean) {
        bean = mapper.selectByPrimaryKey(bean.getId());
        bean.setUpdatedAt(new Date());
        bean.setStatus(0);
        mapper.updateStatusByOpenId(bean);
        bean.setStatus(1);
        return mapper.updateStatusById(bean);
    }

    private void setStatusZore(String openId) {
        Receiver temp = new Receiver();
        temp.setOpenId(openId);
        temp.setStatus(0);
        mapper.updateStatusByOpenId(temp);
    }
}
