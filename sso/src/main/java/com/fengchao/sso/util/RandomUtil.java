package com.fengchao.sso.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class RandomUtil {

    public static final String JY_APP_KEY = "yd44be8744649862e4" ;
    public static final String JY_APP_SECRET = "ee62887dc83644fca8a76f62e6b046f5" ;

    public static String randomString(String seed, int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(seed.length());
            sb.append(seed.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return randomString(str, length);
    }

    /**
     * 获取签名
     * @param params
     * @param appSecret
     * @return
     */
    public static String getSign(Map<String, String> params, String appSecret) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("sign")) {
                // 拼接参数值字符串并进行utf-8解码，防止中文乱码产生
                String value = "";
                try {
                    value = URLDecoder.decode(entry.getValue(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                params.put(entry.getKey(), value);
            }
        }
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接格式
        StringBuilder valueSb = new StringBuilder();
        for (Map.Entry<String, String> entry : entrys) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(entry.getValue())) {
                valueSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        valueSb.append(appSecret);
        String sign = valueSb.toString();
        try {
            sign = DigestUtils.md5Hex(sign.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static void main(String args[]) {
        StringBuilder sb = new StringBuilder("10MFD21F@@E@5@D5GG2AM2EE26MLF051B84596948");
        sb.insert(2, "%");
        System.out.println(sb);
    }
}
