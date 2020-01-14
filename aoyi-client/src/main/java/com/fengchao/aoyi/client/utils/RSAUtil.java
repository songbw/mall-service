package com.fengchao.aoyi.client.utils;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RSAUtil {

    /** 签名算法 **/
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /** 加密算法 **/
    private static final String KEY_ALGORITHM = "RSA";

    /** 私钥 **/
    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDApPEzql843P16\n" +
            "ESyxnv6VB4xR9FV8beK7B4u4kDogxP/UHebwfLzoYXhQp9mHH22cTgNamPmh0PD/\n" +
            "2IJot/pzLIps7J1WXlQn4uOhELKHDmEnLdNdslCo1CVWdN0XrFOZ5a0dlUbhNHQc\n" +
            "L0TVRYNpyez4JuYu+YErTZWO0Gfy29NKq2Qnf4K2tPkxXxUNiX2pk/8ho/JTn0Hu\n" +
            "t6x0GQOpl7VwbQqZc5H8zw27LBqu1ZwKCIgqSpdwjl0BsyUUq94NSCTyUhDA57ae\n" +
            "/NbRZQiuTrlveDVoBypOMATDGWjUEDlltwi+UjbfWfQtXOvH+jukB7Q70WIED9R0\n" +
            "cWPI6qqxAgMBAAECggEBAIs8rDkzgu+OioaXsz4ONONyOTFi8AanRaD4qzwSwnC8\n" +
            "8ktV8X0QK1mqWVOyfb70MD0xBauNtaAHbKj+zAL0NsjYAJUJ0A6Ezz6k9vGLJ/4F\n" +
            "nqK6tHKcV0AvVW1Puh1KKIKrRpyDMF/FkcZtQ8sCGyFzGRuiwexh2cJ6qQY4C2mz\n" +
            "x7EVF/8dZkchnKtKq7VLjTKWBODPHZSrslD3Dq5y7UmUq6VmGngMDpU+aIZbfFJk\n" +
            "cU4ZxsFyfWrhxycvxFErqWX1+B6ZfB59FZo6aa8MyXCiEo03f257a4ifBRbuKE8f\n" +
            "uASWWjq2QLVJ5RCHmCYL2324VnU8qEUXir8+DU11J6ECgYEA5N+XoTsDpmf9Dnjs\n" +
            "mtthLob59Yqr6E2afEX+BVROupyxS5h1FAHAPhzjlv4RrVnkY35VexEJbgYrjTl8\n" +
            "iAJ6QZHtOdVBKnOom586WwlTRzI8+wzC/DHoV1vc/s8QEZU5vCt1mlE2B6HtunGF\n" +
            "w71xOyw6ckhrY7tza0bGk+JJdXsCgYEA13oY8aOaI8uVnDiD/kgrJjNnMeRW82QB\n" +
            "tTRIjK1hEnZXiJ3Oiz9rsW4jmJwiwdOIcLIIdYoM5Uo5COQ8bnUpNah0IqWjweWw\n" +
            "nvV4LttcYN3DeCyb0aEHCImMwW91IdIOWsnZ2CzyAtFGjeapft8l2n+QRbm0b8/P\n" +
            "j1GtrzIQKsMCgYEAz/uLzB1XxMlswdELmfHPzzsjPInGjDSxNUtZejhRzu0DhGHH\n" +
            "yuWIcClKfHTnVXOBwxQT0+D4g74hp5Vc4C/4L0vPmqV7MXum9bxtiDriLamulqjT\n" +
            "K+CidXhqTDeYL5I+xAtuNEymX6s74Bi3aNfnZNFdT2sWA6xdR1gqrkqNXK8CgYAv\n" +
            "7w5reryIZ/qRNRVP1NgceGTleWlKXZ3vpIDbIpqsk7znPEu9OE+JufmbMmic+6Sn\n" +
            "hUNSenJh83/6lWNIIzIWqLGlH3ntYV5IDk09Bzlmpw6bxjinio3Y/Vhdk6DX9DWT\n" +
            "RsH6UA1KSSnL2VstSRDPqX77s9MK6IzA0ie0CIwGJwKBgHTs4ZRyhtzEq9EUdeiv\n" +
            "2yvc1Q5DPjpcITEMePGQQfcMfJIKj5N2Bx0uvokkNcG/wnVXpQ8N9bXy9kw0imdt\n" +
            "kDg8OHKO+Nww4IJY3toUVf+IeD2cpiRoWIm6Bq2ApIRMjMHbsVOnGfpOvItxY5Fk\n" +
            "0G/KJUWQZpSVwAs7x1OmHA7N";

    /**
     * 验签
     */
    public static String signMyself(String text) throws Exception {
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
            log.error("RSA 签名失败");

            throw e;
        }
    }
}
