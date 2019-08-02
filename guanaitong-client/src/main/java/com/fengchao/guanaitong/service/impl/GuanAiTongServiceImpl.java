package com.fengchao.guanaitong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.guanaitong.service.IGuanAiTongService;
import com.fengchao.guanaitong.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@SuppressWarnings("unchecked")
@Slf4j
@Service
public class GuanAiTongServiceImpl implements IGuanAiTongService {

    private final String MEDIA_TYPE="application/x-www-form-urlencoded; charset=utf-8";
    private final String URL_PREFIX = "https://openapi.guanaitong.cc/";//test
    //private final String URL_PREFIX = "https://openapi.guanaitong.com/"; //normal

    private static final String TOKEN_CREATE_PATH = "token/create";

    private static final String GRANT_TYPE_KEY = "grant_type";
    private final String GRANT_TYPE_VALUE = "client_credential";

    private final String APPID_KEY = "appid";
    private final String APPID_VALUE = "20110843";

    private final String APPSECRET_KEY = "appsecret";
    private final String APPSECRET_VALUE = "78dde3cc1e3cab6cbbabbc1bf88faa4e";

    //private final String VERSION_KEY = "version";
    //private final String VERSION_VALUE = "1.0.0";

    private final String TOKEN_KEY = "access_token";
    private final String TIME_STAMP_KEY = "timestamp";
    private final String SIGN_KEY = "sign";

    private static final int HTTP_STATUS_OK = 200;
    private static final int RESPONSE_DATA_OK = 0;


    //okHttp3添加信任所有证书
    private static OkHttpClient getUnsafeOkHttpClient() {

        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getTokenSign(String timeStamp){
        StringBuilder sb = new StringBuilder();
        sb.append(APPID_KEY);
        sb.append("=");
        sb.append(APPID_VALUE);
        sb.append("&");
        sb.append(APPSECRET_KEY);
        sb.append("=");
        sb.append(APPSECRET_VALUE);
        sb.append("&");
        sb.append(GRANT_TYPE_KEY);
        sb.append("=");
        sb.append(GRANT_TYPE_VALUE);
        sb.append("&");
        sb.append(TIME_STAMP_KEY);
        sb.append("=");
        sb.append(timeStamp);

        byte[] bytes = sb.toString().getBytes();
        return DigestUtils.sha1Hex(bytes);
    }

    private String map2string(Map map) {
        StringBuilder sb = new StringBuilder();

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        int i = 0;
        while (iter.hasNext()) {
            String key = iter.next();
            if (0 < i ) {
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
            } else {
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
            }
            i++;
        }

        return sb.toString();

    }

    private String getTokenRequestMap(){
        //appid=20110843&grant_type=client_credential&timestamp=1563329819&sign=a472d47c242afbb1d5c305a6abaae6f683705310

        Map<String, Object> map = new TreeMap<>();
        /*
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });
*/
        map.put(APPID_KEY,APPID_VALUE);
        map.put(GRANT_TYPE_KEY,GRANT_TYPE_VALUE);
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();
        map.put(TIME_STAMP_KEY,timeStamp);
        String sign = getTokenSign(timeStamp);
        map.put(SIGN_KEY,sign);

        return map2string(map);
    }

    private Map buildFormSignMap(Map m) throws Exception{

        Map<String, Object> tMap = new TreeMap<>();

        try {
            String token = getAccessToken();
            tMap.put(TOKEN_KEY, token);
            tMap.put(APPSECRET_KEY, APPSECRET_VALUE);
        } catch (Exception e) {
            log.info("getAccessToken got exception : {}",e.getMessage());
            throw new Exception(e);
        }


        //tMap.put(VERSION_KEY, VERSION_VALUE);

        Set<String> keySet = m.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            tMap.put(key,m.get(key).toString());
        }

        return tMap;
    }

    private String getFormSign(Map map) throws Exception{

        Map m;
        try {
            m = buildFormSignMap(map);
        } catch (Exception e) {
            throw new Exception(e);
        }

        String xFormString = map2string(m);
        log.info("getFormSign, parameters for sign : " + xFormString);
        byte[] bytes = xFormString.getBytes();
        return DigestUtils.sha1Hex(bytes);
    }

    private JSONObject tryPost(String path, String xForm) throws Exception {

        OkHttpClient client = getUnsafeOkHttpClient();//new OkHttpClient();
        JSONObject json;
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody body = RequestBody.create(mediaType, xForm);
        Request request = new Request.Builder()
                .url(URL_PREFIX + path)
                .post(body).build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException ex) {
            log.info("try post got exception : " + ex.getMessage());
            throw new Exception(ex);
        }

        if (null == response || null == response.body()) {
            log.warn("tryPost {} ,got null response data from GuanAiTong",path );
            return null;
        }

        String responseString = response.body().string();
        log.info("tryPost {} ,got response from GuanAiTong : {}",path,responseString);
        int code = response.code();
        if (HTTP_STATUS_OK != code) {
            log.info("tryPost got response code = " + String.valueOf(code));
            return null;
        }

        json = JSONObject.parseObject(responseString);
        return json;

/*
        if (null != json) {
            Integer resultCode = json.getInteger("code");
            System.out.println("get code = " + resultCode.toString());
            if (RESPONSE_DATA_OK != resultCode) {
                String msg = json.getString("message");
                if (null != msg) {
                    log.info("error: " + msg);
                }
                System.out.println("=== get message:"+msg);
                return null;
            }

            JSONObject data = json.getJSONObject("data");
            if (null != data) {
                return data;
            } else {
                System.out.println("====get data is null");
            }
        } else {
            System.out.println("===convert String to json failed");
        }

        return null;
        */
    }

