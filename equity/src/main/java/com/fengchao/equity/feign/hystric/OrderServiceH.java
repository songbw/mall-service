package com.fengchao.equity.feign.hystric;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.OrderService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class OrderServiceH implements OrderService {

    @Override
    public String getOrderList(/*Map<String, Object> map,*/ List<Integer> idArray) {

        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取订单信息失败 " +idArray.toString());
        return JSON.toJSONString(result);
    }
}

