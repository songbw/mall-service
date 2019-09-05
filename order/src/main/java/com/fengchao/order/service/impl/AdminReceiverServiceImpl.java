package com.fengchao.order.service.impl;

import com.fengchao.order.mapper.AoyiBaseFulladdressMapper;
import com.fengchao.order.mapper.ReceiverMapper;
import com.fengchao.order.model.AoyiBaseFulladdress;
import com.fengchao.order.model.Receiver;
import com.fengchao.order.service.AdminReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminReceiverServiceImpl implements AdminReceiverService {

    @Autowired
    private ReceiverMapper mapper;
    @Autowired
    private AoyiBaseFulladdressMapper addressMapper;

    @Override
    public List<Receiver> findByOpenId(String openId) {
        List<Receiver> receivers  = new ArrayList<>() ;
        mapper.selectByOpenId(openId).forEach(receiver -> {
            receivers.add(handleBean(receiver)) ;
        });
        return receivers;
    }

    @Override
    public Receiver find(Integer id) {
        Receiver receiver = mapper.selectByPrimaryKey(id);
        return handleBean(receiver);
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
}
