package com.fengchao.statistics.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.EquityService;
import org.springframework.stereotype.Component;

@Component
public class EquityServiceH implements EquityService {
    @Override
    public OperaResult findPromotion(int id) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        result.setCode(404);
        result.setMsg("获取活动信息失败 " + id);
        return result;
    }
}
