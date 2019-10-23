package com.fengchao.guanaitong.service;

import com.fengchao.guanaitong.bean.JssdkSignBean;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;

public interface IJSSDKService {
    String getAccessToken() throws Exception;

    String getApiTicket() throws Exception;

    JssdkSignBean getJsApiSign(String url) throws Exception;
}
