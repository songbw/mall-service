package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.mapper.OverviewMapper;
import com.fengchao.statistics.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverviewServiceImpl implements OverviewService {

    @Autowired
    private OverviewMapper mapper;
    @Autowired
    private OrderService orderService;

    @Override
    public void add(QueryBean queryBean) {

    }


}
