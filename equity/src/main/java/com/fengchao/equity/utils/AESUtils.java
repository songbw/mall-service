package com.fengchao.equity.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;

public class AESUtils {

    private static String key = "Ext22oMh4JGhBoxRqVtmrw==";

    public static String genKeyAES() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        String base64Str = byte2Base64(key.getEncoded());
        return base64Str;
    }


    //将Base64编码后的AES秘钥转换成SecretKey对象
    public static SecretKey loadKeyAES() throws Exception{
        byte[] bytes = base642Byte(key);
        SecretKeySpec key = new SecretKeySpec(bytes, "AES");
        return key;
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    //加密
    public static String encryptAES(byte[] source) throws Exception{
        byte[] bytes = base642Byte(key);
        SecretKeySpec key = new SecretKeySpec(bytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] aFinalByte = cipher.doFinal(source);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(aFinalByte);
    }

    //解密
    public static byte[] decryptAES(byte[] source, SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(source);
    }


    public static void main(String[] args) {
        try {
//           加密
            String s = encryptAES("32344".getBytes());
//            解密
            SecretKey secretKey = loadKeyAES();
            byte[] bytes = base642Byte(s);
            byte[] aes = decryptAES(bytes, secretKey);
            System.out.println(new String(aes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
