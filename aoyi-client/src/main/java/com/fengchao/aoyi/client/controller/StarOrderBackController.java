package com.fengchao.aoyi.client.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.aoyi.client.bean.StarBackBean;
import com.fengchao.aoyi.client.startService.OrderStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author song
 * @2020-02-09 12:18 下午
 **/
@RestController
@RequestMapping(value = "/star/order/back")
@Slf4j
public class StarOrderBackController {

    @Autowired
    private OrderStarService starService ;

    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private String gBack(StarBackBean bean) {
        log.info("怡亚通回调 入参:{}", JSON.toJSONString(bean));

        String result = starService.notify(bean);

        log.info("怡亚通回调 返回:{}", result);

        return "success";
    }

}
