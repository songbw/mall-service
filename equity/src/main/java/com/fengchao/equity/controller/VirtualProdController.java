package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.VirtualTicketsBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.VirtualTicketsX;
import com.fengchao.equity.service.VirtualTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/virtual", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class VirtualProdController {

    @Autowired
    private VirtualTicketsService ticketsService;

    @PostMapping("create")
    public OperaResult createVirtualTicket(@RequestBody VirtualTicketsBean bean, OperaResult result){
        result.getData().put("id",ticketsService.createVirtualticket(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findVirtualTicket(String openId, Integer pageNo,Integer pageSize, OperaResult result){
        PageableData<VirtualTicketsX> virtualTicket = ticketsService.findVirtualTicket(openId, pageNo, pageSize);
        result.getData().put("result", virtualTicket);
        return result;
    }

    @PostMapping("consume")
    public OperaResult consume(@RequestBody VirtualTicketsBean bean, OperaResult result){
        int num = ticketsService.consumeTicket(bean);
        result.getData().put("result", num);
        return result;
    }

    @PostMapping("cancel")
    public OperaResult cancel(@RequestBody VirtualTicketsBean bean, OperaResult result){
        int num = ticketsService.cancelTicket(bean);
        result.getData().put("result", num);
        return result;
    }
}
