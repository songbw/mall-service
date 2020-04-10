package com.fengchao.equity.controller;

import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.bean.OpenIdPhoneBean;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.service.CardInfoService;
import com.fengchao.equity.service.CardTicketService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/card", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CardController {

    @Autowired
    private CardInfoService service;
    @Autowired
    private CardTicketService ticketService;


    @PostMapping("openIdByPhone")
    public OperaResult
    putCardTicketOpenId(@RequestBody OpenIdPhoneBean bean,
                     @RequestHeader(value = "appId") String appId,
                     OperaResult result) throws Exception {
        log.info("设置礼品券openId: appId={} 入参:{}", appId,JSONUtil.toJsonString(bean));
        //if (null == appId){
        //    result.setCode(500);
        //    result.setMsg("应用缺失appId");
        //    return result;
        //}

        result.getData().put("result",ticketService.putOpenIdByPhone(bean.getOpenId(),bean.getPhone()));
        return result;
    }

    @PostMapping("binds")
    public OperaResult
    verifyCardTicket(@RequestBody CardTicketBean bean,
                     @RequestHeader(value = "appId") String appId,
                     OperaResult result) throws Exception {
        log.info("用户验证礼品券参数 appId={} 入参:{}", appId,JSONUtil.toJsonString(bean));
        if (null == appId){
            result.setCode(500);
            result.setMsg("应用缺失appId");
            return result;
        }
        bean.setAppId(appId);
        result.getData().put("result",ticketService.verifyCardTicket(bean));
        return result;
    }

    @PostMapping("exchange")
    public OperaResult
    exchangeCardTicket(@RequestBody CardTicketBean bean,
                       @RequestHeader(value = "appId") String appId,
                       OperaResult result) throws Exception {
        log.info("兑换礼品券参数 appId={} 入参:{}", appId,JSONUtil.toJsonString(bean));
        bean.setAppId(appId);
        result.getData().put("result",ticketService.exchangeCardTicket(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult getCardTicket(String openId, OperaResult result){
        result.getData().put("result",ticketService.getCardTicket(openId));
        return result;
    }

    @GetMapping("findByCard")
    public OperaResult getCardTicketByCard(String openId, String card, OperaResult result){
        result.getData().put("result",ticketService.getCardTicketByCard(openId, card));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteCardTicket(Integer id, OperaResult result){
        result.getData().put("result",ticketService.deleteCardTicket(id));
        return result;
    }
}
