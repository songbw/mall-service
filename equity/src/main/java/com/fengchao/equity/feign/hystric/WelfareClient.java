package com.fengchao.equity.feign.hystric;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.feign.IWelfareClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class WelfareClient implements IWelfareClient {

    @Override
    public JSONObject
    getCardInfoCodes(@RequestParam(value = "corporationCode",required = false) String corporationCode){

        OperaResponse result = new OperaResponse(null);
        result.setCode(404);
        result.setMsg("福利服务调用获取提货券号列表失败 ");
        return JSON.parseObject(JSON.toJSONString(result));
    }

    @Override
    public JSONObject
    getCardInfoGroupCodes(@RequestParam(value = "corporationCode",required = false) String corporationCode){

        OperaResponse result = new OperaResponse(null);
        result.setCode(404);
        result.setMsg("福利服务调用获取分组提货券号列表失败 ");
        return JSON.parseObject(JSON.toJSONString(result));
    }
}
