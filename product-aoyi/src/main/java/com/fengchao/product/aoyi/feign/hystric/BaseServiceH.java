package com.fengchao.product.aoyi.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.model.AyFcImages;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseServiceH implements BaseService {

    @Override
    public OperaResult downUpload(AyFcImages images) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(images);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("base服务失败 " + msg);
        return result;
    }
}
