package com.fengchao.guanaitong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.guanaitong.bean.JssdkSignBean;
import com.fengchao.guanaitong.bean.WXIds;
import com.fengchao.guanaitong.config.GuanAiTongConfig;
import com.fengchao.guanaitong.config.WeChatConfiguration;
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
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class JSSDKServiceImpl implements IJSSDKService {

    private RedisDAO redisDAO;
    private WeChatConfiguration weChatConfiguration;

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
    public JSSDKServiceImpl(WeChatConfiguration weChatConfiguration,RedisDAO redisDAO) {

        this.redisDAO = redisDAO;
        this.weChatConfiguration = weChatConfiguration;
    }

    @Override
    public String getAccessToken(String iAppId) throws Exception{
        String _func = "getAccessToken";
        String cacheToken = redisDAO.getValue(WeChatJSSDK.JS_SDK_TOKEN_KEY);
        if (null != cacheToken && !cacheToken.isEmpty()) {
            log.info("get cached JSSDK token: {" + cacheToken + "} ttl=" + redisDAO.ttl(WeChatJSSDK.JS_SDK_TOKEN_KEY) + "s");
            return cacheToken;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject json;

        WXIds ids = getAppIdConfig(iAppId);
        if (null == ids){
            return null;
        }
        String urlPrefix = ids.getUrlPrefix();
        String appId = ids.getAppId();
        String appSecret = ids.getAppSecret();
        if (null == urlPrefix || null == appId || null == appSecret){
            return null;
        }

        HttpUrl.Builder urlBuilder =HttpUrl.parse(urlPrefix + WeChatJSSDK.JS_SDK_TOKEN_PATH)
                .newBuilder()
                .addQueryParameter("grant_type","client_credential")
                .addQueryParameter("appid", appId)
                .addQueryParameter("secret",appSecret);

        Request request = new Request.Builder().get()
                .url(urlBuilder.build())
                .build();

        log.info("{} request url={}",_func, request.url().toString());
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
            log.info("access_token: {}", token);
            log.info("expires_in: {}" , expires.toString());
            redisDAO.setValue(WeChatJSSDK.JS_SDK_TOKEN_KEY, token, expires - 5);
            return token;
        } else {
            log.warn("data in response from Wechat-JSSDK is null");
        }

        return null;

    }

    @Override
    public JssdkSignBean getJsApiSign(String url,String iAppId) throws Exception {
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();
        String nonceStr = /*"Wm3WZYTPz0wzccnW";*/getRandom();
        StringBuilder sb = new StringBuilder();

        /*
        noncestr=Wm3WZYTPz0wzccnW
jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
timestamp=1414587457
url=http://mp.weixin.qq.com?params=value
        * */

        sb.append("jsapi_ticket=");
        sb.append(getApiTicket(iAppId)/*"sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg"*/);
        sb.append("&noncestr=");
        sb.append(nonceStr);
        sb.append("&timestamp=");
        sb.append(timeStamp/*"1414587457"*/);
        sb.append("&url=");
        sb.append(url.trim()/*"http://mp.weixin.qq.com?params=value"*/);

        String param = sb.toString();

        byte[] bytes = param.getBytes();
        String resultSign = DigestUtils.sha1Hex(bytes);

        String appId = getAppId(iAppId);
        if (null == appId){
            return null;
        }
        log.info("getJsApiSign url, param : {}, appId={}",param,appId);
        JssdkSignBean result = new JssdkSignBean();
        result.setSignature(resultSign);
        result.setAppId(appId);
        result.setTimestamp(timeStamp);
        result.setNonceStr(nonceStr);

        return result;
    }

    @Override
    public String getApiTicket(String iAppId) throws Exception {
        String _func = "getApiTicket";
        String cacheTicket = redisDAO.getValue(WeChatJSSDK.JS_SDK_TICKET_KEY);
        if (null != cacheTicket && !cacheTicket.isEmpty()) {
            log.info("get cached JSSDK ticket: {" + cacheTicket + "} ttl=" + redisDAO.ttl(WeChatJSSDK.JS_SDK_TICKET_KEY) + "s");
            return cacheTicket;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject json;

        String urlPrefix=getUrlPrefix(iAppId);
        if (null == urlPrefix){
            return null;
        }

        HttpUrl.Builder urlBuilder =HttpUrl.parse(urlPrefix + WeChatJSSDK.JS_SDK_GET_TICKET_PATH)
                .newBuilder()
                .addQueryParameter("access_token",getAccessToken(iAppId))
                .addQueryParameter("type", "jsapi");

        Request request = new Request.Builder().get()
                .url(urlBuilder.build())
                .build();

        log.info("{} request url={}",_func, request.url().toString());
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


    private String getAppId(String iAppId){
        WXIds ids = getAppIdConfig(iAppId);
        if (null == ids){
            return null;
        }else{
            return ids.getAppId();
        }
    }

    private String getUrlPrefix(String iAppId){
        WXIds ids = getAppIdConfig(iAppId);
        if (null == ids){
            return null;
        }else{
            return ids.getUrlPrefix();
        }
    }

    private WXIds getAppIdConfig(String iAppId){

        List<WXIds> idsList = weChatConfiguration.getIds();

        if (null == idsList || 0 == idsList.size()){
            log.error("没有找到微信appId配置项");
            return null;
        }

        for(WXIds ids:idsList){
            //log.info("微信appId配置 {}",JSON.toJSONString(ids));
            if (iAppId.equals(ids.getIAppId())){
                return ids;
            }
        }
        log.error("没有找到 iAppId= {} 的微信appId配置项",iAppId);
        return null;
    }
}
