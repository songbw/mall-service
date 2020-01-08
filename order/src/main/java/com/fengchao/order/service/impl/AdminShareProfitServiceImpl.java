package com.fengchao.order.service.impl;

import com.fengchao.order.bean.vo.ExportShareProfitVo;
import com.fengchao.order.service.AdminShareProfitService;
import com.fengchao.order.service.SettlementAssistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AdminShareProfitServiceImpl implements AdminShareProfitService {

    private SettlementAssistService settlementAssistService;

    @Override
    public ExportShareProfitVo shareProfit(Date startTime, Date endTime, String appId) {
        // settlementAssistService.queryIncomeUserOrderBoList()

        return null;
    }
}
