package com.fengchao.pingan.utils;


import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import com.fengchao.pingan.bean.InitCodeRequestBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Pkcs8Util {

    private static String KEY_FILE = "pkcs/pkcs8_private.pem";

    private static String pkcs8 = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDOvqcZlQpkZv32" +
            "6u67sXEnlD86iRCt0AerY3XtkuEockKQIDcaJj6ZUkD8TSjZUhM8ypuPYdRjqWj7" +
            "Jc4JneZF1V8pmfhmxvkW5ewBaGzgAB08v1ISD8sEmileWFQUlbQw9qIvKJpS9KF3" +
            "fdr1KJe6QWuP4T376rJDHGceIK8tJqudb5Svhh67gubksxo/vE3hjNwLEHnF1Hgo" +
            "H4SS4FXYq0T+DbbRaifuY8sTdpqF42rX0oW7l5Kb/Wn2n0Yp297uBo9QKhHQ0oft" +
            "VpHmubDO48MqGNF1Ywa+MbnNk0aKxQQEDLfdIA1T9qfwywhIQvrPN4lMcArn9lg2" +
            "y4xYWuG7AgMBAAECggEAfTkPHzidzchwBPGxXfAQ+IcbcQn6Fz0MmCurDxXK2OO0" +
            "G3XXyjfl7JnckLTvjAnhaw3RARyhNygyVQRF8p84DY46kIkY4q+bydnDg+rLHzYS" +
            "f2vmetgkAIqbifB4JOuz84A/Jc8zAhH4BtctGxbWJ9NeIgysrXwvMuTuNFpZoD9X" +
            "BZvTUWRH2OQjoPUpomdXWIq+XWeLcG7JKfv5oTw2J/7pZraZdycaiyHg9ev+4RbG" +
            "8SbT0HKBNsVPnFyVf8YbzplajcO2d6me/cUyL6/98sdHuwbLx/yS+EUXeaDj5LVm" +
            "IaQkWwu0Xb2zodj4asFIFrbPW5ZY+w+/hLS/er8xEQKBgQD8i+N7aTCLlQ1WXC5x" +
            "04dkVJkA36uyzId8LrSmyUEhYd2/Aj8FmkLd8Wv3N5XLsHbXGqv/EDpl3CDvFK5a" +
            "R3eN6xG1NvRLLaIk1K/Nsa7DIZ7X+32410S1UUjSHjDQIAUVFAM3D83H3bOPCORi" +
            "theQIOnXehvh7hxcoyLV7Lt8kwKBgQDRkmwPSJgVZzI2ZiOUww7RksTmeVUgSDsQ" +
            "iszggFdH/aeWNlB2Hea6cN6zc/0aDMRHe/oDmd2D/FTva9lYGWSYsi3zs3C7mBOW" +
            "WhBvuu7P94r6rxOTDn/cs+vXTlXHtiA1KZZ5xW8DPSxutw0QEw1GN563EzqL9jP4" +
            "hFcsqa9nOQKBgAW48b2hI412Iig7zH2dymqWlCT17IEPq14//K3uz3//JLQoS99H" +
            "oI5A4y5l3woPhJHZM96CsqAOPdzM/ipjhiz8D5molh6B+TOWcilaBli7kUrZkv62" +
            "OgNVxS2lq3t7zkGtA2mi/QmzV1c9X1dxQtVsOYA9bxZOe+wJckegpprrAoGAfjji" +
            "PJJMhxeQdpMt+vtAgZkUXnvVeYwHhv8SRmUGLKXpud8ctwdrX/97IjpJxtbJoQRa" +
            "oLCbalMgFNOwJA+nNxCZ/J2m2FXP9k5od+Lu0vMVEcdA0uF4wUTcVuEnaRGYdGyH" +
            "5O+03Q6LJGO7ymKvLAtz3HAIYyP2SUfr+Up6CoECgYBaR3YdtofqaEZrZIipPyeR" +
            "uk9fJ8cQ8ShJJIGQH4kbd0WHaktK/ADAr2hwJQCSx18IyHmyOgWfw6B8Hj+/sWuo" +
            "TNgTy4y3UD2S7W48dku4uw1a78iSMOxJFy9a+xvr5RwzBDaiwtUKIY5Hfo0G7CyJ" +
            "xz3encY7Ml8qizR4vkINIQ==";

    public static String getSign(String json) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(pkcs8);
        PrivateKey privatekey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        Signature dsa = Signature.getInstance("SHA1withRSA");      //采用SHA1withRSA加密
        dsa.initSign(privatekey);
        dsa.update(json.getBytes("UTF-8"));    //voucher需要加密的String必须变成byte类型的
        return new String(Base64.encodeBase64(dsa.sign()));
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey1 = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey1);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }


    /**
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, Object> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, Object> tmpMap = paraMap;
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                @Override
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                Object val = item.getValue();
                if (val != null && !"".equals(val)) {
                    String key = item.getKey();
                    if (urlEncode) {
                        val = URLEncoder.encode(((String) val).trim(), "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    /**
     * 生成密文
     *
     * @param bean
     * @return
     */
    public static String getCiphe(InitCodeRequestBean bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("appId").append(bean.getAppId()).append("appKey").append("49b759add6e84ed6bebc69eb85202061").append("randomSeries").append(bean.getRandomSeries()).append("timestamp").append(bean.getTimestamp());
        String cipherText = DigestUtils.md5Hex(buffer.toString());
        return cipherText;
    }

}