/*
    @Override
    public void test() {

        //{"enterprise_open_id":"a19ddfe5af29d5d2ae7c5e181a07b6c2","open_id":"1018f89838e91fea5270d52d65f59dba"}
        Map<String, String> map = new TreeMap<>();

        //map.put("auth_code", "09DD513D996B49F55B0926E4C37DF41B756B62A6");

        map.put("open_id", "1018f89838e91fea5270d52d65f59dba");

        String xForm = buildXFormBody(map);
        JSONObject json = tryPost(GET_DETAIL_PATH, xForm);
        if (null != json) {
            System.out.println("received: " + json.toString());
        }

    }
*/

    @Override
    public String buildUrlXFormBody(Map map) throws Exception{

        log.info("buildUrlXFormBody map : {}", JSON.toJSONString(map));
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Object> item = it.next();
            if (null == item.getValue()) {
                it.remove();
                log.info("find null value in map: " + item.getKey());
            }
        }

        Map<String, Object> theMap = new HashMap<>();
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();
        map.put(TIME_STAMP_KEY,timeStamp);

        try {
            String sign = getFormSign(map);
            String token = getAccessToken();
            theMap.put(TOKEN_KEY, token);
            theMap.put(SIGN_KEY, sign);
        } catch (Exception e) {
            throw new Exception(e);
        }
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String urlValue = URLEncoder.encode(map.get(key).toString(), StandardCharsets.UTF_8.toString());
                theMap.put(key, urlValue);
            } catch (UnsupportedEncodingException ex) {
                log.info("urlEncode error: " + ex.getMessage());
                throw new Exception(ex);
            }
        }

        String xForm = map2string(theMap);
        log.info("buildXForm: {}" ,xForm);
        return xForm;

    }


    @Override
    public String buildXFormBody(Map map) throws Exception{

        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Object> item = it.next();
            if (null == item.getValue()) {
                it.remove();
                log.info("find null value in map: " + item.getKey());
            }
        }

        Map<String, Object> theMap = new HashMap<>();
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();
        map.put(TIME_STAMP_KEY,timeStamp);

        try {
            String sign = getFormSign(map);
            String token = getAccessToken();
            if (null == token) {
                log.info("failed to get access_token!! ");
                return null;
            }
            theMap.put(TOKEN_KEY, token);
            theMap.put(SIGN_KEY, sign);
        } catch (Exception e) {
            throw new Exception(e);
        }

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            theMap.put(key, map.get(key).toString());
        }

        String xForm = map2string(theMap);
        log.info("buildXForm: {}" ,xForm);
        return xForm;

    }

    @Override
    public String getAccessToken() throws Exception {

        String cacheToken = RedisUtil.getValue(TOKEN_KEY);
        if (null != cacheToken && !cacheToken.isEmpty()) {
            log.info("get cached token: {" + cacheToken + "} ttl=" + RedisUtil.ttl(TOKEN_KEY) + "s");
            return cacheToken;
        }

        OkHttpClient client = getUnsafeOkHttpClient();//new OkHttpClient();
        String xFormBody = getTokenRequestMap();
        JSONObject json;
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody body = RequestBody.create(mediaType, xFormBody);
        Request request = new Request.Builder()
                .url(URL_PREFIX + TOKEN_CREATE_PATH)
                .post(body).build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception ex) {
            log.warn("get token from GuanAiTong: got exception: " + ex.getMessage());
            throw new Exception(ex);
        }

        if (null == response || null == response.body()) {
            log.warn("try get token from guanAitong: no response");
            return null;
        }

        String responseString = response.body().string();
        log.info("get token from guanAiTong : " + responseString);
        int code = response.code();
        if (HTTP_STATUS_OK != code) {
            return null;
        }

        json = JSONObject.parseObject(responseString);

        if (null != json) {
            Integer resultCode = json.getInteger("code");
            if (RESPONSE_DATA_OK != resultCode) {
                log.info("get token response code = " + resultCode.toString());
                String msg = json.getString("message");
                if (null != msg) {
                    log.warn("get token from guanaitong, error: " + msg);
                }
                return null;
            }

            JSONObject data = json.getJSONObject("data");
            if (null != data) {
                String token = data.getString("access_token");
                if (null == token) {
                    log.warn("failed to get access_token from guanAiTong");
                    return null;
                }
                Integer expires = data.getInteger("expires_in");
                log.info("access_token:" + token);
                log.info("expires_in:" + expires.toString());
                RedisUtil.putRedis(TOKEN_KEY, token, expires - 5);
                return token;
            } else {
                log.warn("data in response from guanaitong is null");
            }
        } else {
            log.warn("data in response from guanaitong is null");
        }

        return null;
    }

    @Override
    public JSONObject guanAiTongPost(String path, Map map) throws Exception{
        if (null == path || path.isEmpty()) {
            log.info("path is null");
            return null;
        }

        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Object> item = it.next();
            if (null == item.getValue()) {
                it.remove();
                log.info("find null value in map" + item.getKey());
            }
        }

        String xForm = buildXFormBody(map);
        if (null == xForm || xForm.isEmpty()) {
            log.info("build param failed");
            return null;
        }

        try {
            JSONObject json = tryPost(path, xForm);
            if (null == json) {
                log.info("response data from remote is null");
            }
            return json;
        } catch (Exception e) {
            //log.warn("access GuanAiTong {} , got exception: {}" ,path, e.getMessage());
            throw new Exception(e);
        }

/*
        Integer code = json.getInteger("code");
        if (null == code || 0 != code) {
            return null;
        }

        JSONObject data = json.getJSONObject("data");
        if (null == data) {
            System.out.println("get data is null");
        } else {
            System.out.println("got data====== ");
            System.out.println(data.toJSONString());
        }

        return data;
*/
    }

}
