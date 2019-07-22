package com.fengchao.equity.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class Pkcs8Util {

    private static String KEY_FILE="pkcs/pkcs8_private.pem" ;

    private static String pkcs8 = "";

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJSbwqiRE9PICIlaER+GBLXcZHh0ub3Qm91TFV\n" +
            "uzhp1gjQcgqJyqyrMUqajMBJpN1KfiDQq9HMDSptpgGCSoDDphtyiMVErUlyMeTP2TfCmnLl9PFi\n" +
            "7oRh7HZtAwZ2f0UTjh6nBPzba6UcoprGgYc5v0PhRnyoUROgV44reLRLbQIDAQAB";

    private static String privateKey = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAIlJvCqJET08gIiVoRH4YEtdxkeH\n" +
            "S5vdCb3VMVW7OGnWCNByConKrKsxSpqMwEmk3Up+INCr0cwNKm2mAYJKgMOmG3KIxUStSXIx5M/Z\n" +
            "N8KacuX08WLuhGHsdm0DBnZ/RROOHqcE/NtrpRyimsaBhzm/Q+FGfKhRE6BXjit4tEttAgMBAAEC\n" +
            "gYEAh+HlTixwVa3mgkmnLMQrey0LrYxp95ElrTCwLeyqf4aLHtVhPNTPZnnZH3Mkdz+oj6ybCi6l\n" +
            "7lMxpWljoPPg0VA2xDYWLiElB1QkZl43DzBWcuuXU4AhPTVaYXjhv4+g3irIWCeYQUCNRwRjt3do\n" +
            "I+BmrympMUCr4+IDedcyfoECQQDPgpYsHrR91vcsADWzC9h4mMYZLbH6MjeCXHDhF70fh7rtllmm\n" +
            "yz+cX+KXhDaUXH4h/PzCBzXbMCSMP8626uz9AkEAqV5o9cTpZZsQxKIThzqA6RHgg/GaBNan07zd\n" +
            "mMB55Dei4zxdsPnbC/eNLwPbHNHXsDXicK1RxntEfKcnSStbMQJBAKWUJ8wQvCjljNkZWcACpbqk\n" +
            "/P0+TxO7Wju1E4Uo8gnkvi2ymNrUt29Ju373Sq3bl/H68pzIMBs0MRWQHJwsnOkCQQCGogXNWDow\n" +
            "CpPJuwzK8jaHDy7ps6Q7NGc0aW29f0NlptRUziesBvGZEa+pL+d9gVFzQWI/L4dRv6sxa65O+6gB\n" +
            "AkEAhp3ytHS6QKN6QoelFkasjFB9LT7qmpfwoNV1CKX3sItNdnzZQT5BcD0WKU3E1FCZvEx9ksQH\n" +
            "2p85kn+zX3QSIg==";

    public static String getSign(String json) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PrivateKey privatekey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        Signature dsa = Signature.getInstance("SHA1withRSA");      //采用SHA1withRSA加密
        dsa.initSign(privatekey);
        dsa.update(json.getBytes("UTF-8"));    //voucher需要加密的String必须变成byte类型的
        return new String (Base64.encodeBase64(dsa.sign()));
    }

    public static boolean verify(byte[] data, String sign) throws Exception{
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
     *
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URLENCODE
     * @param keyToLower    是否需要将Key转换为全小写
     *            true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, Object> paraMap, boolean urlEncode, boolean keyToLower)
    {
        String buff = "";
        Map<String, Object> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>()
            {
                @Override
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds)
            {
                Object val = item.getValue() ;
                if (val != null && !"".equals(val))
                {
                    String key = item.getKey();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(((String) val).trim(), "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
//        //系统参数
        String teststr= "1044391000fd194ab888b1aa81c03c3710";
//        String couponCode= "4567";
        // 授权用户名(固定值)
//        map.put("open_id", "20180801050402");// 请求时间戳,时区为GMT+8(北京时间)
        try {
//            加密
            String encryptAES = AESUtils.encryptAES(teststr.getBytes());
//            String couponCodeAES = AESUtils.encryptAES(couponCode.getBytes());
//            //加密后的内容Base64编码
            map.put("open_id", encryptAES);
//            map.put("coupon_code",couponCodeAES);
            String urlMap = formatUrlMap(map, false, false);
//
            String sing = getSign(urlMap);
            System.out.println(sing);
            verify(urlMap.getBytes(), sing);
//            boolean verify = verify(urlMap.getBytes(), sing);
//            System.out.println(verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
