package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;

/**
 * @Author tom
 * @Date 19-7-19 下午6:22
 */
public class HystrixDefaultFallback {

    public static OperaResult defaultFallback() {
        OperaResult result = new OperaResult();
        result.setCode(500);
        result.setMsg("rpc 查询失败降级");

        return result;
    }

    public static OperaResponse defaultReponseFallback() {
        OperaResponse result = new OperaResponse();
        result.setCode(500);
        result.setMsg("rpc 查询失败降级");
        return result;
    }
}
