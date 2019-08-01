package com.fengchao.statistics.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.ProductServiceClient;
import com.fengchao.statistics.rpc.extmodel.CategoryQueryBean;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceClientH implements ProductServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResult findMerchant(int id) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        result.setCode(404);
        result.setMsg("获取商品商户失败 " + id);
        return result;
    }

    @Override
    public OperaResponse<List<CategoryQueryBean>> queryCategorysByCategoryIdList(List<Integer> categoryIdList) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}
