package com.fengchao.product.aoyi.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.model.AyFcImages;
import com.fengchao.product.aoyi.rpc.extmodel.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BaseServiceH implements BaseService {

    @Override
    public OperaResult downUpload(AyFcImages images) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(images);
        } catch (JsonProcessingException e) {
            log.error("下载图片异常:{}", e.getMessage(), e);
        }
        result.setCode(404);
        result.setMsg("base服务失败 " + msg);
        return result;
    }

    @Override
    public OperaResponse sendMail(Email email) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
