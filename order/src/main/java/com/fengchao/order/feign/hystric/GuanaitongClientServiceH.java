package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.SendTradeInfoBean;
import com.fengchao.order.feign.GuanaitongClientService;

/**
 * @author songbw
 * @date 2019/12/24 15:21
 */
public class GuanaitongClientServiceH implements GuanaitongClientService {
    @Override
    public OperaResponse sendTradeInfo(SendTradeInfoBean bean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("权益优惠券服务失败 ");
        return result;
    }
}
