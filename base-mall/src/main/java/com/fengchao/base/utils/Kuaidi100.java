package com.fengchao.base.utils;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.base.config.SMSConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Kuaidi100 {

    private static String kuaidi100URL = Config.getString("kuaidi100.url");
    private static String KEY = Config.getString("kuaidi100.key");
    private static String CUSTOMER = Config.getString("kuaidi100.customer");

    private static String get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpUriRequest request = httpGet;
        HttpContext localContext = new BasicHttpContext();
        HttpResponse response = httpClient.execute(request, localContext);
        return EntityUtils.toString(response.getEntity());
    }

    // 通过快递单号获取是哪个快递公司。
    public static ArrayList<String> queryAutoComNumByKuadi100(String logisticsID) {
        ArrayList<String> coms = new ArrayList<>();
        try {
            if (StringUtils.isBlank(logisticsID)) {
                return null;
            }
            String typeResult = get(SMSConfig.TENT_kuaidiUrl + "/autonumber/auto?num=" + logisticsID + "&key" + SMSConfig.TENT_kuaidiKey);
            JSONArray jsonArray = JSONArray.parseArray(typeResult);
            for (int i = 0; i < jsonArray.size(); i++){
                String comCode = jsonArray.getJSONObject(i).getString("comCode");
                coms.add(comCode);
            }
            if(coms == null){
                return null;
            }
            return coms;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实时查询快递单号
     * @param num			快递单号
     * @return
     */
    public static String synQueryData(String num, String comCode) {
        String phone = null;
        String from = null; //出发地城市
        String to = null; //目的地城市
        int resultv2 = 0; //开通区域解析功能：0-关闭；1-开通
        StringBuilder param = new StringBuilder("{");
        param.append("\"num\":\"").append(num).append("\"");
        param.append(",\"phone\":\"").append(phone).append("\"");
        param.append(",\"from\":\"").append(from).append("\"");
        param.append(",\"to\":\"").append(to).append("\"");
        if(1 == resultv2) {
            param.append(",\"resultv2\":1");
        } else {
            param.append(",\"resultv2\":0");
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("customer", SMSConfig.TENT_kuaidiCustomer);

        param.append(",\"com\":\"").append(comCode).append("\"");
        param.append("}");
        String s = param + SMSConfig.TENT_kuaidiKey + SMSConfig.TENT_kuaidiCustomer;
        String sign = MD5Utils.MD5(s);
        params.put("sign", sign);
        params.put("param", param.toString());
        return post(params);
    }

    /**
     * 发送post请求
     */
    public static String post(Map<String, String> params) {
        StringBuffer response = new StringBuffer("");
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (builder.length() > 0) {
                    builder.append('&');
                }
                builder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                builder.append('=');
                builder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] bytes = builder.toString().getBytes("UTF-8");

            URL url = new URL(SMSConfig.TENT_kuaidiUrl + "/poll/query.do");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(bytes);

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    public static void main(String args[]) {
        System.out.println(synQueryData("0011953635060401", "suning"));
    }
}