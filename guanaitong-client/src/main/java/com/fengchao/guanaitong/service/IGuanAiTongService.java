package com.fengchao.guanaitong.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface IGuanAiTongService {
    String getAccessToken();

    String buildXFormBody(Map map);

    String buildUrlXFormBody(Map map);

    JSONObject guanAiTongPost(String path, Map map);
}
