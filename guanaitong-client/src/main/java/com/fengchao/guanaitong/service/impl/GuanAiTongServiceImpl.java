package com.fengchao.guanaitong.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.guanaitong.service.IGuanAiTongService;
import com.fengchao.guanaitong.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
public class GuanAiTongServiceImpl implements IGuanAiTongService {

    private final String MEDIA_TYPE="application/x-www-form-urlencoded; charset=utf-8";
    private final String URL_PREFIX = "https://openapi.guanaitong.cc/";//test
    //private final String URL_PREFIX = "https://openapi.guanaitong.com/"; //normal

    private final String TOKEN_CREATE_PATH = "token/create";
    private final String GET_OPEN_ID_PATH = "seller/person/getByAuthCode";
    private final String GET_DETAIL_PATH = "seller/person/getDetailByOpenId";

    private final String GRANT_TYPE_KEY = "grant_type";
    private final String GRANT_TYPE_VALUE = "client_credential";

    private final String APPID_KEY = "appid";
    private final String APPID_VALUE = "20110843";

    private final String APPSECRET_KEY = "appsecret";
    private final String APPSECRET_VALUE = "78dde3cc1e3cab6cbbabbc1bf88faa4e";

    private final String VERSION_KEY = "version";
    private final String VERSION_VALUE = "1.0.0";

    private final String TOKEN_KEY = "access_token";
    private final String TIME_STAMP_KEY = "timestamp";
    private final String SIGN_KEY = "sign";

    private final int HTTP_STATUS_OK = 200;
    private final int RESPONSE_DATA_OK = 0;

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

        Map<String, String> map = new TreeMap<>();
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

    private Map buildFormSignMap(Map m) {

        Map<String, String> tMap = new TreeMap<>();
        String token = getAccessToken();
        tMap.put(TOKEN_KEY, token);
        tMap.put(APPSECRET_KEY, APPSECRET_VALUE);
        //tMap.put(VERSION_KEY, VERSION_VALUE);

        Set<String> keySet = m.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            tMap.put(key,m.get(key).toString());
        }

        return tMap;
    }

    private String getFormSign(Map map) {

        Map m = buildFormSignMap(map);
        String xFormString = map2string(m);

        System.out.println("string for sign : " + xFormString);
        byte[] bytes = xFormString.getBytes();
        return DigestUtils.sha1Hex(bytes);
    }

    private JSONObject tryPost(String path, String xForm) {

        OkHttpClient client = new OkHttpClient();
        JSONObject  json;
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody body = RequestBody.create(mediaType, xForm);
        Request request = new Request.Builder()
                .url(URL_PREFIX+path)
                .post(body).build();

        try {
            Response response = client.newCall(request).execute();

            String responseString = response.body().string();
            System.out.println(responseString);
            int code = response.code();
            if (HTTP_STATUS_OK != code) {
                System.out.println("code = " + String.valueOf(code));
                return null;
            }

            json = JSONObject.parseObject(responseString);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

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
    }


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

    @Override
    public String buildXFormBody(Map map) {

        Map<String, String> theMap = new HashMap<>();
        Long timeStampMs = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long timeStampS = timeStampMs/1000;
        String timeStamp = timeStampS.toString();
        map.put(TIME_STAMP_KEY,timeStamp);

        String sign = getFormSign(map);
        String token = getAccessToken();
        theMap.put(TOKEN_KEY, token);
        theMap.put(SIGN_KEY, sign);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            theMap.put(key, map.get(key).toString());
        }

        String xForm = map2string(theMap);
        System.out.println("buildXForm: " + xForm);
        return xForm;

    }

    @Override
    public String getAccessToken(){

        String cacheToken = RedisUtil.getValue(TOKEN_KEY);
        if (null != cacheToken && !cacheToken.isEmpty()) {
            System.out.println("get cached token: {" + cacheToken + "} ttl="+RedisUtil.ttl(TOKEN_KEY)+"s");
            return cacheToken;
        }

        OkHttpClient client = new OkHttpClient();
        String xFormBody = getTokenRequestMap();
        JSONObject  json;
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody body = RequestBody.create(mediaType, xFormBody);
        Request request = new Request.Builder()
                .url(URL_PREFIX+TOKEN_CREATE_PATH)
                .post(body).build();

        try {
            Response response = client.newCall(request).execute();

            String responseString = response.body().string();
            System.out.println(responseString);
            int code = response.code();
            if (HTTP_STATUS_OK != code) {
                return null;
            }
            System.out.println("code = " + String.valueOf(code));

            json = JSONObject.parseObject(responseString);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

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
                String token = data.getString("access_token");
                Integer expires = data.getInteger("expires_in");
                System.out.println("access_token:"+token);
                System.out.println("expires_in:"+expires.toString());
                RedisUtil.putRedis(TOKEN_KEY,token,expires-5);
                return token;
            } else {
                System.out.println("====get data is null");
            }
        } else {
            System.out.println("===convert String to json failed");
        }

        return null;
    }
}
