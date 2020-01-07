package com.fengchao.order.controller;

import com.fengchao.order.service.SettlementAssistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping(value = "/adminorder")
@Slf4j
public class ShareProfitController {

    private SettlementAssistService settlementAssistService;

    @Autowired
    public ShareProfitController(SettlementAssistService settlementAssistService) {
        this.settlementAssistService = settlementAssistService;
    }

    @GetMapping(value = "/export/shareprofit")
    public void shareprofit(HttpServletResponse response) throws Exception {
        settlementAssistService.queryIncomeUserOrderBoList(new Date(), new Date(), null);

        log.info("=======================");
    }
}
