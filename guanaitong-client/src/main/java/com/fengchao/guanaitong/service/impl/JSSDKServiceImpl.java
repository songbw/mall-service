package com.fengchao.guanaitong.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.guanaitong.service.IJSSDKService;
import com.fengchao.guanaitong.util.RedisDAO;
//import com.fengchao.guanaitong.util.RedisUtil;
import com.fengchao.guanaitong.util.WeChatJSSDK;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Slf4j
@Service
public class JSSDKServiceImpl implements IJSSDKService {

    private RedisDAO redisDAO;

    private String getRandom() {
        Random random = new Random();
        String fourRandom = random.nextInt(1000000) + "";
        int randLength = fourRandom.length();
        StringBuilder sb = new StringBuilder();
        if (randLength < 6) {
            for (int i = 1; i <= 6 - randLength; i++)
                sb.append("0");
        }
        sb.append(fourRandom);
        return sb.toString();
    }

    @Autowired
    public JSSDKServiceImpl(RedisDAO redisDAO) {
        this.redisDAO = redisDAO;
    }

    @Override
    public String getAccessToken() throws Exception{

        String cacheToken = redisDAO.getValue(WeChatJSSDK.JS_SDK_TOKEN_KEY);
        if (null != cacheToken && !cacheToken.isEmpty()) {
            log.info("get cached JSSDK token: {" + cacheToken + "} ttl=" + redisDAO.ttl(WeChatJSSDK.JS_SDK_TOKEN_KEY) + "s");
            return cacheToken;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject json;

        HttpUrl.Builder urlBuilder =HttpUrl.parse(WeChatJSSDK.JS_SDK_URL_PREFIX + WeChatJSSDK.JS_SDK_TOKEN_PATH)
                .newBuilder()
                .addQueryParameter("grant_type","client_credential")
                .addQueryParameter("appid", WeChatJSSDK.JS_SDK_APPID)
                .addQueryParameter("secret",WeChatJSSDK.JS_SDK_APP_SECRET);

        Request request = new Request.Builder().get()
                .url(urlBuilder.build())
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception ex) {
            log.warn("get token from WeChat: got exception: " + ex.getMessage());
            throw new Exception(ex);
        }

        if (null == response || null == response.body()) {
            log.warn("try get token from WeChat JsApi: no response");
            return null;
        }

        String responseString = response.body().string();
        log.info("get token from WeChat : " + responseString);

        json = JSONObject.parseObject(responseString);

        if (null != json) {
            String token = json.getString("access_token");
            if (null == token) {
                log.warn("failed to get access_token from WeChat");
                    return null;
            }
            Integer expires = json.getInteger("expires_in");
            log.info("access_token:" + token);
            log.info("expires_in:" + expires.toString());
            redisDAO.setValue(WeChatJSSDK.JS_SDK_TOKEN_KEY, token, expires - 5);
            return token;
        } else {
            log.warn("data in response from guanaitong is null");
        }

        return null;

    }

    @Override
    public String getJsApiSign(String url) throws Exception {
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();

        StringBuilder sb = new StringBuilder();

        sb.append("jsapi_ticket=");
        sb.append(getApiTicket());
        sb.append("&noncestr=");
        sb.append(getRandom());
        sb.append("&timestamp=");
        sb.append(timeStamp);
        sb.append("&url=");
        sb.append(url.trim());

        String param = sb.toString();
        log.info("getJsApiSign url, param : {}",param);
        byte[] bytes = param.getBytes();
        return DigestUtils.sha1Hex(bytes);
    }

    @Override
    public String getApiTicket() throws Exception {
        String cacheTicket = redisDAO.getValue(WeChatJSSDK.JS_SDK_TICKET_KEY);
        if (null != cacheTicket && !cacheTicket.isEmpty()) {
            log.info("get cached JSSDK ticket: {" + cacheTicket + "} ttl=" + redisDAO.ttl(WeChatJSSDK.JS_SDK_TICKET_KEY) + "s");
            return cacheTicket;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject json;

        HttpUrl.Builder urlBuilder =HttpUrl.parse(WeChatJSSDK.JS_SDK_URL_PREFIX + WeChatJSSDK.JS_SDK_GET_TICKET_PATH)
                .newBuilder()
                .addQueryParameter("access_token",getAccessToken())
                .addQueryParameter("type", "wx_card");


        Request request = new Request.Builder().get()
                .url(urlBuilder.build())
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception ex) {
            log.warn("get ticket from WeChat: got exception: " + ex.getMessage());
            throw new Exception(ex);
        }

        if (null == response || null == response.body()) {
            log.warn("try get ticket from WeChat JsApi: no response");
            return null;
        }

        String responseString = response.body().string();
        log.info("get token from WeChat : " + responseString);

        json = JSONObject.parseObject(responseString);

        if (null != json) {
            Integer code = json.getInteger("errcode");
            if (0 != code) {
                log.warn("failed to get ticket from WeChat");
                return null;
            }

            String ticket = json.getString("ticket");
            if (null == ticket) {
                log.warn("failed to get ticket from WeChat");
                return null;
            }
            Integer expires = json.getInteger("expires_in");
            log.info("api ticket:" + ticket);
            log.info("expires_in:" + expires.toString());
            redisDAO.setValue(WeChatJSSDK.JS_SDK_TICKET_KEY, ticket, expires - 5);
            return ticket;
        } else {
            log.warn("data in response from WeChat JSSDK is null");
        }

        return null;
    }
}
