package com.fengchao.sso.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClient {

    private static final String BASE_URL = "https://api.weixin.qq.com" ;
    private static final String ACCESS_TOKEN_PATH = "/sns/oauth2/access_token" ;
    private static final String MIMI_ACCESS_TOKEN_PATH = "/sns/jscode2session" ;
    private static final String USER_INFO_PATH = "/sns/userinfo" ;

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static Client client = null;

    private static TrustManager[] getTrustManager() {
        return new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // Trust all servers
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // Trust all clients
            }
        } };
    }

    public static Client createClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, getTrustManager(), new SecureRandom());

            HostnameVerifier verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostName, SSLSession sslSession) {
                    return true;
                }
            };
            return ClientBuilder.newBuilder().sslContext(ctx).hostnameVerifier(verifier)
                    .build();
        } catch (GeneralSecurityException exception) {
            // Log (or/and) throw exception
            return null;
        }
    }

    public static <T> T getAccessToken(String appId, String secret, String code, Class<T> obj){
        client = createClient();
        WebTarget target = client.target(BASE_URL).path(ACCESS_TOKEN_PATH).queryParam("appid", appId).queryParam("secret", secret).queryParam("code", code).queryParam("grant_type", "authorization_code");
        logger.info(target.getUri().toString());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        T bean = response.readEntity(obj);
        return bean;
    }

    public static <T> T getMiniAccessToken(String appId, String secret, String code, Class<T> obj){
        client = createClient();
        WebTarget target = client.target(BASE_URL).path(MIMI_ACCESS_TOKEN_PATH).queryParam("appid", appId).queryParam("secret", secret).queryParam("js_code", code).queryParam("grant_type", "authorization_code");
        logger.info(target.getUri().toString());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        T bean = response.readEntity(obj);
        return bean;
    }

    public static <T> T getUserInfo(String accessToken, String openId, Class<T> obj){
        client = createClient();
        WebTarget target = client.target(BASE_URL).path(USER_INFO_PATH).queryParam("access_token", accessToken).queryParam("openid", openId).queryParam("lang", "zh_CN");
        logger.info(target.getUri().toString());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        T bean = response.readEntity(obj);
        return bean;
    }

}
