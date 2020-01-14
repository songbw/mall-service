package com.fengchao.aoyi.client.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @Auther: LabanYB
 * @Date: 2019/10/31 09:20
 * @Description:
 */
public class RSAUtil {

    /** 签名算法 **/
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /** 加密算法 **/
    private static final String KEY_ALGORITHM = "RSA";

    /** 私钥 **/
    private static final String PRIVATE_KEY = "";

    /**
     * 验签
     */
    public static String signMyself(String text) {
        byte[] keyBytes = Base64.decodeBase64(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(text.getBytes());
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            System.out.println("签名失败");
        }
        return null;
    }
}
