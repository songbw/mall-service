package com.fengchao.equity.feign;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.hystric.OrderServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "order", fallback = OrderServiceH.class)
public interface OrderService {

    @RequestMapping(value = "/order/batch/ids", method = RequestMethod.POST)
    String getOrderList(/*@RequestHeader Map<String, Object> headers,*/ @RequestBody List<Integer> idArray);

}

