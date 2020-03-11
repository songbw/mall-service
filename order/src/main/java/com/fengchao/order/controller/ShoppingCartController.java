package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.ShoppingCartService;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    @PostMapping("/all")
    private OperaResult find(@RequestBody ShoppingCartQueryBean queryBean, OperaResult result, @RequestHeader("appId") String appId) {
        log.info("获取购物车列表 入参 appId:{} queryBean:{}", appId, JSONUtil.toJsonString(queryBean));
        queryBean.setAppId(appId);
        result.getData().put("result", service.findList(queryBean)) ;

        log.info("获取购物车列表 返回: {}", JSONUtil.toJsonString(result));
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody ShoppingCart bean, @RequestHeader("appId") String appId) {
        OperaResult result = new OperaResult() ;
        if (StringUtils.isEmpty(appId)) {
            result.setCode(4000002);
            result.setMsg("appId 不能为空");
            return result ;
        }
        bean.setAppId(appId);
        return service.add(bean);
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping("/num")
    private OperaResult modifyNum(@RequestBody ShoppingCart bean, @RequestHeader("appId") String appId) {
        bean.setAppId(appId);
        return service.modifyNum(bean);
    }

    @GetMapping("/count")
    private OperaResponse count(String openId) {
        return service.count(openId) ;
    }

}
