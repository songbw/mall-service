package com.fengchao.order.controller;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.service.OldOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songbw
 * @date 2019/10/30 18:30
 */
@RestController
@RequestMapping(value = "/old/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class OldOrderController {

    @Autowired
    private OldOrderService service ;

    @PostMapping("/all")
    private OperaResponse findList(@RequestBody OldOrderQueryBean queryBean) {
        return service.findList(queryBean);
    }

}
