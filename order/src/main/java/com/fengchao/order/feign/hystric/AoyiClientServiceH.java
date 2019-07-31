package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OrderParamBean;
import com.fengchao.order.feign.AoyiClientService;
import org.springframework.stereotype.Component;

@Component
public class AoyiClientServiceH implements AoyiClientService {
    @Override
    public OperaResponse order(OrderParamBean orderParamBean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(orderParamBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("添加订单服务失败 " + msg);
        return result;
    }


}
