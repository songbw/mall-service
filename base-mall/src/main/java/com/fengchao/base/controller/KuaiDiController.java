package com.fengchao.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.bean.OperaResult;
import com.fengchao.base.service.KuaiDiService;
import com.fengchao.base.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kuaidi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KuaiDiController {

    @Autowired
    private KuaiDiService service;

    @GetMapping
    private OperaResponse<JSONObject> kuaidi100(String num, String code) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(num)) {
            response.setCode(5000001);
            response.setMsg("num is null");
            return response;
        }
        if (StringUtils.isEmpty(code)) {
            response.setCode(5000001);
            response.setMsg("code is null");
            return response;
        }
        return service.kuaidi100(num, code);
    }

}
