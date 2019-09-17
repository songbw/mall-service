package com.fengchao.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.service.KuaiDiService;
import com.fengchao.base.utils.Kuaidi100;
import org.springframework.stereotype.Service;

@Service
public class KuaiDiServiceImpl implements KuaiDiService {


    @Override
    public OperaResponse<JSONObject> kuaidi100(String num, String code) {
        OperaResponse<JSONObject> response = new OperaResponse<>();
        String jsonString = Kuaidi100.synQueryData(num, code) ;
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        response.setData(jsonObject);
        return response;
    }
}
