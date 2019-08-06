package com.fengchao.guanaitong.service;

import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;

public interface IJSSDKService {
    String getAccessToken() throws Exception;

    String getApiTicket() throws Exception;

    String getJsApiSign(String url) throws Exception;
}
