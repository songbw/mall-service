package com.fengchao.aggregation.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.feign.ProdService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdServiceH implements ProdService {

    @Override
    public OperaResult findProductListByMpuIdList(List<String> mpuIdList, String appId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + mpuIdList);
        return result;
    }

    @Override
    public OperaResult findProductListByMpuIdListAdmin(List<String> mpuIdList, String appId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + mpuIdList);
        return result;
    }
}
