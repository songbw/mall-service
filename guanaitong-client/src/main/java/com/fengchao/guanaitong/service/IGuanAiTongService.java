package com.fengchao.guanaitong.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface IGuanAiTongService {
    String getAccessToken() throws Exception;

    String buildXFormBody(Map map) throws Exception;

    String buildUrlXFormBody(Map map) throws Exception;

    JSONObject guanAiTongPost(String path, Map map) throws Exception;

    JSONObject guanAiTongXFormUrlEncodedPost(String path, Map map) throws Exception;
}
