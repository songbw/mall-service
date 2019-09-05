package com.fengchao.order.service.impl;

import com.fengchao.order.mapper.ReceiverMapper;
import com.fengchao.order.model.Receiver;
import com.fengchao.order.service.AdminReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReceiverServiceImpl implements AdminReceiverService {

    @Autowired
    private ReceiverMapper mapper;

    @Override
    public List<Receiver> findByOpenId(String openId) {
        return mapper.selectByOpenId(openId);
    }

    @Override
    public Receiver find(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
}
