package com.fengchao.pingan.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.feign.WSPayClientService;
import org.springframework.stereotype.Component;

@Component
public class WSPayClientServiceH implements WSPayClientService {
    @Override
    public CommonResult<PrePayResultDTO> payment(PrePayDTO paymentBean) {
        CommonResult result = new CommonResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(paymentBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMessage("payment服务失败" + msg);
        return result;
    }

    @Override
    public CommonResult<String> aggPayBack(AggPayBackBean aggPayBackBean) {
        CommonResult result = new CommonResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(aggPayBackBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMessage("payment服务失败" + msg);
        return result;
    }

    @Override
    public CommonResult<String> aggRefundBack(AggPayBackBean aggPayBackBean) {
        CommonResult result = new CommonResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(aggPayBackBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMessage("wk payment服务失败" + msg);
        return result;
    }
}
