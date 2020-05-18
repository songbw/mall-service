package com.fengchao.guanaitong.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface IGuanAiTongService {
    String getAccessToken(String iAppId) throws Exception;

    String buildXFormBody(Map map,String iAppId) throws Exception;

    String buildUrlXFormBody(Map map,String iAppId) throws Exception;

    JSONObject guanAiTongPost(String path, Map map,String iAppId) throws Exception;
}
