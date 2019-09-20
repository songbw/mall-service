package com.fengchao.pingan.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.CommonResult;
import com.fengchao.pingan.bean.PaymentBean;
import com.fengchao.pingan.bean.PrePayResultDTO;
import com.fengchao.pingan.feign.WSPayClientService;
import org.springframework.stereotype.Component;

@Component
public class WSPayClientServiceH implements WSPayClientService {
    @Override
    public CommonResult<PrePayResultDTO> payment(PaymentBean paymentBean) {
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
}
