package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.VirtualTicketsBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.service.VirtualProdService;
import com.fengchao.equity.service.VirtualTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminVirtual", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminVirtualProdController {

    @Autowired
    private VirtualProdService prodService;
    @Autowired
    private VirtualTicketsService ticketsService;

    @PostMapping("create")
    public OperaResult createVirtualProd(@RequestBody VirtualProd bean, OperaResult result){
        result.getData().put("id",prodService.createVirtualProd(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findVirtualProd(Integer pageNo, Integer pageSize, OperaResult result){
        PageableData<VirtualProd> virtualProd = prodService.findVirtualProd(pageNo, pageSize);
        result.getData().put("result", virtualProd);
        return result;
    }


    @GetMapping("findById")
    public OperaResult findByVirtualProdId(Integer id, OperaResult result){
        result.getData().put("result",prodService.findByVirtualProdId(id));
        return result;
    }

    @GetMapping("findByMpu")
    public OperaResult findByVirtualProdMpu(String mpu, OperaResult result){
        result.getData().put("result",prodService.findByVirtualProdMpu(mpu));
        return result;
    }

    @GetMapping("findByCode")
    public OperaResult findByVirtualProdcode(@RequestParam("code") String code, OperaResult result){
        result.getData().put("result",ticketsService.findByVirtualProdcode(code));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateVirtualProd(@RequestBody VirtualProd bean, OperaResult result){
        int num = prodService.updateVirtualProd(bean);
        result.getData().put("result",num);
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteVirtualProd(Integer id, OperaResult result){
        result.getData().put("result",prodService.deleteVirtualProd(id));
        return result;
    }

    @PostMapping("consumeCode")
    public OperaResult consume(@RequestBody VirtualTicketsBean bean, OperaResult result){
        int num = ticketsService.consumeTicket(bean);
        result.getData().put("result",num);
        return result;
    }

    @PostMapping("cancelCode")
    public OperaResult cancel(@RequestBody VirtualTicketsBean bean, OperaResult result){
        int num = ticketsService.cancelTicket(bean);
        result.getData().put("result",num);
        return result;
    }
}
