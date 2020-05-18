package com.fengchao.guanaitong.service;

import com.fengchao.guanaitong.bean.JssdkSignBean;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;

public interface IJSSDKService {
    String getAccessToken(String iAppId) throws Exception;

    String getApiTicket(String iAppId) throws Exception;

    JssdkSignBean getJsApiSign(String url,String iAppId) throws Exception;
}
