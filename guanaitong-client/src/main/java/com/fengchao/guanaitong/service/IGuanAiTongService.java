package com.fengchao.guanaitong.service;

import java.util.Map;

public interface IGuanAiTongService {
    String getAccessToken();

    String buildXFormBody(Map map);

    void test();
}
