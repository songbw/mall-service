package com.fengchao.base.service;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.base.bean.OperaResponse;

/**
 * 快递查询服务
 */
public interface KuaiDiService {

    OperaResponse<JSONObject> kuaidi100(String num, String code) ;

}
